package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "viewapkwithao3")
public class Viewapkwithao3 extends AbstractEntity {

    public Viewapkwithao3() {
        super();
    }

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "lankod", nullable = true)
    private java.lang.Integer lankod;

    @Column (name = "arbetsplatskod", nullable = true)
    private java.lang.String arbetsplatskod;

    @Column (name = "ao3", nullable = true)
    private java.lang.String ao3;

    @Column (name = "ansvar", nullable = true)
    private java.lang.String ansvar;

    @Column (name = "frivillig_uppgift", nullable = true)
    private java.lang.String frivilligUppgift;

    @Column (name = "agarform", nullable = true)
    private java.lang.String agarform;

    @Column (name = "vardform", nullable = true)
    private java.lang.String vardform;

    @Column (name = "verksamhet", nullable = true)
    private java.lang.String verksamhet;

    @Column (name = "sorteringskod_prod", nullable = true)
    private java.lang.String sorteringskodProd;

    @Column (name = "sorteringskod_best", nullable = true)
    private java.lang.String sorteringskodBest;

    @Column (name = "benamning", nullable = true)
    private java.lang.String benamning;

    @Column (name = "postnr", nullable = true)
    private java.lang.String postnr;

    @Column (name = "postort", nullable = true)
    private java.lang.String postort;

    @Column (name = "postadress", nullable = true)
    private java.lang.String postadress;

    @Column (name = "lakemedkomm", nullable = true)
    private java.lang.String lakemedkomm;

    @Column (name = "kontakt_akod", nullable = true)
    private java.lang.String kontaktAkod;

    @Column (name = "leverans", nullable = true)
    private java.lang.Integer leverans;

    @Column (name = "fakturering", nullable = true)
    private java.lang.Integer fakturering;

    @Column (name = "anmarkning", nullable = true, length = 4096)
    private java.lang.String anmarkning;

    @Column (name = "from_datum", nullable = true)
    private java.sql.Timestamp fromDatum;

    @Column (name = "till_datum", nullable = true)
    private java.sql.Timestamp tillDatum;

    @Column (name = "reg_datum", nullable = true)
    private java.sql.Timestamp regDatum;

    @Column (name = "ersattav", nullable = true)
    private java.lang.String ersattav;

    @Column (name = "user_id", nullable = true)
    private java.lang.Integer userId;

    @Column (name = "arbetsplatskodlan", nullable = true)
    private java.lang.String arbetsplatskodlan;

    @Column (name = "namn", nullable = true)
    private java.lang.String namn;

    @Column (name = "andringsdatum", nullable = true)
    private java.lang.String andringsdatum;

    @Column (name = "kommun", nullable = true)
    private java.lang.String kommun;

    @Column (name = "slask", nullable = true)
    private java.lang.String slask;

    @Column (name = "apodos", nullable = true)
    private java.lang.Boolean apodos;

    @Column (name = "externfaktura", nullable = true)
    private java.lang.String externfaktura;

    @Column (name = "kommunkod", nullable = true)
    private java.lang.String kommunkod;

    @Column (name = "externfakturamodell", nullable = true)
    private java.lang.String externfakturamodell;

    @Column (name = "vgpv", nullable = true)
    private java.lang.Boolean vgpv;

    @Column (name = "hsaid", nullable = true)
    private java.lang.String hsaid;

    @Column (name = "expr1", nullable = true)
    private java.lang.Integer expr1;

    @Column (name = "ao3id", nullable = true)
    private java.lang.String ao3id;

    @Column (name = "foretagsnamn", nullable = true)
    private java.lang.String foretagsnamn;

    @Column (name = "producent", nullable = true)
    private java.lang.String producent;

    @Column (name = "kontaktperson", nullable = true)
    private java.lang.String kontaktperson;

    @Column (name = "foretagsnr", nullable = true)
    private java.lang.String foretagsnr;

    @Column (name = "raderad", nullable = true)
    private java.lang.Boolean raderad;

    @Column (name = "expr2", nullable = true)
    private Byte[] expr2;

    public java.lang.Integer getId(){
        return id;
    }

    public void setId(java.lang.Integer v){
        this.id = v;
    }

    public java.lang.Integer getLankod(){
        return lankod;
    }

    public void setLankod(java.lang.Integer v){
        this.lankod = v;
    }

    public java.lang.String getArbetsplatskod(){
        return arbetsplatskod;
    }

    public void setArbetsplatskod(java.lang.String v){
        this.arbetsplatskod = v;
    }

    public java.lang.String getAo3(){
        return ao3;
    }

    public void setAo3(java.lang.String v){
        this.ao3 = v;
    }

    public java.lang.String getAnsvar(){
        return ansvar;
    }

    public void setAnsvar(java.lang.String v){
        this.ansvar = v;
    }

    public java.lang.String getFrivilligUppgift(){
        return frivilligUppgift;
    }

    public void setFrivilligUppgift(java.lang.String v){
        this.frivilligUppgift = v;
    }

    public java.lang.String getAgarform(){
        return agarform;
    }

    public void setAgarform(java.lang.String v){
        this.agarform = v;
    }

    public java.lang.String getVardform(){
        return vardform;
    }

    public void setVardform(java.lang.String v){
        this.vardform = v;
    }

    public java.lang.String getVerksamhet(){
        return verksamhet;
    }

    public void setVerksamhet(java.lang.String v){
        this.verksamhet = v;
    }

    public java.lang.String getSorteringskodProd(){
        return sorteringskodProd;
    }

    public void setSorteringskodProd(java.lang.String v){
        this.sorteringskodProd = v;
    }

    public java.lang.String getSorteringskodBest(){
        return sorteringskodBest;
    }

    public void setSorteringskodBest(java.lang.String v){
        this.sorteringskodBest = v;
    }

    public java.lang.String getBenamning(){
        return benamning;
    }

    public void setBenamning(java.lang.String v){
        this.benamning = v;
    }

    public java.lang.String getPostnr(){
        return postnr;
    }

    public void setPostnr(java.lang.String v){
        this.postnr = v;
    }

    public java.lang.String getPostort(){
        return postort;
    }

    public void setPostort(java.lang.String v){
        this.postort = v;
    }

    public java.lang.String getPostadress(){
        return postadress;
    }

    public void setPostadress(java.lang.String v){
        this.postadress = v;
    }

    public java.lang.String getLakemedkomm(){
        return lakemedkomm;
    }

    public void setLakemedkomm(java.lang.String v){
        this.lakemedkomm = v;
    }

    public java.lang.String getKontaktAkod(){
        return kontaktAkod;
    }

    public void setKontaktAkod(java.lang.String v){
        this.kontaktAkod = v;
    }

    public java.lang.Integer getLeverans(){
        return leverans;
    }

    public void setLeverans(java.lang.Integer v){
        this.leverans = v;
    }

    public java.lang.Integer getFakturering(){
        return fakturering;
    }

    public void setFakturering(java.lang.Integer v){
        this.fakturering = v;
    }

    public java.lang.String getAnmarkning(){
        return anmarkning;
    }

    public void setAnmarkning(java.lang.String v){
        this.anmarkning = v;
    }

    public java.sql.Timestamp getFromDatum(){
        return fromDatum;
    }

    public void setFromDatum(java.sql.Timestamp v){
        this.fromDatum = v;
    }

    public java.sql.Timestamp getTillDatum(){
        return tillDatum;
    }

    public void setTillDatum(java.sql.Timestamp v){
        this.tillDatum = v;
    }

    public java.sql.Timestamp getRegDatum(){
        return regDatum;
    }

    public void setRegDatum(java.sql.Timestamp v){
        this.regDatum = v;
    }

    public java.lang.String getErsattav(){
        return ersattav;
    }

    public void setErsattav(java.lang.String v){
        this.ersattav = v;
    }

    public java.lang.Integer getUserId(){
        return userId;
    }

    public void setUserId(java.lang.Integer v){
        this.userId = v;
    }

    public java.lang.String getArbetsplatskodlan(){
        return arbetsplatskodlan;
    }

    public void setArbetsplatskodlan(java.lang.String v){
        this.arbetsplatskodlan = v;
    }

    public java.lang.String getNamn(){
        return namn;
    }

    public void setNamn(java.lang.String v){
        this.namn = v;
    }

    public java.lang.String getAndringsdatum(){
        return andringsdatum;
    }

    public void setAndringsdatum(java.lang.String v){
        this.andringsdatum = v;
    }

    public java.lang.String getKommun(){
        return kommun;
    }

    public void setKommun(java.lang.String v){
        this.kommun = v;
    }

    public java.lang.String getSlask(){
        return slask;
    }

    public void setSlask(java.lang.String v){
        this.slask = v;
    }

    public java.lang.Boolean getApodos(){
        return apodos;
    }

    public void setApodos(java.lang.Boolean v){
        this.apodos = v;
    }

    public java.lang.String getExternfaktura(){
        return externfaktura;
    }

    public void setExternfaktura(java.lang.String v){
        this.externfaktura = v;
    }

    public java.lang.String getKommunkod(){
        return kommunkod;
    }

    public void setKommunkod(java.lang.String v){
        this.kommunkod = v;
    }

    public java.lang.String getExternfakturamodell(){
        return externfakturamodell;
    }

    public void setExternfakturamodell(java.lang.String v){
        this.externfakturamodell = v;
    }

    public java.lang.Boolean getVgpv(){
        return vgpv;
    }

    public void setVgpv(java.lang.Boolean v){
        this.vgpv = v;
    }

    public java.lang.String getHsaid(){
        return hsaid;
    }

    public void setHsaid(java.lang.String v){
        this.hsaid = v;
    }

    public java.lang.Integer getExpr1(){
        return expr1;
    }

    public void setExpr1(java.lang.Integer v){
        this.expr1 = v;
    }

    public java.lang.String getAo3id(){
        return ao3id;
    }

    public void setAo3id(java.lang.String v){
        this.ao3id = v;
    }

    public java.lang.String getForetagsnamn(){
        return foretagsnamn;
    }

    public void setForetagsnamn(java.lang.String v){
        this.foretagsnamn = v;
    }

    public java.lang.String getProducent(){
        return producent;
    }

    public void setProducent(java.lang.String v){
        this.producent = v;
    }

    public java.lang.String getKontaktperson(){
        return kontaktperson;
    }

    public void setKontaktperson(java.lang.String v){
        this.kontaktperson = v;
    }

    public java.lang.String getForetagsnr(){
        return foretagsnr;
    }

    public void setForetagsnr(java.lang.String v){
        this.foretagsnr = v;
    }

    public java.lang.Boolean getRaderad(){
        return raderad;
    }

    public void setRaderad(java.lang.Boolean v){
        this.raderad = v;
    }

    public Byte[] getExpr2(){
        return expr2;
    }

    public void setExpr2(Byte[] v){
        this.expr2 = v;
    }

    public Viewapkwithao3 loadData(Ao3 ao3Entity) {
        setExpr1(ao3Entity.getId());
        setAo3id(ao3Entity.getAo3id());
        setForetagsnamn(ao3Entity.getForetagsnamn());
        setProducent(null); // todo Probably don't need this?
        setKontaktperson(ao3Entity.getKontaktperson());
        setForetagsnr(ao3Entity.getForetagsnr());
        setRaderad(ao3Entity.getRaderad());
        return this;
    }

    public Viewapkwithao3 loadData(Data data) {
        setId(data.getId());
        setLankod(data.getLankod());
        setArbetsplatskod(data.getArbetsplatskod());
        setAo3(ao3);
        setAnsvar(data.getAnsvar());
        setFrivilligUppgift(data.getFrivilligUppgift());
        setAgarform(data.getAgarform());
        setVardform(data.getVardform());
        setVerksamhet(data.getVerksamhet());
        setSorteringskodProd(null); // todo Probably don't need this?
        setSorteringskodBest(null); // todo Probably don't need this?
        setBenamning(data.getBenamning());
        setPostnr(data.getPostnr());
        setPostort(data.getPostort());
        setPostadress(data.getPostadress());
        setLakemedkomm(null); // todo Probably don't need this? Or fixed value?
        setKontaktAkod(null); // todo Use this?
        setLeverans(null); // todo Probably don't need this?
        setFakturering(data.getFakturering());
        setAnmarkning(data.getAnmarkning());
        setFromDatum(data.getFromDatum());
        setTillDatum(data.getTillDatum());
        setRegDatum(data.getRegDatum());
        setErsattav(data.getErsattav());
        setUserId(data.getUserId()); // data.getUserIdNew() != null ? data.getUserIdNew() : data.getUserId() + "");
        setArbetsplatskodlan(data.getArbetsplatskodlan());
        setNamn(null); // todo Probably don't need this?
        setAndringsdatum(data.getAndringsdatum());
        setKommun(null); // todo Probably don't need this?
        setApodos(false); // todo Probably don't need this?
        setExternfaktura(data.getExternfaktura());
        setKommunkod(null); // todo Probably don't need this?
        setExternfakturamodell(data.getExternfakturamodell());
        setVgpv(data.getVgpv());
        return this;
    }

}