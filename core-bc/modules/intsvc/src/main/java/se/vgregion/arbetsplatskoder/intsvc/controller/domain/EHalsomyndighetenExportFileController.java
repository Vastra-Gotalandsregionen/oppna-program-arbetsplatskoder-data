package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.service.EHalsomyndighetenExportFileService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * Note: When more files are to be exported - change the name of this controller to a more generic 'exportich' one.
 */
@Controller
@RequestMapping("/export")
public class EHalsomyndighetenExportFileController {

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private EHalsomyndighetenExportFileService eHalsomyndighetenExportFileService;

  @RequestMapping(value = "ehalsomyndigheten", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public String get() {
    HttpHeaders headers = new HttpHeaders();
    headers.put("Content-Type", Collections.singletonList("text/plain"));
    return eHalsomyndighetenExportFileService.generate();
  }

}
