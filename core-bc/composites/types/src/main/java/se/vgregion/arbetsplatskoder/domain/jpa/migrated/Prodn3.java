package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "prodn3")
public class Prodn3 extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "producentid", nullable = true)
    private java.lang.String producentid;

    @Column (name = "kortnamn", nullable = true)
    private java.lang.String kortnamn;

    @Column (name = "raderad", nullable = false)
    private java.lang.Boolean raderad;

    @Column (name = "n2", nullable = true)
    private java.lang.String n2;

    @JoinColumn(name = "prodn2")
    @ManyToOne(fetch = FetchType.EAGER)
    private Prodn2 prodn2;

    @Column (name = "autoradering", nullable = true)
    private java.lang.Boolean autoradering;

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

    public java.lang.String getN2(){
        return n2;
    }

    public void setN2(java.lang.String v){
        this.n2 = v;
    }

    public Prodn2 getProdn2() {
        return prodn2;
    }

    public void setProdn2(Prodn2 prodn2) {
        this.prodn2 = prodn2;
    }

    public java.lang.Boolean getAutoradering(){
        return autoradering;
    }

    public void setAutoradering(java.lang.Boolean v){
        this.autoradering = v;
    }
}