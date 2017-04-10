package se.vgregion.arbetsplatskoder.intsvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.Verksamhet;
import se.vgregion.arbetsplatskoder.repository.VerksamhetRepository;

import java.util.List;

@Controller
@RequestMapping("/verksamhet")
public class VerksamhetController {

    @Autowired
    private VerksamhetRepository verksamhetRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Verksamhet> getAgarforms() {
        return verksamhetRepository.findVerksamhetsByRaderadIsFalse();
    }
}
