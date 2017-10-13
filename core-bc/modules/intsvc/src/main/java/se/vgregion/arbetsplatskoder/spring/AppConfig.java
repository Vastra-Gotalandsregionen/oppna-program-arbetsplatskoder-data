package se.vgregion.arbetsplatskoder.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author Patrik Björk
 */
@Configuration
@Import({RepositoryRestMvcConfiguration.class, QuartzConfig.class})
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppConfig {

    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> authentication;
    }
}
