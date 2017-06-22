package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

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
import se.vgregion.arbetsplatskoder.domain.ReportType;
import se.vgregion.arbetsplatskoder.domain.jpa.FileBlob;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.json.Report;
import se.vgregion.arbetsplatskoder.repository.DataRepository;
import se.vgregion.arbetsplatskoder.repository.FileBlobRepository;
import se.vgregion.arbetsplatskoder.service.HmacUtil;
import se.vgregion.arbetsplatskoder.util.ExcelUtil;

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

    static List<String[]> dataSetToMatrix(List<Data> result) {
        List<String[]> matrix = new ArrayList<>();

        matrix.add(new String[]{
                "Arbetsplatskod",
                "Ao3",
                "Ansvar",
                "Frivillig uppgift",
                "Ägarform",
                "Vårdform",
                "Verksamhet",
                "Summeringsnivå 1",
                "Summeringsnivå 2",
                "Summeringsnivå 3",
                "Benämning",
                "Giltig fr.o.m.",
                "Giltig t.o.m.",
                "Ändringsdatum"
        });

        for (Data data : result) {
            matrix.add(createRow(data));
        }
        return matrix;
    }


    private static String[] createRow(Data data) {

        String[] row = new String[]{
                data.getArbetsplatskodlan(),
                data.getAo3(),
                data.getAnsvar(),
                data.getFrivilligUppgift(),
                data.getAgarform(),
                data.getVardform(),
                data.getVerksamhet(),
                data.getProdn1() != null ? data.getProdn1().getKortnamn() : "",
                data.getProdn3() != null && data.getProdn3().getProdn2() != null ? data.getProdn3().getProdn2().getAvdelning() : "",
                data.getProdn3() != null ? data.getProdn3().getForetagsnamn() : "",
                data.getBenamning(),
                toDateString(data.getFromDatum()),
                toDateString(data.getTillDatum()),
                data.getAndringsdatum()
        };

        return row;
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
