package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.repository.DataRepository;
import se.vgregion.arbetsplatskoder.repository.UserRepository;
import se.vgregion.arbetsplatskoder.util.ExcelUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

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
}
