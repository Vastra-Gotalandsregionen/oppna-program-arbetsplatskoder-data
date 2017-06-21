package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import se.vgregion.arbetsplatskoder.domain.jpa.Role;
import se.vgregion.arbetsplatskoder.domain.jpa.User;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.intsvc.controller.util.HttpUtil;
import se.vgregion.arbetsplatskoder.repository.DataRepository;
import se.vgregion.arbetsplatskoder.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

@Controller
@RequestMapping("/data")
public class DataController {

    private final int pageSize = 20;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Page<Data> getDatas(@RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "query", required = false) String query,
                                @RequestParam(value = "sort", required = false) String sort,
                                @RequestParam(value = "asc", required = false) boolean asc,
                                @RequestParam(value = "onlyMyDatas", required = false) boolean onlyMyDatas) throws NoSuchFieldException {

        Sort.Order sorteringskodProd = new Sort.Order(Sort.Direction.ASC, "prodn1.kortnamn").ignoreCase();
        Sort.Order arbetsplatskod = new Sort.Order(Sort.Direction.ASC, "benamning").ignoreCase();

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

        Pageable pageable = new PageRequest(page == null ? 0 : page, pageSize,
            finalSort);

        Page<Data> result;
        if (query != null && query.length() > 0) {
            if (onlyMyDatas && !Role.ADMIN.equals(getUser().getRole())) {
                Set<Prodn1> prodn1s = getUserProdn1s();
                result = dataRepository.advancedSearch("%" + query.toLowerCase() + "%", pageable, prodn1s);
                //result = dataRepository.advancedSearchByProdn1In("%" + query.toLowerCase() + "%", prodn1s, pageable);
            } else {
                result = dataRepository.advancedSearch("%" + query.toLowerCase() + "%", pageable);
            }
        } else {
            if (onlyMyDatas && !Role.ADMIN.equals(getUser().getRole())) {
                Set<Prodn1> prodn1s = getUserProdn1s();

                result = dataRepository.findAllByProdn1In(prodn1s, pageable);
            } else {
                result = dataRepository.findAll(pageable);
            }
        }

        return result;
    }


    private Set<Prodn1> getUserProdn1s() {
        User user = getUser();

        return user.getProdn1s();
    }

    private User getUser() {
        String userIdFromRequest = HttpUtil.getUserIdFromRequest(request);

        return userRepository.findOne(userIdFromRequest);
    }

    @RequestMapping(value = "/arbetsplatskod/{arbetsplatskod}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Data> getDataByArbetsplatskodlan(@PathVariable("arbetsplatskod") String arbetsplatskod) {
        List<Data> result = dataRepository.findAllByArbetsplatskodEquals(arbetsplatskod);

        if (result.size() > 1) {
            throw new IllegalStateException("No two datas should have the same arbetsplatskod.");
        } else if (result.size() == 0) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.ok(result.get(0));
        }
    }

    @RequestMapping(value = "/ersattav/{ersattav}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Data>> getDataByErsattav(@PathVariable("ersattav") String ersattav) {
        List<Data> result = dataRepository.findAllByErsattavEquals(ersattav);

        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Data getData(@PathVariable("id") Integer id) {
        return dataRepository.findOne(id);
    }

    @RequestMapping(value = "users", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findAllUserIdsWithData() {
        return dataRepository.findAllUserIdsWithData();
    }

    /*@RequestMapping(value = "foo", method = RequestMethod.GET)
    @ResponseBody
    public List<Data> foo() {
        return dataRepository.foo();
    }*/

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity deleteData(@PathVariable("id") Integer id) {
        dataRepository.delete(id);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Data saveData(@RequestBody Data data) {
        User user = getUser();

        Random random = new Random();

        if (data.getId() == null) {
            data.setId(Math.abs(random.nextInt())); // todo Improve this
            data.setDeletable(true);
        }

        if (data.getArbetsplatskod() == null) {
            data.setArbetsplatskod(Math.abs(random.nextInt()) + "");
            data.setArbetsplatskodlan(data.getLankod() + data.getArbetsplatskod());
        }

        data.setUserIdNew(user.getId());

        data.setSsmaTimestamp(new Byte[]{0x00});

        Timestamp now = Timestamp.from(Instant.now());

        if (data.getRegDatum() == null) {
            data.setRegDatum(now);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));

        data.setAndringsdatum(sdf.format(now));

        return dataRepository.saveAndArchive(data);
    }


}
