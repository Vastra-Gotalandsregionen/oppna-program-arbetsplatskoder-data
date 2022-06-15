package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.ArchivedData;
import se.vgregion.arbetsplatskoder.repository.ArchivedDataRepository;
import se.vgregion.arbetsplatskoder.repository.DataRepository;

import java.util.List;

@Controller
@RequestMapping("/archivedData")
public class ArchivedDataController {

    @Autowired
    private ArchivedDataRepository archivedDataRepository;

    @Autowired
    private DataRepository dataRepository;

    @RequestMapping(value = "/{dataId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ArchivedData> findByReplacer(@PathVariable("dataId") Integer dataId) {
        return archivedDataRepository.findAllByReplacerEqualsOrderByAndringsdatumDesc(
                dataRepository.findById(dataId).orElseThrow()
        );
    }
}
