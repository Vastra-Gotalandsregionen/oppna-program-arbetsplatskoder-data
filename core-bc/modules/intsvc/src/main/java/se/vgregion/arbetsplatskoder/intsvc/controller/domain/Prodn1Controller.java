package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import se.vgregion.arbetsplatskoder.repository.DataRepository;
import se.vgregion.arbetsplatskoder.repository.Prodn1Repository;
import se.vgregion.arbetsplatskoder.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static se.vgregion.arbetsplatskoder.intsvc.controller.util.HttpUtil.getUserIdFromRequest;

@Controller
@RequestMapping("/prodn1")
public class Prodn1Controller {

    @Autowired
    private Prodn1Repository prodn1Repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Prodn1>> getProdn1s(
            @RequestParam(value = "orphan", defaultValue = "false") boolean orphan,
            @RequestParam(value = "id", required = false) Integer id) {

        HttpServletRequest request = this.request;

        String userId = getUserIdFromRequest(request);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findOne(userId);

        List<Prodn1> result;
        if (Role.ADMIN.equals(user.getRole())) {

            if (id == null) {
                result = prodn1Repository.findAllByOrderByForetagsnamnAsc();
            } else {
                throw new RuntimeException("Use /api/prodn1/{id} instead."); // todo Remove this
            }

        } else {
            result = new ArrayList<>(user.getProdn1s());
        }

        if (orphan) {
            Iterator<Prodn1> iterator = result.iterator();

            while (iterator.hasNext()) {
                HashSet<Prodn1> prodn1s = new HashSet<>(Collections.singletonList(iterator.next()));
                List<Data> allByProdn1In = dataRepository.findAllByProdn1In(prodn1s);
                if (allByProdn1In.size() > 0) {
                    iterator.remove();
                }
            }
        }

        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Prodn1 getProdn1(@PathVariable(value = "id", required = true) Integer id) {
        return prodn1Repository.findOne(id);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    // todo secure
    public ResponseEntity<Prodn1> saveProdn1(@RequestBody Prodn1 prodn1) {

        if (prodn1.getId() == null) {
            // New entity.
            prodn1.setId(Math.abs(new Random().nextInt()));
        }

        prodn1.setSsmaTimestamp(new Byte[]{0x00}); // todo What to do with these?

        return ResponseEntity.ok(prodn1Repository.save(prodn1));
    }
}
