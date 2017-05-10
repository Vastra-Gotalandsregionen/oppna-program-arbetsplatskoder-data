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
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn2;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn3;
import se.vgregion.arbetsplatskoder.repository.Prodn2Repository;
import se.vgregion.arbetsplatskoder.repository.Prodn3Repository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/prodn3")
public class Prodn3Controller {

    private final int pageSize = 20;

    @Autowired
    private Prodn3Repository prodn3Repository;

    @Autowired
    private Prodn2Repository prodn2Repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Page<Prodn3> getProdn3s(@RequestParam(value = "prodn1", required = false) String prodn1,
                                   @RequestParam(value = "prodn2", required = false) String prodn2,
                                   @RequestParam(value = "page", required = false) Integer page) {

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "producentid").ignoreCase();

        if (prodn1 != null && prodn2 == null) {
            Pageable pageable = new PageRequest(page == null ? 0 : page, pageSize, new Sort(order));

            List<String> n2sByN1 = prodn2Repository.findAllByN1Equals(prodn1, null)
                    .stream()
                    .map(Prodn2::getProducentid)
                    .collect(Collectors.toList());

            return prodn3Repository.findAllByN2In(n2sByN1, pageable);
        }

        if (prodn2 != null) {
            // The MAX_VALUE for page pageSize since we use this in an autocomplete component. Rethink design otherwise...
            Pageable pageable = new PageRequest(page == null ? 0 : page, Integer.MAX_VALUE, new Sort(order));

            return prodn3Repository.findAllByN2Equals(prodn2, pageable);
        } else {
            Pageable pageable = new PageRequest(page == null ? 0 : page, pageSize, new Sort(order));

            return prodn3Repository.findAll(pageable);
        }
    }

    @RequestMapping(value = "/{producentid}", method = RequestMethod.GET)
    @ResponseBody
    public Prodn3 getProdn3(@PathVariable(value = "producentid", required = true) String producentid) {
        return prodn3Repository.findProdn3ByProducentidEquals(producentid);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    // todo secure
    public ResponseEntity<Prodn3> saveProdn3(@RequestBody Prodn3 prodn3) {

        if (prodn3.getId() == null) {
            // New entity.
            prodn3.setId(Math.abs(new Random().nextInt()));
        }

        prodn3.setSsmaTimestamp(new Byte[]{0x00}); // todo What to do with these?

        return ResponseEntity.ok(prodn3Repository.save(prodn3));
    }
}
