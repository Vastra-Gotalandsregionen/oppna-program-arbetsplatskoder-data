package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.ViewapkHsaid;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkwithao3;
import se.vgregion.arbetsplatskoder.export.repository.ViewApkForSesamLmnRepository;
import se.vgregion.arbetsplatskoder.export.repository.ViewapkHsaidRepository;
import se.vgregion.arbetsplatskoder.export.repository.Viewapkwithao3Repository;
import se.vgregion.arbetsplatskoder.service.*;

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
    ViewApkForSesamLmnRepository viewApkForSesamLmnRepository;

    @Autowired
    LokeDatabaseIntegrationService lokeDatabaseIntegrationService;

    @Autowired
    Viewapkwithao3Repository viewapkwithao3Repository;

    @Autowired
    KivSesamLmnDatabaseIntegrationService kivSesamLmnDatabaseIntegrationService;

    @Autowired
    SesamLmnDatabaseIntegrationService sesamLmnDatabaseIntegrationService;

    @Autowired
    private ViewapkHsaidRepository viewapkHsaidRepository;

    @RequestMapping(value = "ehalsomyndigheten", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public String get() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Type", Collections.singletonList("text/plain"));
        return eHalsomyndighetenExportFileService.runFileTransfer();
        // return eHalsomyndighetenExportFileService.generate();
    }

    @RequestMapping(value = "stralfors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public String fetchStralforsExport() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Type", Collections.singletonList("text/plain"));
        return stralforsExportFileService.runFileTransfer();
        // return stralforsExportFileService.generate();
    }

    @RequestMapping(value = "sesam-lmn", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity<List<se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkforsesamlmn>> fetchSesamLmnExport() {
        sesamLmnDatabaseIntegrationService.populateTables();
        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Type", Collections.singletonList("text/plain"));
        List<se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkforsesamlmn> result = viewApkForSesamLmnRepository.findAll();
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "loke", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity<List<Viewapkwithao3>> fetchLokeExport() {
        lokeDatabaseIntegrationService.populateLokeTable();
        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Type", Collections.singletonList("text/plain"));
        List<Viewapkwithao3> result = viewapkwithao3Repository.findAll();
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "kiv", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity<List<ViewapkHsaid>> fetchKivExport() {
        kivSesamLmnDatabaseIntegrationService.run();
        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Type", Collections.singletonList("text/plain"));
        List<ViewapkHsaid> result = viewapkHsaidRepository.findAll();
        return ResponseEntity.ok(result);
    }

}
