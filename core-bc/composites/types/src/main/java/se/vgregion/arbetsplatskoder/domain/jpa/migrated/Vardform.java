package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "vardform")
public class Vardform extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "vardformid", nullable = true)
    private java.lang.String vardformid;

    @Column (name = "vardformtext", nullable = true)
    private java.lang.String vardformtext;

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

    public java.lang.String getVardformid(){
        return vardformid;
    }

    public void setVardformid(java.lang.String v){
        this.vardformid = v;
    }

    public java.lang.String getVardformtext(){
        return vardformtext;
    }

    public void setVardformtext(java.lang.String v){
        this.vardformtext = v;
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