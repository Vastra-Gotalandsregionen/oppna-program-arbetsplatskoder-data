package se.vgregion.arbetsplatskoder.intsvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Agarform;
import se.vgregion.arbetsplatskoder.repository.AgarformRepository;

import java.util.List;

@Controller
@RequestMapping("/agarform")
public class AgarformController {

    @Autowired
    private AgarformRepository agarformRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Agarform> getAgarforms() {
        return agarformRepository.findAll();
    }
}
