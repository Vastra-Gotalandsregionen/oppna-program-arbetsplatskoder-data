package se.vgregion.arbetsplatskoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.DataExport;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkforsesamlmn;
import se.vgregion.arbetsplatskoder.export.repository.DataExportRepository;
import se.vgregion.arbetsplatskoder.export.repository.ViewApkForSesamLmnRepository;
import se.vgregion.arbetsplatskoder.repository.DataRepository;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        Timestamp aYearAgo = Timestamp.from(ZonedDateTime.now(ZoneId.of("Europe/Stockholm")).minusYears(1).toInstant());
        List<Data> datas = dataRepository.findAllTillDatumGreaterOrForTheTimeBeing(aYearAgo);
        for (Data data : datas) {
            viewApkForSesamLmnRepository.save(new Viewapkforsesamlmn(data));
            dataExportRepository.save(new DataExport(data));
        }
        stopWatch.stop();
        LOGGER.info("Finish populateTables()... " + stopWatch.getTotalTimeMillis() + " ms");
        dataExportRepository.flush();
        viewApkForSesamLmnRepository.flush();
    }

}
