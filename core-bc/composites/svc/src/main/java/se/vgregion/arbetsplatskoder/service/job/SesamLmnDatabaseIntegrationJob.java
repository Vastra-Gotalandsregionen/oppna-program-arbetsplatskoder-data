package se.vgregion.arbetsplatskoder.service.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.vgregion.arbetsplatskoder.service.SesamLmnDatabaseIntegrationService;

public class SesamLmnDatabaseIntegrationJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(SesamLmnDatabaseIntegrationJob.class);

    @Autowired
    private SesamLmnDatabaseIntegrationService sesamLmnDatabaseIntegrationService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        sesamLmnDatabaseIntegrationService.populateTable();
    }

}
