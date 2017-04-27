package se.vgregion.arbetsplatskoder.intsvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Ao3;
import se.vgregion.arbetsplatskoder.repository.Ao3Repository;

import java.util.List;

@Controller
@RequestMapping("/ao3")
public class Ao3Controller {

    @Autowired
    private Ao3Repository ao3Repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Ao3> getAo3s() {
        return ao3Repository.findAll();
    }
}
