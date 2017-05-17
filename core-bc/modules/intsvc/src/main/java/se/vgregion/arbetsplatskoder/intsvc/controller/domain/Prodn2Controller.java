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
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn2;
import se.vgregion.arbetsplatskoder.repository.Prodn1Repository;
import se.vgregion.arbetsplatskoder.repository.Prodn2Repository;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/prodn2")
public class Prodn2Controller {

    private final int defaultPageSize = 20;

    @Autowired
    private Prodn2Repository prodn2Repository;

    @Autowired
    private Prodn1Repository prodn1Repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Page<Prodn2> getProdn2s(@RequestParam(value = "prodn1", required = false) Integer prodn1Id,
                                         @RequestParam(value = "page", required = false) Integer page) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "avdelning").ignoreCase();

        if (prodn1Id != null) {
            // The MAX_VALUE for page defaultPageSize since we use this in an autocomplete component. Rethink design otherwise... TODO let client specify defaultPageSize!
            Pageable pageable = new PageRequest(page == null ? 0 : page, Integer.MAX_VALUE, new Sort(order));

            Prodn1 prodn1Reference = prodn1Repository.getOne(prodn1Id);

            return prodn2Repository.findAllByProdn1Equals(prodn1Reference, pageable);
        } else {
            Pageable pageable = new PageRequest(page == null ? 0 : page, defaultPageSize, new Sort(order));

            return prodn2Repository.findAll(pageable);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Prodn2 getProdn2(@PathVariable(value = "id", required = true) Integer id) {
        return prodn2Repository.findProdn2ByIdEquals(id);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public List<Prodn2> searchProdn2s(@RequestParam(value = "query", required = false) String query) {
        return prodn2Repository.search(query);
    }


    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    // todo secure
    public ResponseEntity<Prodn2> saveProdn2(@RequestBody Prodn2 prodn2) {

        if (prodn2.getId() == null) {
            // New entity.
            prodn2.setId(Math.abs(new Random().nextInt()));
        }

        prodn2.setSsmaTimestamp(new Byte[]{0x00}); // todo What to do with these?

        return ResponseEntity.ok(prodn2Repository.save(prodn2));
    }
}
