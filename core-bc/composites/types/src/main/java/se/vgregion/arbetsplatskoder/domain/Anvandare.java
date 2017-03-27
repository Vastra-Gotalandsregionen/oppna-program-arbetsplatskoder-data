package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "anvandare")
public class Anvandare extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "uid", nullable = true)
    private java.lang.String uid;

    @Column (name = "pwd", nullable = true)
    private java.lang.String pwd;

    @Column (name = "fnamn", nullable = true)
    private java.lang.String fnamn;

    @Column (name = "enamn", nullable = true)
    private java.lang.String enamn;

    @Column (name = "email", nullable = true)
    private java.lang.String email;

    @Column (name = "adress", nullable = true)
    private java.lang.String adress;

    @Column (name = "postadress", nullable = true)
    private java.lang.String postadress;

    @Column (name = "postort", nullable = true)
    private java.lang.String postort;

    @Column (name = "tel", nullable = true)
    private java.lang.String tel;

    @Column (name = "mobil", nullable = true)
    private java.lang.String mobil;

    @Column (name = "fax", nullable = true)
    private java.lang.String fax;

    @Column (name = "behorighet", nullable = true)
    private java.lang.Integer behorighet;

    @Column (name = "sorteringskod_prod", nullable = true)
    private java.lang.String sorteringskodProd;

    @Column (name = "hashpwd", nullable = true)
    private java.lang.String hashpwd;

    @Column (name = "father", nullable = true)
    private java.lang.String father;

    public java.lang.Integer getId(){
        return id;
    }

    public void setId(java.lang.Integer v){
        this.id = v;
    }

    public java.lang.String getUid(){
        return uid;
    }

    public void setUid(java.lang.String v){
        this.uid = v;
    }

    public java.lang.String getPwd(){
        return pwd;
    }

    public void setPwd(java.lang.String v){
        this.pwd = v;
    }

    public java.lang.String getFnamn(){
        return fnamn;
    }

    public void setFnamn(java.lang.String v){
        this.fnamn = v;
    }

    public java.lang.String getEnamn(){
        return enamn;
    }

    public void setEnamn(java.lang.String v){
        this.enamn = v;
    }

    public java.lang.String getEmail(){
        return email;
    }

    public void setEmail(java.lang.String v){
        this.email = v;
    }

    public java.lang.String getAdress(){
        return adress;
    }

    public void setAdress(java.lang.String v){
        this.adress = v;
    }

    public java.lang.String getPostadress(){
        return postadress;
    }

    public void setPostadress(java.lang.String v){
        this.postadress = v;
    }

    public java.lang.String getPostort(){
        return postort;
    }

    public void setPostort(java.lang.String v){
        this.postort = v;
    }

    public java.lang.String getTel(){
        return tel;
    }

    public void setTel(java.lang.String v){
        this.tel = v;
    }

    public java.lang.String getMobil(){
        return mobil;
    }

    public void setMobil(java.lang.String v){
        this.mobil = v;
    }

    public java.lang.String getFax(){
        return fax;
    }

    public void setFax(java.lang.String v){
        this.fax = v;
    }

    public java.lang.Integer getBehorighet(){
        return behorighet;
    }

    public void setBehorighet(java.lang.Integer v){
        this.behorighet = v;
    }

    public java.lang.String getSorteringskodProd(){
        return sorteringskodProd;
    }

    public void setSorteringskodProd(java.lang.String v){
        this.sorteringskodProd = v;
    }

    public java.lang.String getHashpwd(){
        return hashpwd;
    }

    public void setHashpwd(java.lang.String v){
        this.hashpwd = v;
    }

    public java.lang.String getFather(){
        return father;
    }

    public void setFather(java.lang.String v){
        this.father = v;
    }


}