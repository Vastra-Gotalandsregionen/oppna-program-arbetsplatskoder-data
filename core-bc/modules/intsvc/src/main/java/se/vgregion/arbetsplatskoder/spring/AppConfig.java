package se.vgregion.arbetsplatskoder.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Patrik Björk
 */
@Configuration
@Import(RepositoryRestMvcConfiguration.class)
@EnableScheduling
public class AppConfig {

    /*@Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {

        return new RepositoryRestConfigurerAdapter() {

            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
                config.setBasePath("/api");
            }
        };
    }*/
}
