package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

/**
 * For integration. SesamLmn wants to have data.
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "data")
public class DataExport extends AbstractData {

    @Column(name = "Kortnamn")
    private String kortnamn;

    @Column(name = "Kortnamn2")
    private String kortnamn2;

    @Column(name = "Kortnamn3")
    private String kortnamn3;

    final static Date indefiniteTime;

    static {
        try {
            indefiniteTime = new SimpleDateFormat("yyyy-MM-dd").parse("2199-12-01");
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

        Prodn1 prodn1 = data.getProdn1();
        if (prodn1 != null) {
            setKortnamn(prodn1.getKortnamn());
        }

        Prodn3 prodn3 = data.getProdn3();
        if (prodn3 != null) {
            setKortnamn3(prodn3.getKortnamn());

            Prodn2 prodn2 = prodn3.getProdn2();
            if (prodn2 != null) {
                setKortnamn2(prodn2.getKortnamn());
            }
        }
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

    public String getKortnamn() {
        return kortnamn;
    }

    public void setKortnamn(String kortnamn) {
        this.kortnamn = kortnamn;
    }

    public String getKortnamn2() {
        return kortnamn2;
    }

    public void setKortnamn2(String kortnamn2) {
        this.kortnamn2 = kortnamn2;
    }

    public String getKortnamn3() {
        return kortnamn3;
    }

    public void setKortnamn3(String kortnamn3) {
        this.kortnamn3 = kortnamn3;
    }
}
