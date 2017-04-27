package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "prodn1")
public class Prodn1 extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "producentid", nullable = true)
    private java.lang.String producentid;

    @Column (name = "foretagsnamn", nullable = true)
    private java.lang.String foretagsnamn;

    @Column (name = "kortnamn", nullable = true)
    private java.lang.String kortnamn;

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

    public java.lang.String getProducentid(){
        return producentid;
    }

    public void setProducentid(java.lang.String v){
        this.producentid = v;
    }

    public java.lang.String getForetagsnamn(){
        return foretagsnamn;
    }

    public void setForetagsnamn(java.lang.String v){
        this.foretagsnamn = v;
    }

    public java.lang.String getKortnamn(){
        return kortnamn;
    }

    public void setKortnamn(java.lang.String v){
        this.kortnamn = v;
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