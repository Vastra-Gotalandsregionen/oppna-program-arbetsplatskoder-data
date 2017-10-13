package se.vgregion.arbetsplatskoder.service.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.vgregion.arbetsplatskoder.service.EHalsomyndighetenExportFileService;

public class EHalsomyndighetenExportFileJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(EHalsomyndighetenExportFileJob.class);

    @Autowired
    private EHalsomyndighetenExportFileService eHalsomyndighetenExportFileService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        eHalsomyndighetenExportFileService.runFileTransfer();
    }

}
