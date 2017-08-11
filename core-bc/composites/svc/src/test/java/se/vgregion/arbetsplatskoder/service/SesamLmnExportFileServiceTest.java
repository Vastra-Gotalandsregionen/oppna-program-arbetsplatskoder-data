package se.vgregion.arbetsplatskoder.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.arbetsplatskoder.db.service.Crud;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.repository.DataRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by clalu4 on 2017-06-21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/applicationContext.xml")
public class SesamLmnExportFileServiceTest {

  @Autowired
  SesamLmnExportFileService sesamLmnExportFileService;

  @Autowired
  private DataRepository dataRepository;

  @Autowired
  private Crud crud;

  /**
   * The original sql for MS SQLServer looked like this:
   * <code>
   * SELECT arbetsplatskod,
   * benamning,
   * left(ao3,3) as a03b ,
   * ansvar,frivillig_uppgift,
   * agarform,
   * vardform,
   * verksamhet,
   * from_datum,
   * till_datum,
   * postadress,
   * postnr,
   * postort,
   * ersattav,
   * sorteringskod_prod,
   * lankod,
   * kommun,
   * lakemedkomm
   * FROM data
   * where len(arbetsplatskod) < 12
   * and till_datum > #DateAdd("m", -12, now())#
   * order by arbetsplatskod
   * </code>
   *
   * @throws Exception
   */
  @Ignore
  @Test
  public void compareFindAllRelevantItemsWithOldishDito() throws Exception {
    String oldishSql = "SELECT     arbetsplatskodlan, ao3, ansvar, frivillig_uppgift, agarform, vardform, verksamhet, sorteringskod_prod, sorteringskod_best, REPLACE(benamning, ';', ',') AS benamning,\n" +
        "                       REPLACE(postadress, ';', ',') AS postadress, postnr, postort, kontakt_akod, leverans, fakturering, lakemedkomm, externfaktura, externfakturamodell, apodos, VGPV, \n" +
        "                      HSAID, anmarkning, from_datum, till_datum, reg_datum\n" +
        "FROM         data\n" +
        "WHERE     (till_datum >= (current_date - interval '1 year'))";

    List<Object[]> withOldSql = crud.query(oldishSql);
    assertNotNull(crud);

    StringBuilder sb = new StringBuilder();
    for (Object[] objects : withOldSql) {
      sb.append(String.join(";", Arrays.asList(objects).stream()
          .map(o -> o == null ? "" : o.toString().replace(";", ","))
          .collect(Collectors.toList())));
      sb.append("\n");
    }

    assertEquals(withOldSql.size(), sesamLmnExportFileService.findAllRelevantItems().size());

    String newishJpaResult = sesamLmnExportFileService.generate();
    String oldishSqlTextResult = sb.toString().trim();

    assertEquals(oldishSqlTextResult, newishJpaResult);
  }

  @Test
  public void findAllRelevantItems() throws Exception {
    List<Data> jpaFiltering = sesamLmnExportFileService.findAllRelevantItems();
  }

}