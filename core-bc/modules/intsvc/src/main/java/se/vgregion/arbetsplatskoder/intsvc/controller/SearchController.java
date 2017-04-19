package se.vgregion.arbetsplatskoder.intsvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.json.Unit;
import se.vgregion.arbetsplatskoder.service.UnitSearchService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private UnitSearchService unitSearchService;

    @RequestMapping(value = "/unit", method = RequestMethod.GET)
    @ResponseBody
    public List<Unit> searchUnits(@RequestParam(value = "query", required = false) String query) {

        if (query == null || "".equals(query)) {
            return new ArrayList<>();
        }

        List<Unit> units = unitSearchService.searchUnits(query);

        if (units != null && units.size() > 30) {
            return units.subList(0, 30);
        }

        return units;
    }

}
