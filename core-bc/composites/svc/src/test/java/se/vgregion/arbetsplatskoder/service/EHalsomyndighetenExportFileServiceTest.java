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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;

/**
 * Created by clalu4 on 2017-06-21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/applicationContext.xml")
public class EHalsomyndighetenExportFileServiceTest {

  @Autowired
  EHalsomyndighetenExportFileService eHalsomyndighetenExportFileService;

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

    String oldSql = "SELECT arbetsplatskod,\n" +
        "         benamning,\n" +
        "         left(ao3,3) as a03b ,\n" +
        "         ansvar,\n" +
        "         frivillig_uppgift,\n" +
        "         agarform,\n" +
        "         vardform,\n" +
        "         verksamhet,\n" +
        "         from_datum,\n" +
        "         till_datum,\n" +
        "         postadress,\n" +
        "         postnr,\n" +
        "         postort,\n" +
        "         ersattav,\n" +
        "         sorteringskod_prod,\n" +
        "         lankod,\n" +
        "         kommun,\n" +
        "         lakemedkomm\n" +
        "         FROM data\n" +
        "         where length(arbetsplatskod) < 12\n" +
        "         and till_datum > (current_date - 365)\n" +
        "         order by arbetsplatskod";
    List<Object[]> withOldSql = crud.query(oldSql);
    System.out.println(withOldSql);
    assertNotNull(crud);
    List<Data> withNewJpa = eHalsomyndighetenExportFileService.findAllRelevantItems();
    // assertEquals(withOldSql.size(), withNewJpa.size());

    StringBuilder sb = new StringBuilder();
    for (Object[] objects : withOldSql) {
      sb.append(String.join(";", Arrays.asList(objects).stream()
          .map(o -> o == null ? "" : o.toString().replace(";",","))
          .collect(Collectors.toList())));
      sb.append("\n");
    }
    System.out.println(sb);
  }

  @Test
  public void findAllRelevantItems() throws Exception {
    List<Data> jpaFiltering = eHalsomyndighetenExportFileService.findAllRelevantItems();
    // Tests remains. Smoke test at the moment, only.
  }

}