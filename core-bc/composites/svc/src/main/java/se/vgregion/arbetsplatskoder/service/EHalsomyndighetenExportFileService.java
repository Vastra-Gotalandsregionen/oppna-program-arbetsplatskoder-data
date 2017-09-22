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
import se.vgregion.arbetsplatskoder.repository.extension.DataExtendedRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author clalu4
 */
@Service
@Transactional
public class EHalsomyndighetenExportFileService {

    @Autowired
    DataRepository dataRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(EHalsomyndighetenExportFileService.class);

    @Value("${export.ehalsomyndigheten.should.run}")
    private Boolean exportShouldRun;

    @Value("${export.ehalsomyndigheten.smb.url}")
    private String url;

    @Value("${export.ehalsomyndigheten.smb.user}")
    private String user;

    @Value("${export.ehalsomyndigheten.smb.user.domain}")
    private String userDomain;

    @Value("${export.ehalsomyndigheten.smb.password}")
    private String password;

    public static String getTodaysFileName(Date now) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYMMdd");
        return sdf.format(now) + "_14_arbetsplatser.txt";
    }

    public static String getTodaysFileName() {
        return getTodaysFileName(new Date());
    }

    @Scheduled(cron = "0 15/45 * * * MON-FRI")
    @Transactional
    public void runFileTransfer() {
        if (!exportShouldRun) {
            return;
        }
        LOGGER.info(EHalsomyndighetenExportFileService.class.getName() + ".runFileTransfer() starts.");
        String urlAtTheTime = url + (url.endsWith("/") ? "" : "/") + getTodaysFileName();
        SambaFileClient.putFile(
            urlAtTheTime,
            generate(),
            userDomain,
            user,
            password
        );
        LOGGER.info(EHalsomyndighetenExportFileService.class.getName() + ".runFileTransfer() completes.");
    }

    public String generate() {
        String result = generate(findAllRelevantItems()) + "\n";
        return result;
    }

    private List<Data> findAllRelevantItems() {
        return dataRepository.findEhalsomyndighetensExportBatch();
    }

    static String trim(String thatText, int toMaximumLength) {
        if (thatText.length() > toMaximumLength) {
            return thatText.substring(0, toMaximumLength);
        } else {
            return thatText;
        }
    }

    static String trim(Object thatObject, int toMaximumLength) {
        return trim(thatObject.toString(), toMaximumLength);
    }

    String generate(List<Data> items) {
        List<String> lines = new ArrayList<>();
        for (Data item : items) {
            lines.add(
                formatRow(
                    item.getArbetsplatskodlan()
                    , trim(item.getBenamning(), 100)
                    , ""
                    , item.getAgarform()
                    , item.getVardform()
                    , item.getVerksamhet()
                    , formatFranDatum(item.getFromDatum(), (Date) item.getTillDatum())
                    , formatTillDatum(item.getTillDatum())
                    , trim(item.getPostadress(), 35)
                    , item.getPostnr()
                    , trim(item.getPostort(), 25)
                    , ""
                    , ""
                    , ""
                    , item.getLakemedkomm()
                    , ""
                    , ""
                    , ""
                    , ""
                )
            );
        }
        return formatLines(lines);
    }

    static DateTimeFormatter yyyyMmDd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static String formatTillDatum(String dateAsTextYyyyMmDd) {
        if (dateAsTextYyyyMmDd == null || dateAsTextYyyyMmDd.isEmpty()) {
            return "2000-01-01";
        }
        if (dateAsTextYyyyMmDd.startsWith("2199-")) {
            return "";
        }
        return inc(dateAsTextYyyyMmDd, 1);
    }

    static String formatTillDatum(Date toDate) {
        if (toDate == null) {
            return "2000-01-01";
        }
        LocalDateTime ld = toLocalDateTime(toDate);
        if (ld.getYear() == 2199) {
            return "";
        }
        return inc(toDate, 1);
    }

    static String inc(String textualDate, int withNumberOfDays) {
        LocalDate date = LocalDate.parse(textualDate, yyyyMmDd);
        LocalDate result = date.plusDays(withNumberOfDays);
        return yyyyMmDd.format(result);
    }

    static String inc(Date that, int withNumberOfDays) {
        LocalDate date = toLocalDateTime(that).toLocalDate();
        LocalDate result = date.plusDays(withNumberOfDays);
        return yyyyMmDd.format(result);
    }

    static LocalDateTime toLocalDateTime(Date fromThat) {
        LocalDateTime result = LocalDateTime.ofInstant(fromThat.toInstant(), TimeZone.getDefault().toZoneId());
        return result;
    }

    static String formatFranDatum(Date fromDate, Date toDate) {
        if (fromDate == null) {
            return "";
        }
        LocalDateTime ldFromDate = toLocalDateTime(fromDate);
        if (ldFromDate.getYear() == 1900) {
            return "";
        }

        if (toDate != null) {
            LocalDateTime ldToDate = toLocalDateTime(toDate);
            if (ldFromDate.getYear() == ldToDate.getYear() &&
                ldFromDate.getMonth() == ldToDate.getMonth() &&
                ldFromDate.getDayOfMonth() == ldToDate.getDayOfMonth()) {
                return inc(fromDate, -1);
            }
        }
        return inc(fromDate, 0);
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
