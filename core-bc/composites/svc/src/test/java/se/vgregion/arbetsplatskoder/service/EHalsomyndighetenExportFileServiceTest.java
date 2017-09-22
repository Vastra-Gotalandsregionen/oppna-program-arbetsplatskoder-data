package se.vgregion.arbetsplatskoder.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.arbetsplatskoder.repository.extension.DataRepositoryImpl;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by clalu4 on 2017-06-21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
public class EHalsomyndighetenExportFileServiceTest {

    @Autowired
    EHalsomyndighetenExportFileService eHalsomyndighetenExportFileService;

    @Test
    public void formatTillDatum() {
        String nothing = EHalsomyndighetenExportFileService.formatTillDatum("");
        assertEquals("2000-01-01", nothing);

        nothing = EHalsomyndighetenExportFileService.formatTillDatum("2199-01-01");
        assertEquals("", nothing);

        String something = EHalsomyndighetenExportFileService.formatTillDatum("1997-01-01");
        assertEquals("1997-01-02", something);

        System.out.println(EHalsomyndighetenExportFileService.formatFranDatum(new Date(), new Date()));

    }

}