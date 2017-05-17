package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn2;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn3;
import se.vgregion.arbetsplatskoder.repository.DataRepository;
import se.vgregion.arbetsplatskoder.repository.Prodn1Repository;
import se.vgregion.arbetsplatskoder.repository.Prodn2Repository;
import se.vgregion.arbetsplatskoder.repository.Prodn3Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/prodn3")
public class Prodn3Controller {

    private final int pageSize = 20;

    @Autowired
    private Prodn3Repository prodn3Repository;

    @Autowired
    private Prodn2Repository prodn2Repository;

    @Autowired
    private Prodn1Repository prodn1Repository;

    @Autowired
    private DataRepository dataRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Page<Prodn3> getProdn3s(@RequestParam(value = "unused", required = false, defaultValue = "false") boolean unused,
                                   @RequestParam(value = "orphan", required = false, defaultValue = "false") boolean orphan,
                                   @RequestParam(value = "prodn1", required = false) Integer prodn1Id,
                                   @RequestParam(value = "prodn2", required = false) Integer prodn2Id,
                                   @RequestParam(value = "page", required = false) Integer page) {

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id").ignoreCase();

        if (unused) {
            Pageable pageable = new PageRequest(page == null ? 0 : page, pageSize, new Sort(order));

            Set<Integer> allProdn3sIdReferencedFromDatas = dataRepository.findAllJoinProdn3().stream()
                    .filter(data -> data.getProdn3() != null)
                    .map(Data::getProdn3)
                    .map(Prodn3::getId)
                    .collect(Collectors.toSet());

            return prodn3Repository.findAllByIdNotIn(allProdn3sIdReferencedFromDatas, pageable);
        }

        if (orphan) {
            return prodn3Repository.findAllByProdn2IsNull(null);
        }

        if (prodn1Id != null && prodn2Id == null) {
            // Find by all prodn2s which have the given prodn1.
            Pageable pageable = new PageRequest(page == null ? 0 : page, pageSize, new Sort(order));

            Prodn1 prodn1Reference = prodn1Repository.getOne(prodn1Id);

            List<Prodn2> n2sByProdn1 = new ArrayList<>(prodn2Repository.findAllByProdn1Equals(prodn1Reference, null)
                    .getContent());

            return prodn3Repository.findAllByProdn2In(n2sByProdn1, pageable);
        }

        if (prodn2Id != null) {
            // The MAX_VALUE for page pageSize since we use this in an autocomplete component. Rethink design otherwise...
            Pageable pageable = new PageRequest(page == null ? 0 : page, Integer.MAX_VALUE, new Sort(order));

            Prodn2 prodn2Reference = prodn2Repository.getOne(prodn2Id);

            return prodn3Repository.findAllByProdn2Equals(prodn2Reference, pageable);
        } else {
            Pageable pageable = new PageRequest(page == null ? 0 : page, pageSize, new Sort(order));

            return prodn3Repository.findAll(pageable);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Prodn3 getProdn3(@PathVariable(value = "id", required = true) Integer id) {
        return prodn3Repository.findProdn3ByIdEquals(id);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    // todo secure
    public ResponseEntity<Prodn3> saveProdn3(@RequestBody Prodn3 prodn3) {

        if (prodn3.getId() == null) {
            // New entity.
            prodn3.setId(Math.abs(new Random().nextInt()));
        }

        prodn3.setSsmaTimestamp(new Byte[]{0x00}); // todo What to do with these?

        return ResponseEntity.ok(prodn3Repository.save(prodn3));
    }
}
