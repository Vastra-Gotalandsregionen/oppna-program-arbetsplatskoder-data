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

    @Autowired
    private Ao3Repository ao3Repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(LokeDatabaseIntegrationService.class);

    public void populateLokeTable() {

        LOGGER.info("Start populateLokeTable()...");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        viewapkwithao3Repository.deleteAll();

        stopWatch.stop();
        LOGGER.info("Deleted all Viewapkwithao3: " + stopWatch.getTotalTimeMillis() + " ms");
        stopWatch.start();

        Map<String, Ao3> ao3Map = ao3Repository.findAll().stream()
            .collect(Collectors.toMap(Ao3::getAo3id, ao3 -> ao3));

        List<Data> datas = dataRepository.findAll();

        for (Data data : datas) {
            String ao3 = data.getAo3();
            Ao3 ao3Entity = ao3Map.get(ao3);

            if (ao3Entity == null || ao3Entity.getId() == null) {
                LOGGER.warn("Data entity " + data.getId() + " with ao3=" + ao3 + " doesn't correspond to any Ao3 entity.");
                continue;
            }

            Viewapkwithao3 entry = new Viewapkwithao3();

            entry.setId(data.getId());
            entry.setLankod(data.getLankod());
            entry.setArbetsplatskod(data.getArbetsplatskod());
            entry.setAo3(ao3);
            entry.setAnsvar(data.getAnsvar());
            entry.setFrivilligUppgift(data.getFrivilligUppgift());
            entry.setAgarform(data.getAgarform());
            entry.setVardform(data.getVardform());
            entry.setVerksamhet(data.getVerksamhet());
            entry.setSorteringskodProd(null); // todo Probably don't need this?
            entry.setSorteringskodBest(null); // todo Probably don't need this?
            entry.setBenamning(data.getBenamning());
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
            entry.setErsattav(data.getErsattav());
            entry.setUserId(data.getUserId()); // data.getUserIdNew() != null ? data.getUserIdNew() : data.getUserId() + "");
            entry.setArbetsplatskodlan(data.getArbetsplatskodlan());
            entry.setNamn(null); // todo Probably don't need this?
            entry.setAndringsdatum(data.getAndringsdatum());
            entry.setKommun(null); // todo Probably don't need this?
            entry.setApodos(false); // todo Probably don't need this?
            entry.setExternfaktura(data.getExternfaktura());
            entry.setKommunkod(null); // todo Probably don't need this?
            entry.setExternfakturamodell(data.getExternfakturamodell());
            entry.setVgpv(data.getVgpv());
            entry.setExpr1(ao3Entity.getId());
            entry.setAo3id(ao3Entity.getAo3id());
            entry.setForetagsnamn(ao3Entity.getForetagsnamn());
            entry.setProducent(null); // todo Probably don't need this?
            entry.setKontaktperson(ao3Entity.getKontaktperson());
            entry.setForetagsnr(ao3Entity.getForetagsnr());
            entry.setRaderad(ao3Entity.getRaderad());

            viewapkwithao3Repository.persist(entry);
        }

        stopWatch.stop();
        LOGGER.info("Finish populateLokeTable()... " + stopWatch.getTotalTimeMillis() + " ms");
    }

}
