package se.vgregion.arbetsplatskoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Fakturering;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Leverans;
import se.vgregion.arbetsplatskoder.repository.DataRepository;
import se.vgregion.arbetsplatskoder.repository.FaktureringRepository;
import se.vgregion.arbetsplatskoder.repository.LeveransRepository;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author clalu4
 */
@Service
@Transactional
public class StralforsExportFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StralforsExportFileService.class);

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private LeveransRepository leveransRepository;

    @Autowired
    private FaktureringRepository faktureringRepository;

    private Map<Integer, Leverans> leveranses;

    private Map<Integer, Fakturering> fakturerings;

    @Value("${export.stralfors.should.run}")
    private Boolean exportShouldRun;

    @Value("${export.stralfors.smb.url}")
    private String url;

    @Value("${export.stralfors.smb.user}")
    private String user;

    @Value("${export.stralfors.smb.user.domain}")
    private String userDomain;

    @Value("${export.stralfors.smb.password}")
    private String password;

    @Transactional
    public String runFileTransfer() {
        if (!exportShouldRun) {
            LOGGER.info("Skipping runFileTransfer()...");
            return "This instance is not set to generate files!";
        }
        LOGGER.info("runFileTransfer() starts.");
        String urlAtTheTime = url + (url.endsWith("/") ? "" : "/") + getTodaysFileName();
        String result = generate();

        SambaFileClient.createPath(
            url,
            userDomain,
            user,
            password
        );

        SambaFileClient.putFile(
            urlAtTheTime,
            result,
            userDomain,
            user,
            password
        );
        LOGGER.info("runFileTransfer() completes.");
        return result;
    }

    static String getTodaysFileName(Date now) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return "utdata_nassjo_" + sdf.format(now) + ".txt";
    }

    static String getTodaysFileName() {
        return getTodaysFileName(new Date());
    }

    public String generate() {
        return generate(findAllRelevantItems());
    }

    @Transactional
    private String generate(List<Data> items) {
        List<String> rows = new ArrayList<>();
        for (Data item : items) {
            rows.add(formatRow(
                item.getArbetsplatskodlan(),
                item.getBenamning(),
                item.getPostadress(),
                item.getPostnr(),
                item.getPostort(),
                getLeveransText(item),
                getFaktureringKortText(item)
            ));
        }
        Collections.sort(rows);
        return formatLines(rows);
    }

    @Transactional
    public List<Data> findAllRelevantItems() {
        List<Data> all = dataRepository.findStralforsExportBatch();
        return all;
    }

    private String formatRow(Object... parts) {
        List<String> lines = new ArrayList<>();
        for (Object part : parts) lines.add(formatValue(part));
        return String.join(";", lines);
    }

    private String formatLines(List<String> parts) {
        return String.join("\r\n", parts);
    }

    private String formatValue(Object s) {
        if (s == null) {
            return "";
        }
        return s.toString().replace(';', ',');
    }

    private String getLeveransText(Data forThisOne) {
        if (leveranses == null) {
            leveranses = new HashMap<>();
            for (Leverans leverans : leveransRepository.findAll()) {
                leveranses.put(leverans.getId(), leverans);
            }
        }
        if (forThisOne.getLeverans() == null || !leveranses.containsKey(forThisOne.getLeverans())) {
            return "Mölndal.";
        }
        return leveranses.get(forThisOne.getLeverans()).getLeveranstext();
    }

    private String getFaktureringKortText(Data forThisOne) {
        if (fakturerings == null) {
            fakturerings = new HashMap<Integer, Fakturering>();
            for (Fakturering fakturering : faktureringRepository.findAll()) {
                fakturerings.put(fakturering.getId(), fakturering);
            }
        }
        if (forThisOne.getFakturering() == null || !fakturerings.containsKey(forThisOne.getFakturering())) {
            return "saknas";
        }
        return fakturerings.get(forThisOne.getFakturering()).getFaktureringKortText();
    }

}
