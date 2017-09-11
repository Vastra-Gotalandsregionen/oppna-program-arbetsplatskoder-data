package se.vgregion.arbetsplatskoder.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
public class BatchServiceTest {

  @Autowired
  BatchService batchService;

  @Test
  public void runSesamLmnTransfer() {
    batchService.runSesamLmnTransfer();
  }

}