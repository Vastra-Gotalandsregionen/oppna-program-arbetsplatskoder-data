package se.vgregion.arbetsplatskoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn2;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn3;
import se.vgregion.arbetsplatskoder.repository.DataRepository;
import se.vgregion.arbetsplatskoder.repository.Prodn1Repository;
import se.vgregion.arbetsplatskoder.repository.Prodn2Repository;
import se.vgregion.arbetsplatskoder.repository.Prodn3Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Patrik Björk
 */
@Service
@Transactional
public class BatchService {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private Prodn1Repository prodn1Repository;

    @Autowired
    private Prodn2Repository prodn2Repository;

    @Autowired
    private Prodn3Repository prodn3Repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchService.class);

    @PostConstruct
    public void init() {

        List<Data> allDatas = dataRepository.findAllByProdn1IsNull();

        List<Prodn3> allProdn3s = prodn3Repository.findAll();
        List<Prodn2> allProdn2s = prodn2Repository.findAll();
        List<Prodn1> allProdn1s = prodn1Repository.findAll();

        Map<String, Prodn3> prodn3Map = new HashMap<>();
        Map<String, Prodn2> prodn2Map = new HashMap<>();
        Map<String, Prodn1> prodn1Map = new HashMap<>();

        allProdn3s.forEach(prodn3 -> prodn3Map.put(prodn3.getProducentid(), prodn3));
        allProdn2s.forEach(prodn2 -> prodn2Map.put(prodn2.getProducentid(), prodn2));
        allProdn1s.forEach(prodn1 -> prodn1Map.put(prodn1.getProducentid(), prodn1));

        for (Data data : allDatas) {
            if (data.getProdn1() != null) {
                continue;
            }

            String sorteringskodProd = data.getSorteringskodProd();

            LOGGER.info("Processing data " + data.getId() + " with sorteringskodProd=" + sorteringskodProd);

            Prodn3 prodn3 = prodn3Map.get(sorteringskodProd);

            if (prodn3 == null) {
                LOGGER.warn("Prodn3 " + sorteringskodProd + " doesn't exist.");

                continue;
            }

            String n2 = prodn3.getN2();

            Prodn2 prodn2 = prodn2Map.get(n2);

            if (prodn2 == null) {
                LOGGER.warn("Prodn2 " + n2 + " doesn't exist.");

                continue;
            }

            String n1 = prodn2.getN1();

            Prodn1 prodn1 = prodn1Map.get(n1);

            if (prodn1 == null) {
                LOGGER.warn("Prodn1 " + n1 + " doesn't exist.");

                continue;
            }


            data.setProdn1(prodn1);

            dataRepository.save(data);
        }
    }

}
