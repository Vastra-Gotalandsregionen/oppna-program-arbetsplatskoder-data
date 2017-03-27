package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "lk")
public class Lk extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "lkid", nullable = true)
    private java.lang.String lkid;

    @Column (name = "lktext", nullable = true)
    private java.lang.String lktext;

    @Column (name = "raderad", nullable = true)
    private java.lang.Boolean raderad;

    @Column (name = "kommunkod", nullable = true)
    private java.lang.String kommunkod;

    @Column (name = "ssma_timestamp", nullable = false)
    private Byte[] ssmaTimestamp;

    public java.lang.Integer getId(){
        return id;
    }

    public void setId(java.lang.Integer v){
        this.id = v;
    }

    public java.lang.String getLkid(){
        return lkid;
    }

    public void setLkid(java.lang.String v){
        this.lkid = v;
    }

    public java.lang.String getLktext(){
        return lktext;
    }

    public void setLktext(java.lang.String v){
        this.lktext = v;
    }

    public java.lang.Boolean getRaderad(){
        return raderad;
    }

    public void setRaderad(java.lang.Boolean v){
        this.raderad = v;
    }

    public java.lang.String getKommunkod(){
        return kommunkod;
    }

    public void setKommunkod(java.lang.String v){
        this.kommunkod = v;
    }

    public Byte[] getSsmaTimestamp(){
        return ssmaTimestamp;
    }

    public void setSsmaTimestamp(Byte[] v){
        this.ssmaTimestamp = v;
    }


}