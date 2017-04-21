package se.vgregion.arbetsplatskoder.intsvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.Prodn1;
import se.vgregion.arbetsplatskoder.repository.Prodn1Repository;

import java.util.List;

@Controller
@RequestMapping("/prodn1")
public class Prodn1Controller {

    @Autowired
    private Prodn1Repository prodn1Repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Prodn1> getProdn1s() {
        return prodn1Repository.findAllByOrderByForetagsnamn();
    }

    @RequestMapping(value = "/{producentid}", method = RequestMethod.GET)
    @ResponseBody
    public Prodn1 getProdn1(@PathVariable(value = "producentid", required = true) String producentid) {
        return prodn1Repository.findProdn1ByProducentidEquals(producentid);
    }
}
