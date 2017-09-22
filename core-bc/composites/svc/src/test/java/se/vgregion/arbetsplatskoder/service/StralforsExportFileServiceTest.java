package se.vgregion.arbetsplatskoder.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

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