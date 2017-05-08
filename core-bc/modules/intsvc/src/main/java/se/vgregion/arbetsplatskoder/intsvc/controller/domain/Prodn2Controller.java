package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
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
import se.vgregion.arbetsplatskoder.repository.Prodn2Repository;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/prodn2")
public class Prodn2Controller {

    @Autowired
    private Prodn2Repository prodn2Repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Prodn2> getProdn2s(@RequestParam(value = "prodn1", required = false) String prodn1) {
        if (prodn1 != null) {
            Sort.Order order = new Sort.Order(Sort.Direction.ASC, "producentid").ignoreCase();
            return prodn2Repository.findAllByN1Equals(prodn1, new Sort(order));
        } else {
            Sort.Order order = new Sort.Order(Sort.Direction.ASC, "producentid").ignoreCase();
            return prodn2Repository.findAll(new Sort(order));
        }
    }

    @RequestMapping(value = "/{producentid}", method = RequestMethod.GET)
    @ResponseBody
    public Prodn2 getProdn2(@PathVariable(value = "producentid", required = true) String producentid) {
        return prodn2Repository.findProdn2ByProducentidEquals(producentid);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    // todo secure
    public ResponseEntity<Prodn2> saveProdn1(@RequestBody Prodn2 prodn2) {

        if (prodn2.getId() == null) {
            // New entity.
            prodn2.setId(Math.abs(new Random().nextInt()));
        }

        prodn2.setSsmaTimestamp(new Byte[]{0x00}); // todo What to do with these?

        return ResponseEntity.ok(prodn2Repository.save(prodn2));
    }
}
