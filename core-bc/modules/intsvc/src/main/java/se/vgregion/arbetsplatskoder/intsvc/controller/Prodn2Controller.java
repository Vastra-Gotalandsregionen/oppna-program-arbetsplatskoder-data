package se.vgregion.arbetsplatskoder.intsvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn2;
import se.vgregion.arbetsplatskoder.repository.Prodn2Repository;

import java.util.List;

@Controller
@RequestMapping("/prodn2")
public class Prodn2Controller {

    @Autowired
    private Prodn2Repository prodn2Repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Prodn2> getProdn1s(@RequestParam(value = "prodn1", required = false) String prodn1) {
        if (prodn1 != null) {
            return prodn2Repository.findAllByN1Equals(prodn1);
        } else {
            return prodn2Repository.findAll();
        }
    }

    @RequestMapping(value = "/{producentid}", method = RequestMethod.GET)
    @ResponseBody
    public Prodn2 getProdn2(@PathVariable(value = "producentid", required = true) String producentid) {
        return prodn2Repository.findProdn2ByProducentidEquals(producentid);
    }
}
