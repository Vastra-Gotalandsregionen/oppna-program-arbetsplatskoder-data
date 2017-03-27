package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "verksamhet")
public class Verksamhet extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "verksamhetid", nullable = true)
    private java.lang.String verksamhetid;

    @Column (name = "verksamhettext", nullable = true)
    private java.lang.String verksamhettext;

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

    public java.lang.String getVerksamhetid(){
        return verksamhetid;
    }

    public void setVerksamhetid(java.lang.String v){
        this.verksamhetid = v;
    }

    public java.lang.String getVerksamhettext(){
        return verksamhettext;
    }

    public void setVerksamhettext(java.lang.String v){
        this.verksamhettext = v;
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