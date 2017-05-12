package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Ao3;
import se.vgregion.arbetsplatskoder.repository.Ao3Repository;

import java.util.Random;

@Controller
@RequestMapping("/ao3")
public class Ao3Controller {

    @Autowired
    private Ao3Repository ao3Repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Page<Ao3> getAo3s(@RequestParam(value = "pageSize", required = false) Integer pageSize,
                             @RequestParam(value = "page", required = false) Integer page) {

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "foretagsnamn");
        Sort sort = new Sort(order);

        Pageable pageable = null;

        if (pageSize != null && page != null) {
            pageable = new PageRequest(page, pageSize, sort);
        } else {
            pageable = new PageRequest(0, Integer.MAX_VALUE, sort);
        }

        return ao3Repository.findAll(pageable);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Ao3 getAo3(@PathVariable("id") Integer id) {
        return ao3Repository.findOne(id);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    // todo secure
    public ResponseEntity<Ao3> saveAo3(@RequestBody Ao3 ao3) {

        if (ao3.getId() == null) {
            // New entity.
            ao3.setId(Math.abs(new Random().nextInt()));
        }

        ao3.setSsmaTimestamp(new Byte[]{0x00}); // todo What to do with these?

        return ResponseEntity.ok(ao3Repository.save(ao3));
    }
}
