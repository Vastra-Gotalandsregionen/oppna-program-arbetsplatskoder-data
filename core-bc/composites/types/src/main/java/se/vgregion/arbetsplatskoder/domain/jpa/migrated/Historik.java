package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "historik")
public class Historik extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "data_id", nullable = true)
    private java.lang.Integer dataId;

    @Column (name = "lankod", nullable = true)
    private java.lang.String lankod;

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

    @Column (name = "benamning", nullable = true)
    private java.lang.String benamning;

    @Column (name = "postnr", nullable = true)
    private java.lang.String postnr;

    @Column (name = "postort", nullable = true)
    private java.lang.String postort;

    @Column (name = "postadress", nullable = true)
    private java.lang.String postadress;

    @Column (name = "sorteringskod_best", nullable = true)
    private java.lang.String sorteringskodBest;

    @Column (name = "from_datum", nullable = true)
    private java.lang.String fromDatum;

    @Column (name = "till_datum", nullable = true)
    private java.lang.String tillDatum;

    @Column (name = "andring_datum", nullable = true)
    private java.sql.Timestamp andringDatum;

    @Column (name = "andring_kod", nullable = true)
    private java.lang.String andringKod;

    @Column (name = "user_id", nullable = true)
    private java.lang.String userId;

    @Column (name = "kontakt_akod", nullable = true)
    private java.lang.String kontaktAkod;

    @Column (name = "receptdistribution", nullable = true)
    private java.lang.String receptdistribution;

    @Column (name = "leverans", nullable = true)
    private java.lang.String leverans;

    @Column (name = "fakturering", nullable = true)
    private java.lang.String fakturering;

    @Column (name = "anmarkning", nullable = true)
    private java.lang.String anmarkning;

    @Column (name = "ersattav", nullable = true)
    private java.lang.String ersattav;

    @Column (name = "lakemedkomm", nullable = true)
    private java.lang.String lakemedkomm;

    @Column (name = "apodos", nullable = true)
    private java.lang.String apodos;

    @Column (name = "externfaktura", nullable = true)
    private java.lang.String externfaktura;

    @Column (name = "externfakturamodell", nullable = true)
    private java.lang.String externfakturamodell;

    @Deprecated @Transient // @Column (name = "ssma_timestamp", nullable = false)
    private Byte[] ssmaTimestamp;

    public java.lang.Integer getId(){
        return id;
    }

    public void setId(java.lang.Integer v){
        this.id = v;
    }

    public java.lang.Integer getDataId(){
        return dataId;
    }

    public void setDataId(java.lang.Integer v){
        this.dataId = v;
    }

    public java.lang.String getLankod(){
        return lankod;
    }

    public void setLankod(java.lang.String v){
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

    public java.lang.String getSorteringskodBest(){
        return sorteringskodBest;
    }

    public void setSorteringskodBest(java.lang.String v){
        this.sorteringskodBest = v;
    }

    public java.lang.String getFromDatum(){
        return fromDatum;
    }

    public void setFromDatum(java.lang.String v){
        this.fromDatum = v;
    }

    public java.lang.String getTillDatum(){
        return tillDatum;
    }

    public void setTillDatum(java.lang.String v){
        this.tillDatum = v;
    }

    public java.sql.Timestamp getAndringDatum(){
        return andringDatum;
    }

    public void setAndringDatum(java.sql.Timestamp v){
        this.andringDatum = v;
    }

    public java.lang.String getAndringKod(){
        return andringKod;
    }

    public void setAndringKod(java.lang.String v){
        this.andringKod = v;
    }

    public java.lang.String getUserId(){
        return userId;
    }

    public void setUserId(java.lang.String v){
        this.userId = v;
    }

    public java.lang.String getKontaktAkod(){
        return kontaktAkod;
    }

    public void setKontaktAkod(java.lang.String v){
        this.kontaktAkod = v;
    }

    public java.lang.String getReceptdistribution(){
        return receptdistribution;
    }

    public void setReceptdistribution(java.lang.String v){
        this.receptdistribution = v;
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

    public java.lang.String getErsattav(){
        return ersattav;
    }

    public void setErsattav(java.lang.String v){
        this.ersattav = v;
    }

    public java.lang.String getLakemedkomm(){
        return lakemedkomm;
    }

    public void setLakemedkomm(java.lang.String v){
        this.lakemedkomm = v;
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


}