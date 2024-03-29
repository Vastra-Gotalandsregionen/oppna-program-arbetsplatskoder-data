package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Vardform;
import se.vgregion.arbetsplatskoder.repository.VardformRepository;

import java.util.Random;

@Controller
@RequestMapping("/vardform")
public class VardformController {

    @Autowired
    private VardformRepository vardformRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Page<Vardform> getVardforms() {
        PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(new Sort.Order(Sort.Direction.ASC, "vardformid").ignoreCase()));

        return vardformRepository.findAll(pageRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Vardform getVardform(@PathVariable("id") Integer id) {
        return vardformRepository.findById(id).orElse(null);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity<Vardform> saveVardform(@RequestBody Vardform vardform) {

        if (vardform.getId() == null) {
            // New entity.
            vardform.setId(Math.abs(new Random().nextInt()));
        }

        return ResponseEntity.ok(vardformRepository.save(vardform));
    }
}
