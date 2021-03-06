package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

@Entity
@Table(name = "prodn1", indexes = {@Index(columnList = "kortnamn")})
public class Prodn1 {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "producentid", nullable = true)
    private java.lang.String producentid;

    @Column (name = "kortnamn", nullable = true)
    private java.lang.String kortnamn;

    @Column (name = "raderad", nullable = false)
    private java.lang.Boolean raderad;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Prodn1 prodn1 = (Prodn1) o;

        return id != null ? id.equals(prodn1.id) : prodn1.id == null;
    }

    @Override
    public int hashCode() {
        return 31 + (id != null ? id.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "Prodn1{" +
                "id=" + id +
                ", producentid='" + getProducentid() + '\'' +
                ", kortnamn='" + getKortnamn() + '\'' +
                ", raderad=" + getRaderad() +
                '}';
    }

}
