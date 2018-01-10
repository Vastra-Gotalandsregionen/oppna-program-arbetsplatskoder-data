package se.vgregion.arbetsplatskoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.ViewapkHsaid;
import se.vgregion.arbetsplatskoder.export.repository.ViewapkHsaidRepository;
import se.vgregion.arbetsplatskoder.repository.DataRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
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
@Transactional(transactionManager = "exportTransactionManager")
public class KivSesamLmnDatabaseIntegrationService {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private ViewapkHsaidRepository viewapkHsaidRepository; // To put the results in.

    private static final Logger LOGGER = LoggerFactory.getLogger(KivSesamLmnDatabaseIntegrationService.class);

    public void run() {
        LOGGER.info("Starts KivDatabaseIntegrationService.run().");
        removeOldExportAgain(itemsToExport());
        LOGGER.info("Finish KivDatabaseIntegrationService.run().");
    }

    public List<ViewapkHsaid> itemsToExport() {
        Timestamp thanThat = new Timestamp(System.currentTimeMillis());
        List<Data> datas = dataRepository.findAllTillDatumGreaterOrForTheTimeBeing(thanThat);
        //List<Data> datas = dataRepository.findAll();

        List<ViewapkHsaid> items = new ArrayList<>();

        for (Data data : datas) {
            ViewapkHsaid exportItem = new ViewapkHsaid(data);
            items.add(exportItem);
        }
        return items;
    }

    public void removeOldExportAgain(List<ViewapkHsaid> those) {
        viewapkHsaidRepository.deleteAll();
        viewapkHsaidRepository.flush();
        for (ViewapkHsaid that : those) {
            viewapkHsaidRepository.saveAndFlush(that);
        }
    }

}
