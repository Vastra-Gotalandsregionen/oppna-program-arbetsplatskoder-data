package se.vgregion.arbetsplatskoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkforsesamlmn;
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
    private Ao3Repository ao3Repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(SesamLmnDatabaseIntegrationService.class);

    public void populateTable() {

        LOGGER.info("Start populateTable()...");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        viewApkForSesamLmnRepository.deleteAll();

        stopWatch.stop();
        LOGGER.info("Deleted all Viewapkforsesamlmn: " + stopWatch.getTotalTimeMillis() + " ms");
        stopWatch.start();

        /*Map<String, Ao3> ao3Map = ao3Repository.findAll().stream()
            .collect(Collectors.toMap(Ao3::getAo3id, ao3 -> ao3));*/


        Timestamp thanThat = new Timestamp(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365));
        List<Data> datas = dataRepository.findAllTillDatumGreater(thanThat);

        for (Data data : datas) {
            Viewapkforsesamlmn entry = new Viewapkforsesamlmn();
            // 'c' down in this block shows fields that are actually used by kiv SesamLmn.
            // Should we remove the other ones?
            entry.setHsaid(data.getHsaid()); // c
            entry.setId(Long.valueOf(data.getId()));
            entry.setAo3(data.getAo3()); // c
            entry.setAnsvar(data.getAnsvar()); // c
            entry.setFrivilligUppgift(data.getFrivilligUppgift()); // c
            entry.setAgarform(data.getAgarform()); // c
            entry.setVardform(data.getVardform());
            entry.setVerksamhet(data.getVerksamhet());
            entry.setSorteringskodProd(null); // todo Probably don't need this?
            entry.setSorteringskodBest(null); // todo Probably don't need this?
            entry.setBenamning(data.getBenamning()); // c
            entry.setPostnr(data.getPostnr());
            entry.setPostort(data.getPostort());
            entry.setPostadress(data.getPostadress());
            entry.setLakemedkomm(null); // todo Probably don't need this? Or fixed value?
            entry.setKontaktAkod(null); // todo Use this?
            entry.setLeverans(null); // todo Probably don't need this?
            entry.setFakturering(data.getFakturering());
            entry.setAnmarkning(data.getAnmarkning());
            entry.setFromDatum(data.getFromDatum());
            entry.setTillDatum(data.getTillDatum());
            entry.setRegDatum(data.getRegDatum());
            entry.setArbetsplatskodlan(data.getArbetsplatskodlan()); // c
            entry.setApodos(false); // todo Probably don't need this?
            entry.setExternfaktura(data.getExternfaktura());
            entry.setExternfakturamodell(data.getExternfakturamodell());
            entry.setVgpv(data.getVgpv()); // c
            viewApkForSesamLmnRepository.save(entry);
        }

        stopWatch.stop();
        LOGGER.info("Finish populateTable()... " + stopWatch.getTotalTimeMillis() + " ms");
    }

}
