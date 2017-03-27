package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "agarform")
public class Agarform extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "agarformid", nullable = true)
    private java.lang.Integer agarformid;

    @Column (name = "agarformtext", nullable = true)
    private java.lang.String agarformtext;

    @Column (name = "raderad", nullable = false)
    private java.lang.Boolean raderad;

    @Column (name = "ssma_timestamp", nullable = false)
    private Byte[] ssmaTimestamp;

    public java.lang.Integer getId(){
        return id;
    }

    public void setId(java.lang.Integer v){
        this.id = v;
    }

    public java.lang.Integer getAgarformid(){
        return agarformid;
    }

    public void setAgarformid(java.lang.Integer v){
        this.agarformid = v;
    }

    public java.lang.String getAgarformtext(){
        return agarformtext;
    }

    public void setAgarformtext(java.lang.String v){
        this.agarformtext = v;
    }

    public java.lang.Boolean getRaderad(){
        return raderad;
    }

    public void setRaderad(java.lang.Boolean v){
        this.raderad = v;
    }

    public Byte[] getSsmaTimestamp(){
        return ssmaTimestamp;
    }

    public void setSsmaTimestamp(Byte[] v){
        this.ssmaTimestamp = v;
    }


}