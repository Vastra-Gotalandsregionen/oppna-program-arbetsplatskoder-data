package se.vgregion.arbetsplatskoder.intsvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import se.vgregion.arbetsplatskoder.domain.Data;
import se.vgregion.arbetsplatskoder.repository.DataRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;

@Controller
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataRepository dataRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Page<Data> getDatas(@RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "query", required = false) String query,
                               @RequestParam(value = "sort", required = false) String sort,
                               @RequestParam(value = "asc", required = false) boolean asc) throws NoSuchFieldException {

        Sort.Order sorteringskodProd = new Sort.Order(Sort.Direction.ASC, "sorteringskodProd").ignoreCase();
        Sort.Order arbetsplatskod = new Sort.Order(Sort.Direction.ASC, "arbetsplatskod").ignoreCase();

        Sort finalSort;
        if (sort != null && sort.length() > 0) {
            Sort.Order dynamicSort;

            Class<?> type = Data.class.getDeclaredField(sort).getType();

            if (type.equals(String.class)) {
                dynamicSort = new Sort.Order(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sort).ignoreCase();
            } else {
                dynamicSort = new Sort.Order(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
            }
            finalSort = new Sort(dynamicSort, sorteringskodProd, arbetsplatskod);
        } else {
            finalSort = new Sort(sorteringskodProd, arbetsplatskod);
        }

        Pageable pageable = new PageRequest(page == null ? 0 : page, 25,
                finalSort);

        Page<Data> result;
        if (query != null && query.length() > 0) {
            result = dataRepository.advancedSearch("%" + query.toLowerCase() + "%", pageable);
        } else {
            result = dataRepository.findAll(pageable);
        }

        return result;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Data getData(@PathVariable("id") Integer id) {
        return dataRepository.findOne(id);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Data saveData(@RequestBody Data data) {
        Random random = new Random();

        data.setId(Math.abs(random.nextInt()));
        data.setArbetsplatskod(Math.abs(random.nextInt()) + "");
        data.setSsmaTimestamp(new Byte[]{0x00});
        data.setRegDatum(Timestamp.from(Instant.now()));

        return dataRepository.save(data);
    }

}
