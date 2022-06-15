package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Verksamhet;
import se.vgregion.arbetsplatskoder.repository.VerksamhetRepository;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/verksamhet")
public class VerksamhetController {

    @Autowired
    private VerksamhetRepository verksamhetRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Verksamhet> getVerksamhets() {
        PageRequest pageRequest = PageRequest.of(
                0,
                Integer.MAX_VALUE,
                Sort.by(new Sort.Order(Sort.Direction.ASC, "verksamhettext").ignoreCase())
        );

        return verksamhetRepository.findAll(pageRequest).getContent();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Verksamhet getVardform(@PathVariable("id") Integer id) {
        return verksamhetRepository.findById(id).orElse(null);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity<Verksamhet> saveVerksamhet(@RequestBody Verksamhet verksamhet) {

        if (verksamhet.getId() == null) {
            // New entity.
            verksamhet.setId(Math.abs(new Random().nextInt()));
        }

        return ResponseEntity.ok(verksamhetRepository.save(verksamhet));
    }

}
