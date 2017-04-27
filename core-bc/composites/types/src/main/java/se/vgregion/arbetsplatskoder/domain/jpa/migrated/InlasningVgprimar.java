package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "inlasning_vgprimar")
public class InlasningVgprimar extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column (name = "lankod", nullable = true)
    private java.lang.Double lankod;

    @Column (name = "arbetsplatskod", nullable = true)
    private java.lang.Double arbetsplatskod;

    @Column (name = "ao3", nullable = true)
    private java.lang.Double ao3;

    @Column (name = "ansvar", nullable = true)
    private java.lang.Double ansvar;

    @Column (name = "frivillig_uppgift", nullable = true)
    private java.lang.String frivilligUppgift;

    @Column (name = "agarform", nullable = true)
    private java.lang.Double agarform;

    @Column (name = "vardform", nullable = true)
    private java.lang.String vardform;

    @Column (name = "verksamhet", nullable = true, length = 3)
    private java.lang.String verksamhet;

    @Column (name = "sorteringskod_prod", nullable = true)
    private java.lang.String sorteringskodProd;

    @Column (name = "sorteringskod_best", nullable = true)
    private java.lang.String sorteringskodBest;

    @Column (name = "benamning", nullable = true)
    private java.lang.String benamning;

    @Column (name = "postnr", nullable = true)
    private java.lang.Double postnr;

    @Column (name = "postort", nullable = true)
    private java.lang.String postort;

    @Column (name = "postadress", nullable = true)
    private java.lang.String postadress;

    @Column (name = "lakemedkomm", nullable = true)
    private java.lang.String lakemedkomm;

    @Column (name = "kontakt_akod", nullable = true)
    private java.lang.String kontaktAkod;

    @Column (name = "leverans", nullable = true)
    private java.lang.String leverans;

    @Column (name = "fakturering", nullable = true)
    private java.lang.String fakturering;

    @Column (name = "anmarkning", nullable = true)
    private java.lang.String anmarkning;

    @Column (name = "from_datum", nullable = true)
    private java.sql.Timestamp fromDatum;

    @Column (name = "till_datum", nullable = true)
    private java.lang.String tillDatum;

    @Column (name = "reg_datum", nullable = true)
    private java.lang.String regDatum;

    @Column (name = "ersattav", nullable = true)
    private java.lang.String ersattav;

    @Column (name = "user_id", nullable = true)
    private java.lang.String userId;

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
    private java.lang.String apodos;

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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public java.lang.Double getLankod(){
        return lankod;
    }

    public void setLankod(java.lang.Double v){
        this.lankod = v;
    }

    public java.lang.Double getArbetsplatskod(){
        return arbetsplatskod;
    }

    public void setArbetsplatskod(java.lang.Double v){
        this.arbetsplatskod = v;
    }

    public java.lang.Double getAo3(){
        return ao3;
    }

    public void setAo3(java.lang.Double v){
        this.ao3 = v;
    }

    public java.lang.Double getAnsvar(){
        return ansvar;
    }

    public void setAnsvar(java.lang.Double v){
        this.ansvar = v;
    }

    public java.lang.String getFrivilligUppgift(){
        return frivilligUppgift;
    }

    public void setFrivilligUppgift(java.lang.String v){
        this.frivilligUppgift = v;
    }

    public java.lang.Double getAgarform(){
        return agarform;
    }

    public void setAgarform(java.lang.Double v){
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

    public java.lang.Double getPostnr(){
        return postnr;
    }

    public void setPostnr(java.lang.Double v){
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

    public java.lang.String getLeverans(){
        return leverans;
    }

    public void setLeverans(java.lang.String v){
        this.leverans = v;
    }

    public java.lang.String getFakturering(){
        return fakturering;
    }

    public void setFakturering(java.lang.String v){
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

    public java.lang.String getTillDatum(){
        return tillDatum;
    }

    public void setTillDatum(java.lang.String v){
        this.tillDatum = v;
    }

    public java.lang.String getRegDatum(){
        return regDatum;
    }

    public void setRegDatum(java.lang.String v){
        this.regDatum = v;
    }

    public java.lang.String getErsattav(){
        return ersattav;
    }

    public void setErsattav(java.lang.String v){
        this.ersattav = v;
    }

    public java.lang.String getUserId(){
        return userId;
    }

    public void setUserId(java.lang.String v){
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

    public java.lang.String getApodos(){
        return apodos;
    }

    public void setApodos(java.lang.String v){
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


}