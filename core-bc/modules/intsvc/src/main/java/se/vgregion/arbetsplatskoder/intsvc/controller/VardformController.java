package se.vgregion.arbetsplatskoder.intsvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.Vardform;
import se.vgregion.arbetsplatskoder.repository.VardformRepository;

import java.util.List;

@Controller
@RequestMapping("/vardform")
public class VardformController {

    @Autowired
    private VardformRepository vardformRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Vardform> getVardforms() {
        return vardformRepository.findAll();
    }
}
