package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn3;
import se.vgregion.arbetsplatskoder.repository.Prodn3Repository;

@Controller
@RequestMapping("/prodn3")
public class Prodn3Controller {

    @Autowired
    private Prodn3Repository prodn3Repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Page<Prodn3> getProdn3s(@RequestParam(value = "prodn2", required = false) String prodn2,
                                   @RequestParam(value = "page", required = false) Integer page) {
        if (prodn2 != null) {
            Pageable pageable = new PageRequest(page == null ? 0 : page, Integer.MAX_VALUE);

            return prodn3Repository.findAllByN2Equals(prodn2, pageable);
        } else {
            Pageable pageable = new PageRequest(page == null ? 0 : page, 25);

            return prodn3Repository.findAll(pageable);
        }
    }

    @RequestMapping(value = "/{producentid}", method = RequestMethod.GET)
    @ResponseBody
    public Prodn3 getProdn3(@PathVariable(value = "producentid", required = true) String producentid) {
        return prodn3Repository.findProdn3ByProducentidEquals(producentid);
    }
}
