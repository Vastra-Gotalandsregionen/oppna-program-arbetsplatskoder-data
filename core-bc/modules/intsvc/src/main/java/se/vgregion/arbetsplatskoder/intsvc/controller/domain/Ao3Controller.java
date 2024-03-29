package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Ao3;
import se.vgregion.arbetsplatskoder.export.repository.Ao3ExportRepository;
import se.vgregion.arbetsplatskoder.repository.Ao3Repository;
import se.vgregion.arbetsplatskoder.service.Ao3Operations;

import java.util.Random;

@Controller
@RequestMapping("/ao3")
public class Ao3Controller {

    @Autowired
    private Ao3Repository ao3Repository;

    @Autowired
    private Ao3Operations ao3Operations;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Page<Ao3> getAo3s(@RequestParam(value = "pageSize", required = false) Integer pageSize,
                             @RequestParam(value = "page", required = false) Integer page) {

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "foretagsnamn").ignoreCase();
        Sort sort = Sort.by(order);

        Pageable pageable;

        if (pageSize != null && page != null) {
            pageable = PageRequest.of(page, pageSize, sort);
        } else {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        }

        return ao3Repository.findAll(pageable);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Ao3 getAo3(@PathVariable("id") Integer id) {
        return ao3Repository.findById(id).orElse(null);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity<Ao3> saveAo3(@RequestBody Ao3 ao3) {

        if (ao3.getId() == null) {
            // New entity.
            ao3.setId(Math.abs(new Random().nextInt()));
        }

        ao3Operations.export(ao3 = ao3Operations.save(ao3));
        return ResponseEntity.ok(ao3);
    }
}
