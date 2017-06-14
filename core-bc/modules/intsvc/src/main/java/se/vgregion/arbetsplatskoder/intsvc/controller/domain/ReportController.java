package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.json.Report;
import se.vgregion.arbetsplatskoder.repository.DataRepository;
import se.vgregion.arbetsplatskoder.repository.UserRepository;
import se.vgregion.arbetsplatskoder.util.ExcelUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/file/all_valid/{tempFileName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]>  getAllValidReportFile(@PathVariable("tempFileName") String tempFileName) {

        String OUTPUT_FILENAME_PREFIX = "arbetsplatskoder_giltiga_";
        String OUTPUT_FILENAME_SUFFIX = ".xlsx";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String outputFileName = OUTPUT_FILENAME_PREFIX + sdf.format(new Date()) + OUTPUT_FILENAME_SUFFIX;

        return getReportFile(tempFileName, outputFileName);
    }

    @RequestMapping(value = "/generate/all_valid", method = RequestMethod.GET)
    @ResponseBody
    public Report generateAllValidReportFile() {

        Report report = new Report();

        String FILENAME_PREFIX = "apk_temp_";

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
                "Benämning"
        });

        List<Data> all = dataRepository.findAll();

        for (Data data : all) {
            matrix.add(createRow(data));
        }

        InputStream inputStream = ExcelUtil.printToFile(matrix);

        String uuid = UUID.randomUUID().toString();


        String fileName = FILENAME_PREFIX + uuid;

        try {

            File folder = new File(System.getProperty("java.io.tmpdir"));
            File file = new File(folder, fileName);

            System.out.println("---- filePath: " + file.getPath());


            OutputStream outputStream = new FileOutputStream(file);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            report.setFileName(fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return report;
    }


    @RequestMapping(value = "/allValidDatas", method = RequestMethod.GET, produces = "application/excel")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getDatas() {

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
                "Benämning"
        });

        List<Data> all = dataRepository.findAll();

        for (Data data : all) {
            matrix.add(createRow(data));
        }

        InputStream inputStream = ExcelUtil.printToFile(matrix);

        HttpHeaders headers = new HttpHeaders();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        headers.put("Content-Disposition", Collections.singletonList("attachment; filename=arbetsplatskoder_giltiga_" + sdf.format(new Date()) + ".xlsx"));

        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
    }

    private String[] createRow(Data data) {

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
                data.getBenamning()
        };

        return row;
    }

    private ResponseEntity<byte[]>  getReportFile(String tempFileName, String outputFileName) {

        File folder = new File(System.getProperty("java.io.tmpdir"));
        File tempFile = new File(folder, tempFileName);

        ByteArrayOutputStream outputStream = null;
        try (InputStream inputStream = new FileInputStream(tempFile)) {

            outputStream = new ByteArrayOutputStream();

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        tempFile.delete();

        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Disposition", Collections.singletonList("attachment; filename=" + outputFileName));

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

}
