package se.vgregion.arbetsplatskoder.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
public class StralforsExportFileServiceTest {

  @Autowired
  StralforsExportFileService stralforsExportFileService;

  @Test
  public void generate() throws Exception {
    assertNotNull(stralforsExportFileService);
    //List<List<Object>> result = stralforsExportFileService.findAllRelevantTextualItems();
    String generatedStuff = stralforsExportFileService.generate();
    System.out.println(generatedStuff);
  }




}
