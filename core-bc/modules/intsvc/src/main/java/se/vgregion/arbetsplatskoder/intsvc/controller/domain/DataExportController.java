package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.db.service.Crud;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkwithao3Temp;
import se.vgregion.arbetsplatskoder.repository.IntegrationExportRepository;
import se.vgregion.arbetsplatskoder.service.EHalsomyndighetenExportFileService;
import se.vgregion.arbetsplatskoder.service.SesamLmnExportFileService;
import se.vgregion.arbetsplatskoder.service.StralforsExportFileService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * Note: When more files are to be exported - change the name of this controller to a more generic 'exportich' one.
 */
@Controller
@RequestMapping("/export")
public class DataExportController {

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private EHalsomyndighetenExportFileService eHalsomyndighetenExportFileService;

  @Autowired
  private StralforsExportFileService stralforsExportFileService;

  @Autowired
  private SesamLmnExportFileService sesamLmnExportFileService;

  @RequestMapping(value = "ehalsomyndigheten", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public String get() {
    HttpHeaders headers = new HttpHeaders();
    headers.put("Content-Type", Collections.singletonList("text/plain"));
    return eHalsomyndighetenExportFileService.generate();
  }

  @RequestMapping(value = "stralfors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public String fetchStralforsExport() {
    HttpHeaders headers = new HttpHeaders();
    headers.put("Content-Type", Collections.singletonList("text/plain"));
    return stralforsExportFileService.generate();
  }

  @RequestMapping(value = "sesam-lmn", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public String fetchSesamLmnExport() {
    HttpHeaders headers = new HttpHeaders();
    headers.put("Content-Type", Collections.singletonList("text/plain"));
    return sesamLmnExportFileService.generate();
  }

  @RequestMapping(value = "loke", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public ResponseEntity<List<Viewapkwithao3Temp>> fetchLokeExport() {
    HttpHeaders headers = new HttpHeaders();
    headers.put("Content-Type", Collections.singletonList("text/plain"));
    Crud crud = new IntegrationExportRepository().getCrud();
    List<Viewapkwithao3Temp> result = crud.find(new Viewapkwithao3Temp());
    return ResponseEntity.ok(result);
  }

}
