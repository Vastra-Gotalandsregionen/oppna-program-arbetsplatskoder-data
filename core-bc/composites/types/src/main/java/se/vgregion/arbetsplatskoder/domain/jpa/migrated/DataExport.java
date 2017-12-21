package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

/**
 * For integration. SesamLmn wants to have data.
 */

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "data")
public class DataExport extends AbstractData {

    final static Date indefiniteTime;

    static {
        try {
            indefiniteTime = new SimpleDateFormat("YYYY-mm-dd").parse("2199-12-01");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DataExport() {
        super();
    }

    public DataExport(Data data) {
        this();
        setVgpv(data.getVgpv());
        setAo3(data.getAo3());
        setUserIdNew(data.getUserIdNew());
        setAndringsdatum(data.getAndringsdatum());
        setSlask(data.getSlask());
        setDeletable(data.getDeletable());
        setExternfaktura(data.getExternfaktura());
        setSorteringskodBest(data.getSorteringskodBest());
        setAnsvar(data.getAnsvar());
        setApodos(data.getApodos());
        setKontaktAkod(data.getKontaktAkod());
        if (data.getTillDatum() == null) {
            setTillDatum(new Timestamp(indefiniteTime.getTime()));
        } else {
            setTillDatum(data.getTillDatum());
        }

        setFrivilligUppgift(data.getFrivilligUppgift());
        setLankod(data.getLankod());
        setPostadress(data.getPostadress());
        setHsaidMissingInKiv(data.getHsaidMissingInKiv());
        setPostnr(data.getPostnr());
        setAnmarkning(data.getAnmarkning());
        setErsattav(data.getErsattav());
        setBenamning(data.getBenamning());
        setExternfakturamodell(data.getExternfakturamodell());
        setId(data.getId());
        setBenamningKort(data.getBenamningKort());
        setGroupCode(data.getGroupCode());
        setVardform(data.getVardform());
        setVerksamhet(data.getVerksamhet());
        setArbetsplatskodlan(data.getArbetsplatskodlan());
        setNamn(data.getNamn());
        setFakturering(data.getFakturering());
        setLeverans(data.getLeverans());
        setArbetsplatskod(data.getArbetsplatskod());
        setAgarform(data.getAgarform());
        setKommunkod(data.getKommunkod());
        setUserId(data.getUserId());
        setFromDatum(data.getFromDatum());
        setRegDatum(data.getRegDatum());
        setLakemedkomm(data.getLakemedkomm());
        setKommun(data.getKommun());
        setSorteringskodProd(data.getSorteringskodProd());
        setHsaid(data.getHsaid());
        setPostort(data.getPostort());
    }

    /*
    public static void main(String[] args) {
        Data data = new Data();
        DataExport export = new DataExport();
        BeanMap dataMap = new BeanMap(data);
        BeanMap exportMap = new BeanMap(export);

        for (String key : exportMap.keySet()) {
            if ("class".equals(key))
                continue;
            System.out.println(
                    exportMap.getPropertyDesc(key).getWriteMethod().getName()
                            + "(data." + dataMap.getPropertyDesc(key).getReadMethod().getName() + "());"
            );
        }
    }
    */


}