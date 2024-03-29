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
import org.springframework.web.bind.annotation.*;
import se.vgregion.arbetsplatskoder.domain.jpa.Role;
import se.vgregion.arbetsplatskoder.domain.jpa.User;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn2;
import se.vgregion.arbetsplatskoder.repository.Prodn1Repository;
import se.vgregion.arbetsplatskoder.repository.Prodn2Repository;
import se.vgregion.arbetsplatskoder.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static se.vgregion.arbetsplatskoder.intsvc.controller.util.HttpUtil.getUserIdFromRequest;

@Controller
@RequestMapping("/prodn2")
public class Prodn2Controller {

    private final int defaultPageSize = 20;

    @Autowired
    private Prodn2Repository prodn2Repository;

    @Autowired
    private Prodn1Repository prodn1Repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Page<Prodn2>> getProdn2s(@RequestParam(value = "prodn1", required = false) Integer prodn1Id,
                                         @RequestParam(value = "page", required = false) Integer page) {
        HttpServletRequest request = this.request;

        String userId = getUserIdFromRequest(request);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findById(userId).orElseThrow();

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "prodn1.kortnamn").ignoreCase();
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "kortnamn").ignoreCase();
        Sort.Order[] orders = new Sort.Order[]{order, order2};

        if (Role.ADMIN.equals(user.getRole())) {
            return getProdn2sForAdmin(prodn1Id, page, orders);
        } else {
            return getProdn2sForNonAdmin(prodn1Id, page, user, orders);
        }
    }

    private ResponseEntity<Page<Prodn2>> getProdn2sForNonAdmin(
            @RequestParam(value = "prodn1", required = false) Integer prodn1Id,
            @RequestParam(value = "page", required = false) Integer page,
            User user, Sort.Order[] orders) {

        Set<Prodn1> prodn1s = user.getProdn1s();
        Set<Integer> usersProdn1Ids = prodn1s.stream().map(Prodn1::getId).collect(Collectors.toSet());
        if (prodn1Id != null) {
            // Not admin + prodn1 condition

            if (!usersProdn1Ids.contains(prodn1Id)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Pageable pageable;
            if (page != null) {
                pageable = PageRequest.of(page, defaultPageSize, Sort.by(orders));
            } else {
                pageable = PageRequest.of(0, 1000);
            }

            Prodn1 prodn1Reference = prodn1Repository.getById(prodn1Id);

            return ResponseEntity.ok(prodn2Repository.findAllByProdn1Equals(prodn1Reference, pageable));
        } else {
            // Not admin + user's prodn1s condition
            Pageable pageable = PageRequest.of(page == null ? 0 : page, defaultPageSize, Sort.by(orders));

            return ResponseEntity.ok(prodn2Repository.findAllByProdn1In(prodn1s, pageable));
        }
    }

    private ResponseEntity<Page<Prodn2>> getProdn2sForAdmin(
            @RequestParam(value = "prodn1", required = false) Integer prodn1Id,
            @RequestParam(value = "page", required = false) Integer page,
            Sort.Order[] orders) {

        if (prodn1Id != null) {
            // Admin + prodn1 condition
            Pageable pageable;
            if (page != null) {
                pageable = PageRequest.of(page, defaultPageSize, Sort.by(orders));
            } else {
                pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(orders));
            }

            Prodn1 prodn1Reference = prodn1Repository.getById(prodn1Id);

            return ResponseEntity.ok(prodn2Repository.findAllByProdn1Equals(prodn1Reference, pageable));
        } else {
            // Admin + no condition
            Pageable pageable = PageRequest.of(page == null ? 0 : page, defaultPageSize, Sort.by(orders));

            return ResponseEntity.ok(prodn2Repository.findAll(pageable));
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Prodn2 getProdn2(@PathVariable(value = "id", required = true) Integer id) {
        return prodn2Repository.findProdn2ByIdEquals(id);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("@authService.hasProdn1Access(authentication, #prodn2.prodn1)")
    public ResponseEntity<Prodn2> saveProdn2(@RequestBody Prodn2 prodn2) {

        if (prodn2.getId() == null) {
            // New entity.
            prodn2.setId(Math.abs(new Random().nextInt()));
        }

        return ResponseEntity.ok(prodn2Repository.save(prodn2));
    }
}
