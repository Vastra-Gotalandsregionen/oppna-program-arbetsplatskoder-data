package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "ersatt_hsaid")
public class ErsattHsaid extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "lankod", nullable = true)
    private java.lang.Integer lankod;

    @Column (name = "arbetsplatskod", nullable = true)
    private java.lang.String arbetsplatskod;

    @Column (name = "benamning", nullable = true)
    private java.lang.String benamning;

    @Column (name = "arbetsplatskodlan", nullable = true)
    private java.lang.String arbetsplatskodlan;

    @Column (name = "vgpv", nullable = true)
    private java.lang.Integer vgpv;

    @Column (name = "hsaid", nullable = true)
    private java.lang.String hsaid;

    @Column (name = "nytt_hsaid", nullable = true)
    private java.lang.String nyttHsaid;

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

    public java.lang.String getBenamning(){
        return benamning;
    }

    public void setBenamning(java.lang.String v){
        this.benamning = v;
    }

    public java.lang.String getArbetsplatskodlan(){
        return arbetsplatskodlan;
    }

    public void setArbetsplatskodlan(java.lang.String v){
        this.arbetsplatskodlan = v;
    }

    public java.lang.Integer getVgpv(){
        return vgpv;
    }

    public void setVgpv(java.lang.Integer v){
        this.vgpv = v;
    }

    public java.lang.String getHsaid(){
        return hsaid;
    }

    public void setHsaid(java.lang.String v){
        this.hsaid = v;
    }

    public java.lang.String getNyttHsaid(){
        return nyttHsaid;
    }

    public void setNyttHsaid(java.lang.String v){
        this.nyttHsaid = v;
    }


}