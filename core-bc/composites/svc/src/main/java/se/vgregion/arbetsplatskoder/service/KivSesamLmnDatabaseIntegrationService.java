package se.vgregion.arbetsplatskoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.ViewapkHsaid;
import se.vgregion.arbetsplatskoder.export.repository.ViewapkHsaidRepository;
import se.vgregion.arbetsplatskoder.repository.DataRepository;

import java.util.List;

/**
 * Kiv uses this view to read from:
 * <code>
 * CREATE VIEW [dbo].[ViewAPK_HSAID]
 * AS
 * SELECT arbetsplatskodlan, HSAID, from_datum, till_datum
 * FROM  dbo.data
 * </code>
 * This class should create data corresponding to that view.
 *
 * @author Patrik Björk
 */
@Service
@Transactional
public class KivSesamLmnDatabaseIntegrationService {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private ViewapkHsaidRepository viewapkHsaidRepository; // To put the results in.

    private static final Logger LOGGER = LoggerFactory.getLogger(KivSesamLmnDatabaseIntegrationService.class);

    @Transactional
    public void run() {
        LOGGER.info("Starts KivDatabaseIntegrationService.run().");
        List<Data> datas = dataRepository.findAll();
        viewapkHsaidRepository.deleteAll();

        for (Data data : datas) {
            ViewapkHsaid exportItem = new ViewapkHsaid();
            exportItem.setHsaid(data.getHsaid());
            exportItem.setArbetsplatskodlan(data.getArbetsplatskodlan());
            exportItem.setFromDatum(data.getFromDatum());
            exportItem.setTillDatum(data.getTillDatum());
            viewapkHsaidRepository.save(exportItem);
        }

        LOGGER.info("Finish KivDatabaseIntegrationService.run().");
    }

}
