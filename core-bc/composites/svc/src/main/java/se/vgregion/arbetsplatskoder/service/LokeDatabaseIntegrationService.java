package se.vgregion.arbetsplatskoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Ao3;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkwithao3;
import se.vgregion.arbetsplatskoder.export.repository.Ao3ExportRepository;
import se.vgregion.arbetsplatskoder.export.repository.Viewapkwithao3Repository;
import se.vgregion.arbetsplatskoder.repository.Ao3Repository;
import se.vgregion.arbetsplatskoder.repository.DataRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Patrik Björk
 */
@Service
@Transactional(transactionManager = "exportTransactionManager")
public class LokeDatabaseIntegrationService {

    @Autowired
    private Viewapkwithao3Repository viewapkwithao3Repository;

    @Autowired
    private DataRepository dataRepository;

    @Autowired private Ao3Repository ao3Repository;

    @Autowired
    private Ao3ExportRepository ao3ExportRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(LokeDatabaseIntegrationService.class);

    public void populateLokeTable() {

        LOGGER.info("Start populateLokeTable()...");

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        viewapkwithao3Repository.deleteAll();
        stopWatch.stop();
        LOGGER.info("Deleted all Viewapkwithao3: " + stopWatch.getTotalTimeMillis() + " ms");

        stopWatch.start();
        ao3ExportRepository.deleteAll();
        stopWatch.stop();
        LOGGER.info("Deleted all ao3 in export repository: " + stopWatch.getTotalTimeMillis() + " ms");

        Map<String, Ao3> ao3Map = ao3Repository.findAll().stream()
            .collect(Collectors.toMap(Ao3::getAo3id, ao3 -> ao3));

        stopWatch.start();
        ao3ExportRepository.save(ao3Map.values());
        stopWatch.stop();
        LOGGER.info("Copied all ao3 from main db into export db: " + stopWatch.getTotalTimeMillis() + " ms");

        stopWatch.start();
        List<Data> datas = dataRepository.findAll();

        for (Data data : datas) {
            String ao3 = data.getAo3();
            Ao3 ao3Entity = ao3Map.get(ao3);

            if (ao3Entity == null || ao3Entity.getId() == null) {
                LOGGER.warn("Data entity " + data.getId() + " with ao3=" + ao3 + " doesn't correspond to any Ao3 entity.");
                continue;
            }

            Viewapkwithao3 entry = new Viewapkwithao3().loadData(data).loadData(ao3Entity);

            viewapkwithao3Repository.persist(entry);
        }

        stopWatch.stop();
        LOGGER.info("Finish populateLokeTable()... " + stopWatch.getTotalTimeMillis() + " ms");
    }

}
