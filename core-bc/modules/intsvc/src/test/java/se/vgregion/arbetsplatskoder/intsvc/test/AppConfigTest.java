package se.vgregion.arbetsplatskoder.intsvc.test;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import se.vgregion.arbetsplatskoder.service.EHalsomyndighetenExportFileService;
import se.vgregion.arbetsplatskoder.service.SesamLmnExportFileService;
import se.vgregion.arbetsplatskoder.service.StralforsExportFileService;

import javax.servlet.http.HttpServletRequest;

@Configuration
@Import(JpaConfigTest.class)
@EnableJpaRepositories(value = {"se.vgregion.arbetsplatskoder.repository", "se.vgregion.arbetsplatskoder.export.repository"})
@ComponentScan("se.vgregion.arbetsplatskoder.intsvc.controller.domain")
@ComponentScan(basePackageClasses = se.vgregion.arbetsplatskoder.service.JwtUtil.class)
@PropertySource("classpath:application-test.properties")
public class AppConfigTest {

    @Bean
    public HttpServletRequest httpServletRequest() {
        return Mockito.mock(HttpServletRequest.class);
    }

    @Bean
    public EHalsomyndighetenExportFileService eHalsomyndighetenExportFileService() {
        return Mockito.mock(EHalsomyndighetenExportFileService.class);
    }

    @Bean
    public StralforsExportFileService stralforsExportFileService() {
        return Mockito.mock(StralforsExportFileService.class);
    }

    @Bean
    public SesamLmnExportFileService sesamLmnExportFileService() {
        return Mockito.mock(SesamLmnExportFileService.class);
    }

}
