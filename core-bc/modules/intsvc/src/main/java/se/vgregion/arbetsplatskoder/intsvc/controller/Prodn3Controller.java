package se.vgregion.arbetsplatskoder.intsvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.Prodn3;
import se.vgregion.arbetsplatskoder.repository.Prodn3Repository;

import java.util.List;

@Controller
@RequestMapping("/prodn3")
public class Prodn3Controller {

    @Autowired
    private Prodn3Repository prodn3Repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Prodn3> getProdn3s(@RequestParam(value = "prodn2", required = false) String prodn2) {
        if (prodn2 != null) {
            return prodn3Repository.findAllByN2Equals(prodn2);
        } else {
            return prodn3Repository.findAll();
        }
    }

    @RequestMapping(value = "/{producentid}", method = RequestMethod.GET)
    @ResponseBody
    public Prodn3 getProdn3(@PathVariable(value = "producentid", required = true) String producentid) {
        return prodn3Repository.findProdn3ByProducentidEquals(producentid);
    }
}
