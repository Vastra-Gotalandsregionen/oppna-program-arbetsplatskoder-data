package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.Role;
import se.vgregion.arbetsplatskoder.domain.jpa.User;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn2;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn3;
import se.vgregion.arbetsplatskoder.repository.DataRepository;
import se.vgregion.arbetsplatskoder.repository.Prodn1Repository;
import se.vgregion.arbetsplatskoder.repository.Prodn2Repository;
import se.vgregion.arbetsplatskoder.repository.Prodn3Repository;
import se.vgregion.arbetsplatskoder.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static se.vgregion.arbetsplatskoder.intsvc.controller.util.HttpUtil.getUserIdFromRequest;

@Controller
@RequestMapping("/prodn3")
public class Prodn3Controller {

    private final int pageSize = 20;

    @Autowired
    private Prodn3Repository prodn3Repository;

    @Autowired
    private Prodn2Repository prodn2Repository;

    @Autowired
    private Prodn1Repository prodn1Repository;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Page<Prodn3>> getProdn3s(
            @RequestParam(value = "unused", required = false, defaultValue = "false") boolean unused,
            @RequestParam(value = "prodn1", required = false) Integer prodn1Id,
            @RequestParam(value = "prodn2", required = false) Integer prodn2Id,
            @RequestParam(value = "page", required = false) Integer page) {

        HttpServletRequest request = this.request;

        String userId = getUserIdFromRequest(request);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findById(userId).orElseThrow();

        Pageable pageable;
        if (page != null) {
            pageable = PageRequest.of(page, pageSize);
        } else {
            pageable = null;
        }

        if (Role.ADMIN.equals(user.getRole())) {
            return getProdn3sForAdmin(unused, prodn1Id, prodn2Id, pageable);
        } else {
            return getProdn3sForNonAdmin(unused, prodn1Id, prodn2Id, user, pageable);
        }

    }

    private ResponseEntity<Page<Prodn3>> getProdn3sForNonAdmin(
            @RequestParam(value = "unused", required = false, defaultValue = "false") boolean unused,
            @RequestParam(value = "prodn1", required = false) Integer prodn1Id,
            @RequestParam(value = "prodn2", required = false) Integer prodn2Id,
            User user,
            Pageable pageable) {

        // Constrain result by permission and possibly request parameter
        Set<Prodn1> prodn1Filter = user.getProdn1s();
        if (prodn1Id != null) {
            Prodn1 toKeep = prodn1Repository.findById(prodn1Id).orElseThrow();

            // The user should only be able to see the one from the request parameter if it is part of those the
            // user has permission to. For some reason java.util.Set.retainAll() didn't work as expected so we use
            // another way.
            if (prodn1Filter.contains(toKeep)) {
                prodn1Filter = new HashSet<>(Collections.singleton(toKeep));
            }
        }

        // Constrain result by previous constraint and possibly by prodn2
        List<Prodn2> prodn2Filter = prodn2Repository.findAllByProdn1In(prodn1Filter, null).getContent();
        if (prodn2Id != null) {
            Prodn2 prodn2 = prodn2Repository.findById(prodn2Id).orElseThrow();

            if (prodn2Filter.contains(prodn2)) {
                prodn2Filter = new ArrayList<>(Collections.singletonList(prodn2));
            }
        }

        // Constrain by prodn3s which we should exclude from the result.
        Set<Integer> prodn3sNotIn = new HashSet<>();
        if (unused) {
            prodn3sNotIn = dataRepository.findAllJoinProdn3().stream()
                    .filter(data -> data.getProdn3() != null)
                    .map(Data::getProdn3)
                    .map(Prodn3::getId)
                    .collect(Collectors.toSet());
        }

        return ResponseEntity.ok(prodn3Repository.findProdn3s(prodn3sNotIn, prodn2Filter, pageable));
    }

    private ResponseEntity<Page<Prodn3>> getProdn3sForAdmin(
            @RequestParam(value = "unused", required = false, defaultValue = "false") boolean unused,
            @RequestParam(value = "prodn1", required = false) Integer prodn1Id,
            @RequestParam(value = "prodn2", required = false) Integer prodn2Id,
            Pageable pageable) {

        // First possibly filter on prodn3 ids to exclude.
        Set<Integer> prodn3sNotIn = null;
        if (unused) {

            // The method of first fetching all prodn3s which aren't used and than adding more constraints is proven
            // more effecient than including this constraint in a more complex single query.
            Set<Integer> allProdn3sIdReferencedFromDatas = dataRepository.findAllJoinProdn3().stream()
                    .filter(data -> data.getProdn3() != null)
                    .map(Data::getProdn3)
                    .map(Prodn3::getId)
                    .collect(Collectors.toSet());

            prodn3sNotIn = allProdn3sIdReferencedFromDatas;
        }

        // Then possibly filter on the prodn2 level.
        List<Prodn2> prodn2Filter = null;
        if (prodn2Id != null) {
            Prodn2 prodn2 = prodn2Repository.findById(prodn2Id).orElseThrow();

            prodn2Filter = Collections.singletonList(prodn2);
        } else {

            // Then possibly filter on the prodn1 level.
            if (prodn1Id != null /*&& prodn2Id == null*/) {
                // Find by all prodn2s which have the given prodn1.
                Prodn1 prodn1Reference = prodn1Repository.getOne(prodn1Id);

                prodn2Filter = new ArrayList<>(prodn2Repository.findAllByProdn1Equals(prodn1Reference, null)
                        .getContent());
            }
        }

        return ResponseEntity.ok(prodn3Repository.findProdn3s(prodn3sNotIn, prodn2Filter, pageable));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Prodn3 getProdn3(@PathVariable(value = "id", required = true) Integer id) {
        return prodn3Repository.findProdn3ByIdEquals(id);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("@authService.hasProdn1Access(authentication, #prodn3.prodn2.prodn1)")
    public ResponseEntity<Prodn3> saveProdn3(@RequestBody Prodn3 prodn3) {

        if (prodn3.getId() == null) {
            // New entity.
            prodn3.setId(Math.abs(new Random().nextInt()));
        }

        return ResponseEntity.ok(prodn3Repository.save(prodn3));
    }
}
