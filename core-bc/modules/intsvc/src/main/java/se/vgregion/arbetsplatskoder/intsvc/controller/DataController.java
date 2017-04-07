package se.vgregion.arbetsplatskoder.intsvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.Data;
import se.vgregion.arbetsplatskoder.repository.DataRepository;

@Controller
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataRepository dataRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Page<Data> getDatas(@RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "query", required = false) String query) {

        Sort.Order sorteringskodProd = new Sort.Order(Sort.Direction.ASC, "sorteringskodProd").ignoreCase();
        Sort.Order arbetsplatskod = new Sort.Order(Sort.Direction.ASC, "arbetsplatskod").ignoreCase();

        Pageable pageable = new PageRequest(page == null ? 0 : page, 25,
                new Sort(sorteringskodProd, arbetsplatskod));

        Page<Data> result;
        if (query != null && query.length() > 0) {
            result = dataRepository.advancedSearch("%" + query.toLowerCase() + "%", pageable);
        } else {
            result = dataRepository.findAll(pageable);
        }

        return result;
        //return shop;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Data getData(@PathVariable("id") Integer id) {
        return dataRepository.findOne(id);
    }



}
