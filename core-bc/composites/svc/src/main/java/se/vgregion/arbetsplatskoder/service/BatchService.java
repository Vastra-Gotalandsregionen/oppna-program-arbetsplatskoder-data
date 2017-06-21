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

    @Transactional
    public void init() {

        List<Data> allDatas = dataRepository.findAll();

        // Remove all ersattav where ersattav equals arbetsplatskod
        for (Data data : allDatas) {
            if (data.getArbetsplatskod().equals(data.getErsattav())) {
                data.setErsattav(null);
                dataRepository.save(data);
            }
        }
//        List<Data> allDatas = dataRepository.findAllByProdn1IsNull();

        List<Prodn3> allProdn3s = prodn3Repository.findAll();
        List<Prodn2> allProdn2s = prodn2Repository.findAll();
        List<Prodn1> allProdn1s = prodn1Repository.findAll();

        Map<String, Prodn3> prodn3Map = new HashMap<>();
        Map<String, Prodn2> prodn2Map = new HashMap<>();
        Map<String, Prodn1> prodn1Map = new HashMap<>();

        trimProdn3sProducentid(allProdn3s);

        allProdn3s.stream().filter(prodn3 -> prodn3.getProducentid() != null).forEach(prodn3 -> prodn3Map.put(prodn3.getProducentid().toLowerCase(), prodn3));
        allProdn2s.stream().filter(prodn2 -> prodn2.getProducentid() != null).forEach(prodn2 -> prodn2Map.put(prodn2.getProducentid().toLowerCase(), prodn2));
        allProdn1s.stream().filter(prodn1 -> prodn1.getProducentid() != null).forEach(prodn1 -> prodn1Map.put(prodn1.getProducentid().toLowerCase(), prodn1));

        // Set prodn2 for all prodn3s
        for (Prodn3 prodn3 : allProdn3s) {
            if (prodn3.getN2() == null) {
                continue;
            }
            Prodn2 prodn2 = prodn2Map.get(prodn3.getN2().toLowerCase());

            if (prodn2 != null && (prodn3.getProdn2() == null || !prodn3.getProdn2().getId().equals(prodn2.getId()))) {
                prodn3.setProdn2(prodn2);
                prodn3Repository.save(prodn3);
            }
        }

        // Set prodn1 for all prodn2s
        for (Prodn2 prodn2 : allProdn2s) {
            if (prodn2.getN1() == null) {
                LOGGER.warn("prodn2: " + prodn2.getId() + " doesn't have any N1.");
                continue;
            }
            Prodn1 prodn1 = prodn1Map.get(prodn2.getN1().toLowerCase());

            if (prodn1 != null && (prodn2.getProdn1() == null || !prodn2.getProdn1().getId().equals(prodn2.getId()))) {
                prodn2.setProdn1(prodn1);
                prodn2Repository.save(prodn2);
            }
        }

        for (Data data : allDatas) {
//            if (data.getProdn1() != null) {
//                continue;
//            }

            String sorteringskodProd = data.getSorteringskodProd();

            if (sorteringskodProd == null) {
                LOGGER.info("No sorteringskodprod data: " + data.getId());
                continue;
            } else if (!sorteringskodProd.equals(sorteringskodProd.trim())) {
                data.setSorteringskodProd(sorteringskodProd.trim());
                dataRepository.save(data);
            }

//            LOGGER.info("Processing data " + data.getId() + " with sorteringskodProd=" + sorteringskodProd);

            Prodn3 prodn3 = prodn3Map.get(sorteringskodProd);

            if (prodn3 == null) {
                prodn3 = prodn3Map.get(sorteringskodProd.toLowerCase().trim());

                if (prodn3 != null) {
                    // We just needed to lowercase... Fix...
                    data.setSorteringskodProd(sorteringskodProd.toLowerCase().trim());

                    // Set this too.
                    data.setProdn3(prodn3);

                    dataRepository.save(data);
                } else {

                    Prodn2 prodn2 = prodn2Map.get(sorteringskodProd);

                    // Try finding a prodn1 with the sorteringskodProd or via a prodn2...
                    Prodn1 prodn1;

                    if (prodn2 != null) {
                        prodn1 = prodn2.getProdn1();
                    } else {
                        prodn1 = prodn1Map.get(sorteringskodProd);
                    }

                    if (prodn1 != null) {
                        // At least we got a match for prodn1.
                        if (data.getProdn1() == null || !data.getProdn1().getProducentid().equals(prodn1.getProducentid())) {
                            data.setProdn1(prodn1);
                            dataRepository.save(data);
                            LOGGER.warn("Prodn3 " + sorteringskodProd + " doesn't exist, but at least we got a match for prodn1.");
                        }
                    } else {
                        LOGGER.warn("Prodn3 " + sorteringskodProd + " doesn't exist, nor did prodn2 or prodn1.");
                    }

                    continue;
                }
            } else {
                if (data.getProdn3() == null || !data.getProdn3().getProducentid().equals(prodn3.getProducentid())) {
                    data.setProdn3(prodn3);
                    dataRepository.save(data);
                }
            }

            String n2 = prodn3.getN2();

            Prodn2 prodn2 = prodn2Map.get(n2);

            if (prodn2 == null) {

                prodn2 = prodn2Map.get(n2.toLowerCase().trim());

                if (prodn2 != null) {
                    prodn3.setN2(n2.toLowerCase().trim());

                    prodn3Repository.save(prodn3);
                } else {
                    LOGGER.warn("Prodn2 " + n2 + " doesn't exist.");

                    continue;
                }
            }

            String n1 = prodn2.getN1();

            Prodn1 prodn1 = prodn1Map.get(n1);

            if (prodn1 == null) {

                if (n1 != null) {
                    prodn1 = prodn1Map.get(n1.toLowerCase().trim());

                    if (prodn1 != null) {
                        prodn2.setN1(n1.toLowerCase().trim());

                        prodn2Repository.save(prodn2);
                    } else {
                        LOGGER.warn("Prodn1 " + n1 + " doesn't exist.");

                        continue;
                    }
                } else {
                    continue;
                }
            }

            if (data.getProdn1() == null || !data.getProdn1().getProducentid().equals(prodn1.getProducentid())) {
                data.setProdn1(prodn1);
                dataRepository.save(data);
            }
        }
    }

    private void trimProdn3sProducentid(List<Prodn3> allProdn3s) {
        allProdn3s.stream()
                .filter(prodn3 -> prodn3.getProducentid() != null)
                .forEach(prodn3 -> {
                    if (!prodn3.getProducentid().equals(prodn3.getProducentid().trim())) {
                        prodn3.setProducentid(prodn3.getProducentid().trim());
                        prodn3Repository.save(prodn3);
                    }
                });
    }

}
