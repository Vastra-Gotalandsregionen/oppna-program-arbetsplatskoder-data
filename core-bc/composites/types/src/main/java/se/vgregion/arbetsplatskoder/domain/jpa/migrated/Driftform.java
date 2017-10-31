package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "driftform")
public class Driftform extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "driftform", nullable = true)
    private java.lang.Integer driftform;

    @Column (name = "driftformtext", nullable = true)
    private java.lang.String driftformtext;

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

    public java.lang.Integer getDriftform(){
        return driftform;
    }

    public void setDriftform(java.lang.Integer v){
        this.driftform = v;
    }

    public java.lang.String getDriftformtext(){
        return driftformtext;
    }

    public void setDriftformtext(java.lang.String v){
        this.driftformtext = v;
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