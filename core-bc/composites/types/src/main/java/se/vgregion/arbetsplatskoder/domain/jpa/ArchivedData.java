package se.vgregion.arbetsplatskoder.domain.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.AbstractEntity;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn3;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "archived_data", indexes = {
        @Index(columnList = "benamning"),
        @Index(columnList = "prodn1"),
        @Index(columnList = "replacer", unique = false)
})
public class ArchivedData extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column (name = "lankod", nullable = true)
    private Integer lankod;

    @Column (name = "arbetsplatskod", nullable = true)
    private String arbetsplatskod;

    @Column (name = "ao3", nullable = true)
    private String ao3;

    @Column (name = "ansvar", nullable = true)
    private String ansvar;

    @Column (name = "frivillig_uppgift", nullable = true)
    private String frivilligUppgift;

    @Column (name = "agarform", nullable = true)
    private String agarform;

    @Column (name = "vardform", nullable = true)
    private String vardform;

    @Column (name = "verksamhet", nullable = true)
    private String verksamhet;

    @Column (name = "sorteringskod_prod", nullable = true)
    private String sorteringskodProd;

    @Column (name = "sorteringskod_best", nullable = true)
    private String sorteringskodBest;

    @JoinColumn(name = "prodn1")
    @ManyToOne(fetch = FetchType.EAGER)
    private Prodn1 prodn1;

    @JoinColumn(name = "prodn3")
    @ManyToOne(fetch = FetchType.EAGER)
    private Prodn3 prodn3;

    @Column (name = "benamning", nullable = true)
    private String benamning;

    @Column (name = "benamning_kort", nullable = true, length = 35)
    private String benamningKort;

    @Column (name = "postnr", nullable = true)
    private String postnr;

    @Column (name = "postort", nullable = true)
    private String postort;

    @Column (name = "postadress", nullable = true)
    private String postadress;

    @Column (name = "lakemedkomm", nullable = true)
    private String lakemedkomm;

    @Column (name = "kontakt_akod", nullable = true)
    private String kontaktAkod;

    @Column (name = "leverans", nullable = true)
    private Integer leverans;

    @Column (name = "fakturering", nullable = true)
    private Integer fakturering;

    @Column (name = "anmarkning", nullable = true)
    private String anmarkning;

    @JsonFormat(pattern="yyyy-MM-dd", timezone = "Europe/Stockholm")
    @Column (name = "from_datum", nullable = true)
    private java.sql.Timestamp fromDatum;

    @JsonFormat(pattern="yyyy-MM-dd", timezone = "Europe/Stockholm")
    @Column (name = "till_datum", nullable = true)
    private java.sql.Timestamp tillDatum;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = "Europe/Stockholm")
    @Column (name = "reg_datum", nullable = true)
    private java.sql.Timestamp regDatum;

    @Column (name = "ersattav", nullable = true)
    private String ersattav;

    @Column (name = "user_id", nullable = true)
    private Integer userId;

    @Column (name = "arbetsplatskodlan", nullable = true)
    private String arbetsplatskodlan;

    @Column (name = "namn", nullable = true)
    private String namn;

    @Column (name = "andringsdatum", nullable = true)
    private String andringsdatum;

    @Column (name = "kommun", nullable = true)
    private String kommun;

    @Column (name = "slask", nullable = true)
    private String slask;

    @Column (name = "apodos", nullable = true)
    private Boolean apodos;

    @Column (name = "externfaktura", nullable = true)
    private String externfaktura;

    @Column (name = "kommunkod", nullable = true)
    private String kommunkod;

    @Column (name = "externfakturamodell", nullable = true)
    private String externfakturamodell;

    @Column (name = "ssma_timestamp", nullable = false)
    private Byte[] ssmaTimestamp;

    @Column (name = "vgpv", nullable = true)
    private Boolean vgpv;

    @Column (name = "hsaid", nullable = true)
    private String hsaid;

    @Column
    private String userIdNew;

    @Column
    private Boolean deletable;

    @Column
    private Boolean groupCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replacer")
    @JsonIgnore
    private Data replacer;

    public Integer getId(){
        return id;
    }

    public void setId(Integer v){
        this.id = v;
    }

    public Integer getLankod(){
        return lankod;
    }

    public void setLankod(Integer v){
        this.lankod = v;
    }

    public String getArbetsplatskod(){
        return arbetsplatskod;
    }

    public void setArbetsplatskod(String v){
        this.arbetsplatskod = v;
    }

    public String getAo3(){
        return ao3;
    }

    public void setAo3(String v){
        this.ao3 = v;
    }

    public String getAnsvar(){
        return ansvar;
    }

    public void setAnsvar(String v){
        this.ansvar = v;
    }

    public String getFrivilligUppgift(){
        return frivilligUppgift;
    }

    public void setFrivilligUppgift(String v){
        this.frivilligUppgift = v;
    }

    public String getAgarform(){
        return agarform;
    }

    public void setAgarform(String v){
        this.agarform = v;
    }

    public String getVardform(){
        return vardform;
    }

    public void setVardform(String v){
        this.vardform = v;
    }

    public String getVerksamhet(){
        return verksamhet;
    }

    public void setVerksamhet(String v){
        this.verksamhet = v;
    }

    public String getSorteringskodProd(){
        return sorteringskodProd;
    }

    public void setSorteringskodProd(String v){
        this.sorteringskodProd = v;
    }

    public Prodn1 getProdn1() {
        return prodn1;
    }

    public void setProdn1(Prodn1 prodn1) {
        this.prodn1 = prodn1;
    }

    public Prodn3 getProdn3() {
        return prodn3;
    }

    public void setProdn3(Prodn3 prodn3) {
        this.prodn3 = prodn3;
    }

    public String getSorteringskodBest(){
        return sorteringskodBest;
    }

    public void setSorteringskodBest(String v){
        this.sorteringskodBest = v;
    }

    public String getBenamning(){
        return benamning;
    }

    public void setBenamning(String v){
        this.benamning = v;
    }

    public String getBenamningKort() {
        return benamningKort;
    }

    public void setBenamningKort(String benamningKort) {
        this.benamningKort = benamningKort;
    }

    public String getPostnr(){
        return postnr;
    }

    public void setPostnr(String v){
        this.postnr = v;
    }

    public String getPostort(){
        return postort;
    }

    public void setPostort(String v){
        this.postort = v;
    }

    public String getPostadress(){
        return postadress;
    }

    public void setPostadress(String v){
        this.postadress = v;
    }

    public String getLakemedkomm(){
        return lakemedkomm;
    }

    public void setLakemedkomm(String v){
        this.lakemedkomm = v;
    }

    public String getKontaktAkod(){
        return kontaktAkod;
    }

    public void setKontaktAkod(String v){
        this.kontaktAkod = v;
    }

    public Integer getLeverans(){
        return leverans;
    }

    public void setLeverans(Integer v){
        this.leverans = v;
    }

    public Integer getFakturering(){
        return fakturering;
    }

    public void setFakturering(Integer v){
        this.fakturering = v;
    }

    public String getAnmarkning(){
        return anmarkning;
    }

    public void setAnmarkning(String v){
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

    public String getErsattav(){
        return ersattav;
    }

    public void setErsattav(String v){
        this.ersattav = v;
    }

    public Integer getUserId(){
        return userId;
    }

    public void setUserId(Integer v){
        this.userId = v;
    }

    public String getArbetsplatskodlan(){
        return arbetsplatskodlan;
    }

    public void setArbetsplatskodlan(String v){
        this.arbetsplatskodlan = v;
    }

    public String getNamn(){
        return namn;
    }

    public void setNamn(String v){
        this.namn = v;
    }

    public String getAndringsdatum(){
        return andringsdatum;
    }

    public void setAndringsdatum(String v){
        this.andringsdatum = v;
    }

    public String getKommun(){
        return kommun;
    }

    public void setKommun(String v){
        this.kommun = v;
    }

    public String getSlask(){
        return slask;
    }

    public void setSlask(String v){
        this.slask = v;
    }

    public Boolean getApodos(){
        return apodos;
    }

    public void setApodos(Boolean v){
        this.apodos = v;
    }

    public String getExternfaktura(){
        return externfaktura;
    }

    public void setExternfaktura(String v){
        this.externfaktura = v;
    }

    public String getKommunkod(){
        return kommunkod;
    }

    public void setKommunkod(String v){
        this.kommunkod = v;
    }

    public String getExternfakturamodell(){
        return externfakturamodell;
    }

    public void setExternfakturamodell(String v){
        this.externfakturamodell = v;
    }

    public Byte[] getSsmaTimestamp(){
        return ssmaTimestamp;
    }

    public void setSsmaTimestamp(Byte[] v){
        this.ssmaTimestamp = v;
    }

    public Boolean getVgpv(){
        return vgpv;
    }

    public void setVgpv(Boolean v){
        this.vgpv = v;
    }

    public String getHsaid(){
        return hsaid;
    }

    public void setHsaid(String v){
        this.hsaid = v;
    }


    public void setUserIdNew(String userIdNew) {
        this.userIdNew = userIdNew;
    }

    public String getUserIdNew() {
        return userIdNew;
    }

    public Boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }

    public Boolean getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(Boolean groupCode) {
        this.groupCode = groupCode;
    }

    public Data getReplacer() {
        return replacer;
    }

    public void setReplacer(Data replacer) {
        this.replacer = replacer;
    }
}