package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.DataExport;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.domain.json.ErrorMessage;
import se.vgregion.arbetsplatskoder.intsvc.controller.util.HttpUtil;
import se.vgregion.arbetsplatskoder.repository.DataRepository;
import se.vgregion.arbetsplatskoder.repository.UserRepository;
import se.vgregion.arbetsplatskoder.service.DataOperations;
import se.vgregion.arbetsplatskoder.util.DateUtil;
import se.vgregion.arbetsplatskoder.util.ReflectionUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private DataOperations dataOperations;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("@authService.isLoggedIn(authentication)")
    public Page<Data> getDatas(@RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "query", required = false) String query,
                               @RequestParam(value = "sort", required = false) String sort,
                               @RequestParam(value = "asc", required = false) boolean asc,
                               @RequestParam(value = "onlyMyDatas", required = false) boolean onlyMyDatas,
                               @RequestParam(value = "onlyActiveDatas", required = false) boolean onlyActiveDatas) {

        Sort.Order sorteringskodProd = new Sort.Order(Sort.Direction.ASC, "prodn1.kortnamn").ignoreCase();
        Sort.Order arbetsplatskod = new Sort.Order(Sort.Direction.ASC, "benamning").ignoreCase();

        Sort finalSort;
        if (sort != null && sort.length() > 0) {
            Sort.Order dynamicSort;

            Class<?> type = ReflectionUtil.getDeclaredField(sort, Data.class).getType();

            if (type.equals(String.class)) {
                dynamicSort = new Sort.Order(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sort).ignoreCase();
            } else {
                dynamicSort = new Sort.Order(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
            }
            finalSort = Sort.by(dynamicSort, sorteringskodProd, arbetsplatskod);
        } else {
            finalSort = Sort.by(sorteringskodProd, arbetsplatskod);
        }

        Pageable pageable = PageRequest.of(page == null ? 0 : page, pageSize,
                finalSort);

        Page<Data> result;
        Date validToDate = null;

        if (onlyActiveDatas) {
            validToDate = DateUtil.aYearAgo();
        }

        if (query == null) {
            query = "";
        }

        if (onlyMyDatas && !Role.ADMIN.equals(getUser().getRole())) {
            Set<Prodn1> prodn1s = getUserProdn1s();
            result = dataRepository.advancedSearch(query.toLowerCase(), pageable, prodn1s, validToDate);
            //result = dataRepository.advancedSearchByProdn1In("%" + query.toLowerCase() + "%", prodn1s, pageable);
        } else {
            result = dataRepository.advancedSearch(query.toLowerCase(), pageable, null, validToDate);
        }

        return result;
    }


    private Set<Prodn1> getUserProdn1s() {
        User user = getUser();

        return user.getProdn1s();
    }

    private User getUser() {
        String userIdFromRequest = HttpUtil.getUserIdFromRequest(request);

        return userRepository.findById(userIdFromRequest).orElse(null);
    }

    @RequestMapping(value = "/arbetsplatskodlan/{arbetsplatskodlan}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("@authService.isLoggedIn(authentication)")
    public ResponseEntity<Data> getDataByArbetsplatskodlan(@PathVariable("arbetsplatskodlan") String arbetsplatskodlan) {
        List<Data> result = dataRepository.findAllByArbetsplatskodlanEquals(arbetsplatskodlan);

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
    @PreAuthorize("@authService.isLoggedIn(authentication)")
    public ResponseEntity<List<Data>> getDataByErsattav(@PathVariable("ersattav") String ersattav) {
        List<Data> result = dataRepository.findAllByErsattavEquals(ersattav);

        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/highestBeginningWith/{arbetsplatskodlanBeginning}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("@authService.isLoggedIn(authentication)")
    public ResponseEntity<List<Data>> findHighestBeginningWith(@PathVariable("arbetsplatskodlanBeginning") String arbetsplatskodlanBeginning) {
        Data result = dataRepository.findHighestBeginningWith(arbetsplatskodlanBeginning);

        List<Data> datas = new ArrayList<>();
        if (result != null) {
            datas.add(result);
        }

        return ResponseEntity.ok(datas);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("@authService.isLoggedIn(authentication)")
    public Data getData(@PathVariable("id") Integer id) {
        return dataRepository.findById(id).orElse(null);
    }

    @RequestMapping(value = "users", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("@authService.isLoggedIn(authentication)")
    public List<String> findAllUserIdsWithData() {
        return dataRepository.findAllUserIdsWithData();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("@authService.isLoggedIn(authentication)")
    public ResponseEntity deleteData(@PathVariable("id") Integer id) {
        if (!(getUser().getProdn1s().contains(dataRepository.findById(id).orElseThrow().getProdn1())
                || getUser().getRole().equals(Role.ADMIN))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        dataOperations.unexport(id);
        dataRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("@authService.isLoggedIn(authentication)")
    public ResponseEntity<Object> saveData(@RequestBody Data data) {
        User user = getUser();

        if (!(user.getProdn1s().contains(data.getProdn1()) || user.getRole().equals(Role.ADMIN))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (data.getId() != null) {
            // Already persisted entity. Check that the user has permission to that entity.
            Data persisted = dataRepository.findById(data.getId()).orElseThrow();
            if (!(user.getProdn1s().contains(persisted.getProdn1()) || user.getRole().equals(Role.ADMIN))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Validate we're not trying to change fields which we're not allowed to change.
            if (!persisted.getArbetsplatskodlan().equals(data.getArbetsplatskodlan())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        // Validate HSA-ID
        String hsaid = data.getHsaid();

        boolean empty = hsaid == null || hsaid.length() == 0 || hsaid.toLowerCase().equals("saknas");

        if (!empty && (hsaid.length() != 26 || !hsaid.startsWith("SE") ||
                !(hsaid.charAt(13) == 'E' || hsaid.charAt(13) == 'F'))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Ogiltigt HSA-ID"));
        }

        Random random = new Random();

        if (data.getId() == null) {
            // A new entity
            data.setId(Math.abs(random.nextInt())); // todo Improve this
            data.setDeletable(true);

            if (data.getArbetsplatskodlan() != null) {
                List<Data> byArbetsplatskodlan = dataRepository
                        .findAllByArbetsplatskodlanEquals(data.getArbetsplatskodlan());

                if (byArbetsplatskodlan.size() > 0) {
                    ErrorMessage errorMessage = new ErrorMessage("Angiven arbetsplatskod finns redan.");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
                }
            }
        }

        if (data.getArbetsplatskodlan() == null) {
            data.setArbetsplatskod(Math.abs(random.nextInt()) + "");
            data.setArbetsplatskodlan(data.getLankod() + data.getArbetsplatskod());
        }

        if (data.getArbetsplatskod() == null) {
            String lankod = data.getLankod() + "";
            data.setArbetsplatskod(data.getArbetsplatskodlan().substring(lankod.length()));
        }

        data.setUserIdNew(user.getId());

        Timestamp now = Timestamp.from(Instant.now());

        if (data.getRegDatum() == null) {
            data.setRegDatum(now);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));

        data.setAndringsdatum(sdf.format(now));

        data = dataOperations.saveAndArchive(data);
//        DataExport de = new DataExport(data);

        dataOperations.export(data);

        return ResponseEntity.ok(data);
    }

}
