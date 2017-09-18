package se.vgregion.arbetsplatskoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.repository.DataRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author clalu4
 */
@Service
@Transactional
public class StralforsExportFileService {

    @Autowired
    private DataRepository dataRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(StralforsExportFileService.class);

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

    @Scheduled(cron = "0 15/45 * * * MON-FRI")
    @Transactional
    public void runFileTransfer() {
        if (!exportShouldRun) {
            return;
        }
        LOGGER.info(EHalsomyndighetenExportFileService.class.getName() + ".runFileTransfer() starts.");
        String urlAtTheTime = url + (url.endsWith("/") ? "" : "/") + "stralfors.export.txt";
        SambaFileClient.putFile(
            urlAtTheTime,
            generate(),
            userDomain,
            user,
            password
        );
        LOGGER.info(SesamLmnExportFileService.class.getName() + ".runFileTransfer() completes.");
    }

    public String generate() {
        return generate(findAllRelevantItems());
    }

    String generate(List<Data> all) {

    /*		SELECT distinct lankod,arbetsplatskod,benamning,postadress,
    postnr, postort, till_datum, data.leverans, data.fakturering
		FROM        data,ao3
		WHERE       data.ao3 = AO3.AO3id AND
				data.till_datum > #now()# AND
				data.apodos = 0 AND --apodos <> TRUE
				arbetsplatskod <> '999999'*/

        List<String> lines = new ArrayList<>();
        for (Data data : all) {
            lines.add(
                formatRow(
                    data.getLankod(),
                    data.getArbetsplatskod(),
                    data.getBenamning(),
                    data.getPostadress(),
                    data.getPostnr(),
                    data.getPostort(),
                    format(data.getTillDatum()),
                    data.getLeverans(),
                    data.getFakturering()
                )
            );
        }
        return formatLines(lines);
    }

    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

    private String format(Date date) {
        if (date == null) {
            return "";
        }
        return dt.format(date);
    }

    @Transactional
    List<Data> findAllRelevantItems() {
        List<Data> all = dataRepository.findStralforsExportBatch();
        all.sort(Comparator.comparing(Data::getArbetsplatskod));
        return all;
    }

    private String formatRow(Object... parts) {
        List<String> lines = new ArrayList<>();
        for (Object part : parts) lines.add(formatValue(part));
        return String.join(";", lines);
    }

    private String formatLines(List<String> parts) {
        return String.join("\n", parts);
    }

    private String formatValue(Object s) {
        if (s == null) return "";
        return s.toString().replace(';', ',');
    }

}
