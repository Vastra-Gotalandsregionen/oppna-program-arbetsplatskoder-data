package se.vgregion.arbetsplatskoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.repository.DataRepository;

import java.text.SimpleDateFormat;
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
public class SesamLmnExportFileService {

  @Autowired
  private DataRepository dataRepository;

  private static final Logger LOGGER = LoggerFactory.getLogger(SesamLmnExportFileService.class);

  public String generate() {
    return generate(findAllRelevantItems());
  }

  String generate(List<Data> all) {

        /*
         *
         * As Postgresql-sql:
         *  SELECT     arbetsplatskodlan, ao3, ansvar, frivillig_uppgift, agarform, vardform, verksamhet, sorteringskod_prod,
         *  sorteringskod_best, REPLACE(benamning, ';', ',') AS benamning,
                       REPLACE(postadress, ';', ',') AS postadress, postnr, postort, kontakt_akod, leverans, fakturering, lakemedkomm,
                       externfaktura, externfakturamodell, apodos, VGPV,
                      HSAID, anmarkning, from_datum, till_datum, reg_datum
            FROM         data
            WHERE     (till_datum >= (current_date - interval '1 year'))
         */

    List<String> sb = new ArrayList<>();
    for (Data data : all) {
      sb.add(
          R(
              data.getArbetsplatskod(),
              //data.getAo3().length() > 3 ? data.getAo3().substring(0, 3) : data.getAo3(),
              data.getAo3(), // Vilken variant av ao3 ska det vara?
              data.getAnsvar(),
              data.getFrivilligUppgift(),
              data.getAgarform(),
              data.getVardform(),
              data.getVerksamhet(),
              data.getSorteringskodProd(),
              data.getSorteringskodBest(),
              data.getBenamning(),
              data.getPostadress(),
              data.getPostnr(),
              data.getPostort(),
              data.getKontaktAkod(),
              data.getLeverans(),
              data.getFakturering(),
              data.getLakemedkomm(),
              data.getExternfaktura(),
              data.getExternfakturamodell(),
              data.getApodos(),
              data.getVgpv(),
              data.getHsaid(),
              data.getAnmarkning(),
              data.getFromDatum(),
              data.getTillDatum(),
              data.getRegDatum()
          )
      );
    }
    return L(sb);
  }

  SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

  private String format(Date date) {
    if (date == null) {
      return "";
    }
    return dt.format(date);
  }

  @Transactional
  List<Data> findAllRelevantItems() {
    List<Data> all = dataRepository.findAll();
    Date currentDate = new Date();
    long oneYear = (long) 365 * 24 * 60 * 60 * 1000;
    Date oneYearBefore = new Date(currentDate.getTime() - oneYear);
    //and till_datum > #DateAdd("m", -12, now())#
    all = all.stream().filter(
        d -> F(d.getArbetsplatskod()).length() < 12
            && d.getTillDatum() != null
            && d.getTillDatum().after(oneYearBefore)).collect(Collectors.toList()
    );
    all.sort(Comparator.comparing(Data::getArbetsplatskod));
    return all;
  }

  private String R(Object... parts) {
    List<String> lines = new ArrayList<>();
    for (Object part : parts) lines.add(F(part));
    return String.join(";", lines);
  }

  private String L(List<String> parts) {
    return String.join("\n", parts);
  }

  private String F(Object s) {
    if (s == null) return "";
    return s.toString().replace(';', ',');
  }

}
