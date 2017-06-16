package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.apache.poi.util.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import se.vgregion.arbetsplatskoder.domain.ReportType;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.json.Report;
import se.vgregion.arbetsplatskoder.repository.DataRepository;
import se.vgregion.arbetsplatskoder.service.HmacUtil;
import se.vgregion.arbetsplatskoder.util.ExcelUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportControllerTest.class);

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private ReportController reportController = new ReportController();

    private List<Data> datas;
    private List<Data> dataSubList;

    @Before
    public void setup() {
        Data d1 = new Data();
        Data d2 = new Data();
        Data d3 = new Data();

        d1.setFromDatum(Timestamp.from(Instant.now()));
        d1.setTillDatum(Timestamp.from(Instant.now()));

        datas = Arrays.asList(d1, d2, d3);
        dataSubList = datas.subList(0, 2);

        when(dataRepository.findAllValidWithoutEndDate()).thenReturn(datas);
        when(dataRepository.findAllUpdatedBetween(anyString(), anyString())).thenReturn(dataSubList);

        HmacUtil hmacUtil = new HmacUtil();
        ReflectionTestUtils.setField(hmacUtil, "jwtSignSecret", "aslökdjfasölkjdf");
        hmacUtil.init();
    }

    @Test
    public void testReportValitWithoutEndDate() throws Exception {
        ResponseEntity<Report> reportResponseEntity = reportController.generateReportFile(null, null, ReportType.VALID_WITHOUT_END_DATE);

        ResponseEntity<byte[]> generatedReport = reportController.getGeneratedReport(
                ReportType.VALID_WITHOUT_END_DATE,
                reportResponseEntity.getBody().getFileName(),
                reportResponseEntity.getBody().getHmac(),
                null,
                null);

        InputStream inputStream = ExcelUtil.exportToStream(ReportController.dataSetToMatrix(datas));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        IOUtils.copy(inputStream, baos);

        byte[] wanted = baos.toByteArray();
        byte[] actual = generatedReport.getBody();

        // The generated bytes are not always exactly the same, but the length should never differ that much.
        assertTrue(Math.abs(wanted.length - actual.length) < 10);

        generatedReport.getHeaders();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(new Date());

        assertEquals(
                "attachment; filename=arbetsplatskoder_giltiga_tills_vidare_" + dateString + ".xlsx",
                generatedReport.getHeaders().get("Content-Disposition").get(0)
        );
    }

    @Test
    public void testReportValitWithoutEndDateModifiedHmac() throws Exception {
        ResponseEntity<Report> reportResponseEntity = reportController.generateReportFile(
                null, null, ReportType.VALID_WITHOUT_END_DATE);

        ResponseEntity<byte[]> generatedReport = reportController.getGeneratedReport(
                ReportType.VALID_WITHOUT_END_DATE,
                reportResponseEntity.getBody().getFileName(),
                "mumboJumbo",
                null,
                null);

        assertTrue(generatedReport.getStatusCode().is4xxClientError());
    }

    @Test
    public void testReportValitWithoutEndDateModifiedFileName() throws Exception {
        ResponseEntity<Report> reportResponseEntity = reportController.generateReportFile(
                null, null, ReportType.VALID_WITHOUT_END_DATE);

        ResponseEntity<byte[]> generatedReport = reportController.getGeneratedReport(
                ReportType.VALID_WITHOUT_END_DATE,
                "aSecretFile.txt",
                reportResponseEntity.getBody().getHmac(),
                null,
                null);

        assertTrue(generatedReport.getStatusCode().is4xxClientError());
    }

    @Test
    public void testReportUpdatedBetweenDatesInvalidDate() throws Exception {
        String fromDate = "2017-asdf-01";
        String toDate = "2017-06-30";

        ResponseEntity<Report> reportResponseEntity = reportController.generateReportFile(fromDate, toDate, ReportType.UPDATED_BETWEEN_DATES);

        assertTrue(reportResponseEntity.getStatusCode().is4xxClientError());
    }

@Test
    public void testReportUpdatedBetweenDates() throws Exception {
        String fromDate = "2017-06-01";
        String toDate = "2017-06-30";

        ResponseEntity<Report> reportResponseEntity = reportController.generateReportFile(fromDate, toDate, ReportType.UPDATED_BETWEEN_DATES);

        ResponseEntity<byte[]> generatedReport = reportController.getGeneratedReport(
                ReportType.UPDATED_BETWEEN_DATES,
                reportResponseEntity.getBody().getFileName(),
                reportResponseEntity.getBody().getHmac(),
                fromDate,
                toDate);

        // Expected
        InputStream inputStream = ExcelUtil.exportToStream(ReportController.dataSetToMatrix(dataSubList));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, baos);

        byte[] wanted = baos.toByteArray();
        byte[] actual = generatedReport.getBody();

        // The generated bytes are not always exactly the same, but the length should never differ that much.
        assertTrue(Math.abs(wanted.length - actual.length) < 10);

        assertEquals(
                "attachment; filename=arbetsplatskoder_giltiga_mellan_" + fromDate + "_och_" + toDate + ".xlsx",
                generatedReport.getHeaders().get("Content-Disposition").get(0)
        );
    }


}