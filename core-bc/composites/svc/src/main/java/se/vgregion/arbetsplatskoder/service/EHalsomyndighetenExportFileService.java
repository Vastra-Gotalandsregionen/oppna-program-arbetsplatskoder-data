package se.vgregion.arbetsplatskoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.repository.DataRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author clalu4
 */
@Service
@Transactional
public class EHalsomyndighetenExportFileService {

  @Autowired
  private DataRepository dataRepository;

  private static final Logger LOGGER = LoggerFactory.getLogger(EHalsomyndighetenExportFileService.class);

  public String generate() {
    return generate(findAllRelevantItems());
  }

  String generate(List<Data> all) {

        /*
         * SELECT arbetsplatskod,
         benamning,
         left(ao3,3) as a03b ,
         ansvar,frivillig_uppgift,
         agarform,
         vardform,
         verksamhet,
         from_datum,
         till_datum,
         postadress,
         postnr,
         postort,
         ersattav,
         sorteringskod_prod,
         lankod,
         kommun,
         lakemedkomm
         FROM data
         where len(arbetsplatskod) < 12
         and till_datum > #DateAdd("m", -12, now())#
         order by arbetsplatskod
         */

    StringBuilder sb = new StringBuilder();
    for (Data data : all) {
      sb.append(
          R(
              data.getArbetsplatskod(),
              data.getBenamning(),
              data.getAo3().length() > 3 ? data.getAo3().substring(0, 3) : data.getAo3(),
              data.getAnsvar(),
              data.getFrivilligUppgift(),
              data.getAgarform(),
              data.getVardform(),
              data.getVerksamhet(),
              data.getFromDatum(),
              data.getTillDatum(),
              data.getPostadress(),
              data.getPostnr(),
              data.getPostort(),
              data.getErsattav(),
              data.getSorteringskodProd(),
              data.getLankod(),
              data.getKommun(),
              data.getLakemedkomm()
          )
      );
      sb.append('\n');
    }
    return sb.toString();
  }

  @Transactional
  List<Data> findAllRelevantItems() {
    List<Data> all = dataRepository.findAll();

    Date currentDate = new Date();
    long oneYear = (long) 365 * 24 * 60 * 60 * 1000;
    Date oneYearBefore = new Date(currentDate.getTime() - oneYear);
    //Date oneYearAgo = new Date(System.currentTimeMillis() - (365 * 24 * 60 * 60 * 1000));

    all = all.stream().filter(
        d -> F(d.getArbetsplatskod()).length() < 12
            && d.getTillDatum() != null
            && d.getTillDatum().before(oneYearBefore)).collect(Collectors.toList()
    );
    all.sort(Comparator.comparing(Data::getArbetsplatskod));
    return all;
  }

  private String R(Object... parts) {
    List<String> lines = new ArrayList<>();
    for (Object part : parts) lines.add(F(part));
    return String.join(";", lines);
  }

  private String F(Object s) {
    if (s == null) return "";
    return s.toString().replace(';', ',');
  }

}
