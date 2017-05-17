package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "prodn2")
public class Prodn2 extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "producentid", nullable = true)
    private java.lang.String producentid;

    @Column (name = "avdelning", nullable = true)
    private java.lang.String avdelning;

    @Column (name = "kortnamn", nullable = true)
    private java.lang.String kortnamn;

    @Column (name = "raderad", nullable = false)
    private java.lang.Boolean raderad;

    @Column (name = "n1", nullable = true)
    private java.lang.String n1;

    @JoinColumn(name = "prodn1")
    @ManyToOne(fetch = FetchType.EAGER)
    private Prodn1 prodn1;

    @Column (name = "autoradering", nullable = true)
    private java.lang.String autoradering;

    @Column (name = "riktvarde", nullable = true)
    private java.lang.String riktvarde;

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

    public java.lang.String getAvdelning(){
        return avdelning;
    }

    public void setAvdelning(java.lang.String v){
        this.avdelning = v;
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

    public java.lang.String getN1(){
        return n1;
    }

    public void setN1(java.lang.String v){
        this.n1 = v;
    }

    public Prodn1 getProdn1() {
        return prodn1;
    }

    public void setProdn1(Prodn1 prodn1) {
        this.prodn1 = prodn1;
    }

    public java.lang.String getAutoradering(){
        return autoradering;
    }

    public void setAutoradering(java.lang.String v){
        this.autoradering = v;
    }

    public java.lang.String getRiktvarde(){
        return riktvarde;
    }

    public void setRiktvarde(java.lang.String v){
        this.riktvarde = v;
    }

    public Byte[] getSsmaTimestamp(){
        return ssmaTimestamp;
    }

    public void setSsmaTimestamp(Byte[] v){
        this.ssmaTimestamp = v;
    }


}