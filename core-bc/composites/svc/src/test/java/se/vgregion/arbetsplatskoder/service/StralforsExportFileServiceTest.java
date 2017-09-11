package se.vgregion.arbetsplatskoder.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
public class StralforsExportFileServiceTest {

  @Autowired
  StralforsExportFileService stralforsExportFileService;

  @Test
  public void generate() throws Exception {
    assertNotNull(stralforsExportFileService);
    List<Data> result = stralforsExportFileService.findAllRelevantItems();
    String generatedStuff = stralforsExportFileService.generate();
    System.out.println(generatedStuff);
  }

}