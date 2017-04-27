package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "viewapk_hsaid")
public class ViewapkHsaid extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column (name = "arbetsplatskodlan", nullable = true)
    private java.lang.String arbetsplatskodlan;

    @Column (name = "hsaid", nullable = true)
    private java.lang.String hsaid;

    @Column (name = "from_datum", nullable = true)
    private java.sql.Timestamp fromDatum;

    @Column (name = "till_datum", nullable = true)
    private java.sql.Timestamp tillDatum;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public java.lang.String getArbetsplatskodlan(){
        return arbetsplatskodlan;
    }

    public void setArbetsplatskodlan(java.lang.String v){
        this.arbetsplatskodlan = v;
    }

    public java.lang.String getHsaid(){
        return hsaid;
    }

    public void setHsaid(java.lang.String v){
        this.hsaid = v;
    }

    public java.sql.Timestamp getFromDatum(){
        return fromDatum;
    }

    public void setFromDatum(java.sql.Timestamp v){
        this.fromDatum = v;
    }

    public java.sql.Timestamp getTillDatum(){
        return tillDatum;
    }

    public void setTillDatum(java.sql.Timestamp v){
        this.tillDatum = v;
    }


}