package se.vgregion.arbetsplatskoder.spring;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import se.vgregion.arbetsplatskoder.service.job.EHalsomyndighetenExportFileJob;
import se.vgregion.arbetsplatskoder.service.job.KivSesamLmnDatabaseIntegrationJob;
import se.vgregion.arbetsplatskoder.service.job.LokeDatabaseIntegrationJob;
import se.vgregion.arbetsplatskoder.service.job.SesamLmnDatabaseIntegrationJob;
import se.vgregion.arbetsplatskoder.service.job.StralforsExportFileJob;

import java.io.File;

@Configuration
public class QuartzConfig {

    @Value("${jdbc.driver}")
    private String jdbcDriver;

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.user}")
    private String jdbcUser;

    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean(name = "jobDetailEhalosmyndigheten")
    public JobDetailFactoryBean jobDetailEhalosmyndigheten() {
        return getJobDetailFactoryBean("EHalsomyndighetenExportFileJob description",
                EHalsomyndighetenExportFileJob.class);
    }

    @Bean(name = "jobDetailKivSesamLmnDatabaseIntegration")
    public JobDetailFactoryBean jobDetailKivSesam() {
        return getJobDetailFactoryBean("KivSesamLmnDatabaseIntegrationJob description",
                KivSesamLmnDatabaseIntegrationJob.class);
    }

    @Bean(name = "jobDetailLokeDatabaseIntegration")
    public JobDetailFactoryBean jobDetailLoke() {
        return getJobDetailFactoryBean("LokeDatabaseIntegrationJob description",
                LokeDatabaseIntegrationJob.class);
    }

    @Bean(name = "jobDetailStralfors")
    public JobDetailFactoryBean jobDetailStralfors() {
        return getJobDetailFactoryBean("StralforsExportFileJob description",
                StralforsExportFileJob.class);
    }

    @Bean(name = "jobDetailSesam")
    public JobDetailFactoryBean jobDetailSesam() {
        return getJobDetailFactoryBean("SesamLmnDatabaseIntegrationJob description",
                SesamLmnDatabaseIntegrationJob.class);
    }

    @Bean(name = "ehalsomyndighetenTrigger")
    public CronTriggerFactoryBean triggerEhalsomyndigheten(@Qualifier("jobDetailEhalosmyndigheten") JobDetail job) {
        return getCronTriggerFactoryBean(job, "0 15,45 * ? * MON-FRI", "EHalsomyndighetenExportFileJob");
    }

    @Bean(name = "kivSesamTrigger")
    public CronTriggerFactoryBean triggerKivSesam(@Qualifier("jobDetailKivSesamLmnDatabaseIntegration") JobDetail job) {
        return getCronTriggerFactoryBean(job, "0 15,45 * ? * MON-FRI", "KivSesamLmnDatabaseIntegrationJob");
    }

    @Bean(name = "lokeTrigger")
    public CronTriggerFactoryBean triggerLoke(@Qualifier("jobDetailLokeDatabaseIntegration") JobDetail job) {
        return getCronTriggerFactoryBean(job, "0 15,45 * ? * MON-FRI", "LokeDatabaseIntegrationJob");
    }

    @Bean(name = "stralforsTrigger")
    public CronTriggerFactoryBean triggerStralfors(@Qualifier("jobDetailStralfors") JobDetail job) {
        return getCronTriggerFactoryBean(job, "0 15,45 * ? * MON-FRI", "StralforsExportFileJob");
    }

    @Bean(name = "sesamTrigger")
    public CronTriggerFactoryBean triggerSesam(@Qualifier("jobDetailSesam") JobDetail job) {
        return getCronTriggerFactoryBean(job, "0 15,45 * ? * MON-FRI", "SesamLmnDatabaseIntegrationJob");
    }

    CronTriggerFactoryBean getCronTriggerFactoryBean(JobDetail job, String cronExpression, String name) {
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setJobDetail(job);
        trigger.setCronExpression(cronExpression);
        trigger.setStartDelay(0);
        trigger.setName(name);

        return trigger;
    }

    JobDetailFactoryBean getJobDetailFactoryBean(String description, Class<?> jobClass) {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(jobClass);
        jobDetailFactory.setDescription(description);
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    public SchedulerFactoryBean scheduler(@Qualifier("ehalsomyndighetenTrigger") Trigger trigger1,
                                          @Qualifier("kivSesamTrigger") Trigger trigger2,
                                          @Qualifier("lokeTrigger") Trigger trigger3,
                                          @Qualifier("stralforsTrigger") Trigger trigger4,
                                          @Qualifier("sesamTrigger") Trigger trigger5,
                                          @Qualifier("jobDetailEhalosmyndigheten") JobDetail job1,
                                          @Qualifier("jobDetailKivSesamLmnDatabaseIntegration") JobDetail job2,
                                          @Qualifier("jobDetailLokeDatabaseIntegration") JobDetail job3,
                                          @Qualifier("jobDetailStralfors") JobDetail job4,
                                          @Qualifier("jobDetailSesam") JobDetail job5) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

        Resource configLocation = getConfigLocation();
        schedulerFactory.setConfigLocation(configLocation);

        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setJobDetails(job1, job2, job3, job4, job5);
        schedulerFactory.setTriggers(trigger1, trigger2, trigger3, trigger4, trigger5);
        return schedulerFactory;
    }

    private Resource getConfigLocation() {
        String path = System.getProperty("user.home") + "/.app/arbetsplatskoder/quartz.properties";
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalStateException("File " + file.getAbsolutePath() + " doesn't exist. Create the file " +
                    "based on quartz.properties.template.");
        }
        return new FileSystemResource(path);
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }
}
