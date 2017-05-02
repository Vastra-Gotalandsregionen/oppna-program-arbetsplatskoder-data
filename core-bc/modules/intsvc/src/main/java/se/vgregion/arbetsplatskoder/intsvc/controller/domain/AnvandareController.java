package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Anvandare;
import se.vgregion.arbetsplatskoder.repository.AnvandareRepository;

import java.util.List;

@Controller
@RequestMapping("/anvandare")
public class AnvandareController {

    @Autowired
    private AnvandareRepository anvandareRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Anvandare> getAnvandares() {
        return anvandareRepository.findAll();
    }
}
