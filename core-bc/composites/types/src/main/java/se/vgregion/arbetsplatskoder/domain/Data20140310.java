package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "data_20140310")
public class Data20140310 extends AbstractEntity {

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

    @Column (name = "anmarkning", nullable = true)
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

    @Column (name = "ssma_timestamp", nullable = false)
    private Byte[] ssmaTimestamp;

    @Column (name = "vgpv", nullable = true)
    private java.lang.Boolean vgpv;

    @Column (name = "hsaid", nullable = true)
    private java.lang.String hsaid;

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

    public Byte[] getSsmaTimestamp(){
        return ssmaTimestamp;
    }

    public void setSsmaTimestamp(Byte[] v){
        this.ssmaTimestamp = v;
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


}