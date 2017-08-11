package se.vgregion.arbetsplatskoder.repository;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntegrationExportRepositoryTest {

  @Test
  public void isExportConfigPresentInEnvironment() {
    System.out.println(IntegrationExportRepository.isExportConfigPresentInEnvironment());
  }

}