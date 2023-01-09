package se.vgregion.arbetsplatskoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import se.vgregion.arbetsplatskoder.domain.jpa.Link;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.*;
import se.vgregion.arbetsplatskoder.repository.*;
import se.vgregion.arbetsplatskoder.util.ExcelUtil;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Patrik Björk
 */
@Service
@Transactional
public class BatchService {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private DataOperations dataOperations;

    @Autowired
    private Prodn1Repository prodn1Repository;

    @Autowired
    private Prodn2Repository prodn2Repository;

    @Autowired
    private Prodn3Repository prodn3Repository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private Ao3Repository ao3Repository;

    @Autowired
    private UnitSearchService unitSearchService;

    @Autowired
    private DataSource dataSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchService.class);

    @Scheduled(cron = "0 37 5 * * *")
    @Transactional
    public void processMissingHsaids() {
        List<Data> all = dataRepository.findAll();

        for (Data data : all) {
            if (!StringUtils.isEmpty(data.getHsaid()) && data.getHsaid().startsWith("SE")) {
                Boolean missingInKiv = unitSearchService.searchUnits(data.getHsaid()).size() == 0;

                if (!missingInKiv.equals(data.getHsaidMissingInKiv())) {
                    data.setHsaidMissingInKiv(missingInKiv);
                    dataRepository.save(data);
                    LOGGER.info("Saved data: " + data.getId() + " - " + data.getBenamning());
                }
            }
        }
    }

    @Transactional
    public void init() {

        initQuartzTables();
        initLinks();

        try {
            processDbSchemaChanges();
        } catch (Exception e) {
            LOGGER.error("processDbSchemaChanges() failed: " + e.getMessage());
        }

        try {
            migrateDataAccordingToNewOrganization();
        } catch (Exception e) {
            LOGGER.error("migrateDataAccordingToNewOrganization() failed: " + e.getMessage());
        }

        List<Data> allDatas = dataRepository.findAll();

        processErsattav(allDatas);

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

            if (prodn2 != null && prodn3.getProdn2() == null) {
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

            if (prodn1 != null && prodn2.getProdn1() == null) {
                prodn2.setProdn1(prodn1);
                prodn2Repository.save(prodn2);
            }
        }

        for (Data data : allDatas) {

            // We don't want to touch those where prodn3 (and prodn1, but that's taken for granted if prodn3 is set) is
            // set. Only newly migrated entities are regarded.
            if (data.getProdn3() != null) {
                continue;
            }

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

            Prodn2 prodn2;
            if (n2 != null) {
                prodn2 = prodn2Map.get(n2);
            } else {
                prodn2 = prodn3.getProdn2();
            }

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

    private void migrateDataAccordingToNewOrganization() {
        List<Map<String, String>> keyValuesList = ExcelUtil.readRows();

        for (Map<String, String> keyValues : keyValuesList) {
            String s = keyValues.get("Arbetsplatskod");

            List<Data> allByArbetsplatskodlanEquals = dataRepository.findAllByArbetsplatskodlanEquals(s);

            if (allByArbetsplatskodlanEquals.size() != 1) {
                throw new IllegalStateException(s + " has " + allByArbetsplatskodlanEquals.size() + " number of entries.");
            }

            Data data = allByArbetsplatskodlanEquals.get(0);

            // Motpart / Ao3
            String newAo3 = keyValues.get("Ny Motpart");

            boolean anyChange = false;

            if (StringUtils.hasText(newAo3)) {
                if (newAo3.contains(",")) {
                    String[] split = newAo3.split(",");
                    String newAo3Code = split[0];

                    Ao3 byAo3id = ao3Repository.findByAo3id(newAo3Code);
                    if (byAo3id == null) {
                        Ao3 newAo3Entity = new Ao3();
                        newAo3Entity.setId(Math.abs(new Random().nextInt()));
                        newAo3Entity.setAo3id(newAo3Code);
                        newAo3Entity.setForetagsnamn(split[1].trim());
                        newAo3Entity.setRaderad(false);

                        ao3Repository.save(newAo3Entity);
                    }

                    if (!newAo3Code.equals(data.getAo3())) {
                        LOGGER.info("Updating " + data.getBenamning() + ": Motpart " + data.getAo3() + " -> " + newAo3Code);
                        data.setAo3(newAo3Code);
                        anyChange = true;
                    }
                } else {
                    throw new IllegalStateException("Unexpected format of Ao3: " + newAo3 + ". Expected \",\".");
                }
            }

            // Ansvar
            String newAnsvar = keyValues.get("Nytt Ansvar");

            if (StringUtils.hasText(newAnsvar) && !newAnsvar.equals(data.getAnsvar())) {
                LOGGER.info("Updating " + data.getBenamning() + ": Ansvar " + data.getAnsvar() + " -> " + newAnsvar);
                data.setAnsvar(newAnsvar);
                anyChange = true;
            }

            if (anyChange) {
                Timestamp now = Timestamp.from(Instant.now());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                sdf.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));

                data.setAndringsdatum(sdf.format(now));

                data.setUserIdNew("system");
                dataOperations.saveAndArchive(data);
            }
        }
    }

    void processErsattav(List<Data> allDatas) {
        Map<String, Data> dataByArbetsplataskod = allDatas.stream()
                .collect(Collectors.toMap(Data::getArbetsplatskod, data -> data));

        // Remove all ersattav where ersattav equals arbetsplatskod and change ersattav to arbetsplatskodlan
        for (Data data : allDatas) {
            if (data.getArbetsplatskod().equals(data.getErsattav())) {
                data.setErsattav(null);
                dataRepository.save(data);
            }

            if (data.getErsattav() != null && !data.getErsattav().startsWith("14")) {
                if (dataByArbetsplataskod.containsKey(data.getErsattav())) {
                    // We found the data which has replaced the for loop data. Set arbetsplatskodlan instead
                    data.setErsattav(dataByArbetsplataskod.get(data.getErsattav()).getArbetsplatskodlan());
                }
            }
        }
    }

    void processDbSchemaChanges() {
        try {
            ResultSet resultSet = dataSource.getConnection().createStatement().executeQuery("select * from data");
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            boolean found = false;
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                if (columnName.endsWith("_old")) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                ClassPathResource classPathResource = new ClassPathResource("clone-columns-to-legacy.sql");

                ResourceDatabasePopulator databasePopulator =
                        new ResourceDatabasePopulator(true, false, "UTF-8", classPathResource);

                databasePopulator.execute(dataSource);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void initQuartzTables() {
        ClassPathResource classPathResource = new ClassPathResource("init-quartz.sql");
        ResourceDatabasePopulator databasePopulator =
                new ResourceDatabasePopulator(false, false, "UTF-8", classPathResource);

        databasePopulator.execute(dataSource);
    }

    private void initLinks() {
        List<Link> allLinks = linkRepository.findAll();

        if (allLinks.size() == 0) {
            Link link1 = new Link(1, "Användarmanual", "");
            Link link2 = new Link(2, "Regler & Rutiner", "");
            Link link3 = new Link(3, "Kontaktpersoner", "");

            linkRepository.save(link1);
            linkRepository.save(link2);
            linkRepository.save(link3);
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
