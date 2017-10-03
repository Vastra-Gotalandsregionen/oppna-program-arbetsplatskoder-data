package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.AgarformEnum;
import se.vgregion.arbetsplatskoder.domain.ReportType;
import se.vgregion.arbetsplatskoder.domain.jpa.FileBlob;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Ao3;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Vardform;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Verksamhet;
import se.vgregion.arbetsplatskoder.domain.json.Report;
import se.vgregion.arbetsplatskoder.repository.Ao3Repository;
import se.vgregion.arbetsplatskoder.repository.DataRepository;
import se.vgregion.arbetsplatskoder.repository.FileBlobRepository;
import se.vgregion.arbetsplatskoder.repository.VardformRepository;
import se.vgregion.arbetsplatskoder.repository.VerksamhetRepository;
import se.vgregion.arbetsplatskoder.service.HmacUtil;
import se.vgregion.arbetsplatskoder.util.ExcelUtil;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private FileBlobRepository fileBlobRepository;

    @Autowired
    private VardformRepository vardformRepository;

    @Autowired
    private VerksamhetRepository verksamhetRepository;

    @Autowired
    private Ao3Repository ao3Repository;

    private CacheManager cacheManager = CacheManager.getInstance();

    @PostConstruct
    public void init() {
        cacheManager.addCacheIfAbsent(new Cache("vardform", 100, false, false, 10, 10));
        cacheManager.addCacheIfAbsent(new Cache("verksamhet", 100, false, false, 10, 10));
        cacheManager.addCacheIfAbsent(new Cache("ao3", 100, false, false, 10, 10));
    }

    @RequestMapping(value = "/file/{reportType}/{tempFileName}/{hmac}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getGeneratedReport(
            @PathVariable("reportType") ReportType reportType,
            @PathVariable("tempFileName") String fileName,
            @PathVariable("hmac") String hmac,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate
            ) {

        // Verify requested file with hmac
        try {
            String calculatedHmac = HmacUtil.calculateRFC2104HMAC(fileName);

            if (!calculatedHmac.equals(hmac)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (SignatureException | NoSuchAlgorithmException | InvalidKeyException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String OUTPUT_FILENAME_SUFFIX = ".xlsx";
        String OUTPUT_FILENAME_PREFIX;

        String outputFileName;

        switch (reportType) {
            case VALID_WITH_END_DATE:
                OUTPUT_FILENAME_PREFIX = "arbetsplatskoder_giltiga_med_slutdatum_";
                outputFileName = OUTPUT_FILENAME_PREFIX + sdf.format(new Date()) + OUTPUT_FILENAME_SUFFIX;
                break;
            case VALID_WITHOUT_END_DATE:
                OUTPUT_FILENAME_PREFIX = "arbetsplatskoder_giltiga_tills_vidare_";
                outputFileName = OUTPUT_FILENAME_PREFIX + sdf.format(new Date()) + OUTPUT_FILENAME_SUFFIX;
                break;
            case UPDATED_BETWEEN_DATES:
                OUTPUT_FILENAME_PREFIX = "arbetsplatskoder_giltiga_mellan_" + fromDate + "_och_" + toDate;
                outputFileName = OUTPUT_FILENAME_PREFIX + OUTPUT_FILENAME_SUFFIX;
                break;
            case WITH_DELETED_PRODN1:
                OUTPUT_FILENAME_PREFIX = "arbetsplatskoder_med_raderad_concise_sumnivå1_";
                outputFileName = OUTPUT_FILENAME_PREFIX + OUTPUT_FILENAME_SUFFIX;
                break;
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return getReportFile(fileName, outputFileName);
    }

    @RequestMapping(value = "/generate/{reportType}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Report> generateReportFile(
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @PathVariable("reportType") ReportType reportType
    ) {

        List<Data> result;

        switch (reportType) {
            case VALID_WITHOUT_END_DATE:
                result = dataRepository.findAllValidWithoutEndDate();
                break;
            case UPDATED_BETWEEN_DATES:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    sdf.parse(fromDate); // Just to make some validation

                    Date toDateDate = sdf.parse(toDate);
                    Instant nextDay = toDateDate.toInstant().plus(1, ChronoUnit.DAYS);
                    toDateDate = new Date(nextDay.toEpochMilli());
                    toDate = sdf.format(toDateDate);

                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                result = dataRepository.findAllUpdatedBetween(fromDate, toDate);

                break;
            case VALID_WITH_END_DATE:
                result = dataRepository.findAllValidWithEndDate();
                break;
            case WITH_DELETED_PRODN1:
                result = dataRepository.findAllByProdn1Raderad(true);
                break;
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Report report = new Report();

        String FILENAME_PREFIX = "apk_temp_";

        List<String[]> matrix = dataSetToMatrix(result);

        InputStream inputStream = ExcelUtil.exportToStream(matrix);

        String uuid = UUID.randomUUID().toString();

        String fileName = FILENAME_PREFIX + uuid;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            fileBlobRepository.save(new FileBlob(fileName, outputStream.toByteArray()));

            report.setFileName(fileName);
            report.setHmac(HmacUtil.calculateRFC2104HMAC(fileName));

        } catch (IOException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(report);
    }

    List<String[]> dataSetToMatrix(List<Data> result) {
        List<String[]> matrix = new ArrayList<>();

        matrix.add(new String[]{
                "Arbetsplatskod",
                "Ägarform",
                "Motpart",
                "Ansvar",
                "Frivillig uppgift",
                "Faktureras externt",
                "VGPV",
                "Vårdform",
                "Medicinsk verksamhetskod",
                "Concise SumNivå 1",
                "Concise SumNivå 2",
                "Concise SumNivå 3",
                "HSA-ID",
                "Benämning",
                "Benämning kort",
                "Adress",
                "Postnummer",
                "Postort",
                "Giltig fr.o.m.",
                "Giltig t.o.m.",
                "Anmärkning",
                "Ersätts av",
                "Ändringsdatum",
                "HSA-ID saknas i KIV"
        });

        for (Data data : result) {
            matrix.add(createRow(data));
        }
        return matrix;
    }


    private String[] createRow(Data data) {

        AgarformEnum agarform = AgarformEnum.getByKey(data.getAgarform());

        return new String[]{
                data.getArbetsplatskodlan(),
                agarform != null ? agarform.getLabel() : data.getAgarform(),
                getAo3Text(data.getAo3()),
                data.getAnsvar(),
                data.getFrivilligUppgift(),
                data.getExternfakturamodell(),
                data.getVgpv() ? "Ja" : "Nej",
                getVardformText(data.getVardform()),
                getVerksamhetText(data.getVerksamhet()),
                data.getProdn1() != null ? data.getProdn1().getKortnamn() : "",
                data.getProdn3() != null && data.getProdn3().getProdn2() != null ? data.getProdn3().getProdn2().getKortnamn() : "",
                data.getProdn3() != null ? data.getProdn3().getKortnamn() : "",
                data.getHsaid(),
                data.getBenamning(),
                data.getBenamningKort(),
                data.getPostadress(),
                data.getPostnr(),
                data.getPostort(),
                toDateString(data.getFromDatum()),
                toDateString(data.getTillDatum()),
                data.getAnmarkning(),
                data.getErsattav(),
                data.getAndringsdatum(),
                data.getHsaidMissingInKiv() != null && data.getHsaidMissingInKiv() ? "Ja" : "Nej"
        };
    }

    private String getAo3Text(String ao3id) {
        Cache cache = cacheManager.getCache("ao3");

        Element element = cache.get(ao3id);
        if (element != null) {
            return (String) element.getObjectValue();
        } else {
            Ao3 byAo3id = ao3Repository.findByAo3id(ao3id);

            String ao3text = ao3id + ", " + byAo3id.getForetagsnamn();

            cache.put(new Element(ao3id, ao3text));

            return ao3text;
        }
    }

    private String getVerksamhetText(String verksamhetid) {
        Cache cache = cacheManager.getCache("verksamhet");

        Element element = cache.get(verksamhetid);
        if (element != null) {
            return (String) element.getObjectValue();
        } else {
            Verksamhet byVerksamhetid = verksamhetRepository.findByVerksamhetid(verksamhetid);

            String verksamhettext = verksamhetid + ", " + byVerksamhetid.getVerksamhettext();

            cache.put(new Element(verksamhetid, verksamhettext));

            return verksamhettext;
        }
    }

    private String getVardformText(String vardformid) {
        Cache cache = cacheManager.getCache("vardform");

        Element element = cache.get(vardformid);
        if (element != null) {
            return (String) element.getObjectValue();
        } else {
            Vardform byVardformid = vardformRepository.findByVardformid(vardformid);

            String vardformtext = vardformid + ", " + (byVardformid != null ? byVardformid.getVardformtext() : "VÅRDFORM SAKNAS");

            cache.put(new Element(vardformid, vardformtext));

            return vardformtext;
        }
    }

    private static String toDateString(Timestamp timestamp) {
        if (timestamp == null || timestamp.toLocalDateTime() == null) {
            return "";
        }

        return timestamp.toLocalDateTime().format(DateTimeFormatter.ISO_DATE);
    }

    private ResponseEntity<byte[]> getReportFile(String tempFileName, String outputFileName) {

        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Disposition", Collections.singletonList("attachment; filename=" + outputFileName));

        FileBlob one = fileBlobRepository.findOne(tempFileName);

        byte[] content = one.getContent();

        fileBlobRepository.delete(tempFileName);

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

}
