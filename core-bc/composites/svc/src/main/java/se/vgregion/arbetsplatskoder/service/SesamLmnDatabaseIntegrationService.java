package se.vgregion.arbetsplatskoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.DataExport;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkforsesamlmn;
import se.vgregion.arbetsplatskoder.export.repository.DataExportRepository;
import se.vgregion.arbetsplatskoder.export.repository.ViewApkForSesamLmnRepository;
import se.vgregion.arbetsplatskoder.repository.Ao3Repository;
import se.vgregion.arbetsplatskoder.repository.DataRepository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author clalu4
 */
@Service
@Transactional(transactionManager = "exportTransactionManager")
public class SesamLmnDatabaseIntegrationService {

    @Autowired
    private ViewApkForSesamLmnRepository viewApkForSesamLmnRepository;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private DataExportRepository dataExportRepository;

    @Autowired
    private Ao3Repository ao3Repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(SesamLmnDatabaseIntegrationService.class);

    public void populateTables() {
        LOGGER.info("Start populateTables()...");
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        viewApkForSesamLmnRepository.deleteAll();
        stopWatch.stop();
        LOGGER.info("Deleted all Viewapkforsesamlmn: " + stopWatch.getTotalTimeMillis() + " ms");

        stopWatch.start();
        dataExportRepository.deleteAll();
        stopWatch.stop();
        LOGGER.info("Deleted all Data in export-db: " + stopWatch.getTotalTimeMillis() + " ms");

        stopWatch.start();
        Timestamp thanThat = new Timestamp(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365));
        List<Data> datas = dataRepository.findAllTillDatumGreaterOrForTheTimeBeing(thanThat);
        for (Data data : datas) {
            viewApkForSesamLmnRepository.save(new Viewapkforsesamlmn(data));
            dataExportRepository.save(new DataExport(data));
        }
        stopWatch.stop();
        LOGGER.info("Finish populateTables()... " + stopWatch.getTotalTimeMillis() + " ms");
    }

}
