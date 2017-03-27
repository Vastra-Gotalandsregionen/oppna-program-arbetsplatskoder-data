package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "changejourcentraler")
public class Changejourcentraler extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column (name = "namn", nullable = true)
    private java.lang.String namn;

    @Column (name = "hsaid", nullable = true)
    private java.lang.String hsaid;

    @Column (name = "vckod", nullable = true)
    private java.lang.String vckod;

    @Column (name = "arbetsplatskodlan", nullable = true)
    private java.lang.String arbetsplatskodlan;

    @Column (name = "vgpv", nullable = true)
    private java.lang.String vgpv;

    @Column (name = "offentligprivat", nullable = true)
    private java.lang.String offentligprivat;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public java.lang.String getNamn(){
        return namn;
    }

    public void setNamn(java.lang.String v){
        this.namn = v;
    }

    public java.lang.String getHsaid(){
        return hsaid;
    }

    public void setHsaid(java.lang.String v){
        this.hsaid = v;
    }

    public java.lang.String getVckod(){
        return vckod;
    }

    public void setVckod(java.lang.String v){
        this.vckod = v;
    }

    public java.lang.String getArbetsplatskodlan(){
        return arbetsplatskodlan;
    }

    public void setArbetsplatskodlan(java.lang.String v){
        this.arbetsplatskodlan = v;
    }

    public java.lang.String getVgpv(){
        return vgpv;
    }

    public void setVgpv(java.lang.String v){
        this.vgpv = v;
    }

    public java.lang.String getOffentligprivat(){
        return offentligprivat;
    }

    public void setOffentligprivat(java.lang.String v){
        this.offentligprivat = v;
    }


}