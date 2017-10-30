package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "leverans")
public class Leverans extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "leveransid", nullable = true)
    private java.lang.Integer leveransid;

    @Column (name = "leveranstext", nullable = true)
    private java.lang.String leveranstext;

    @Column (name = "raderad", nullable = false)
    private java.lang.Boolean raderad;

    @Deprecated @Transient // @Column (name = "ssma_timestamp", nullable = false)
    private Byte[] ssmaTimestamp;

    public java.lang.Integer getId(){
        return id;
    }

    public void setId(java.lang.Integer v){
        this.id = v;
    }

    public java.lang.Integer getLeveransid(){
        return leveransid;
    }

    public void setLeveransid(java.lang.Integer v){
        this.leveransid = v;
    }

    public java.lang.String getLeveranstext(){
        return leveranstext;
    }

    public void setLeveranstext(java.lang.String v){
        this.leveranstext = v;
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