package se.vgregion.arbetsplatskoder.service.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.vgregion.arbetsplatskoder.service.LokeDatabaseIntegrationService;

public class LokeDatabaseIntegrationJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(LokeDatabaseIntegrationJob.class);

    @Autowired
    private LokeDatabaseIntegrationService lokeDatabaseIntegrationService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        lokeDatabaseIntegrationService.populateLokeTable();
    }

}
