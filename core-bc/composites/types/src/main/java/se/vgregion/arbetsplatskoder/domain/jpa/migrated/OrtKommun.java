package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "ort_kommun")
public class OrtKommun extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column (name = "kommkod", nullable = true)
    private java.lang.String kommkod;

    @Column (name = "ort_kommun", nullable = true)
    private java.lang.String ortKommun;

    @Column (name = "alias", nullable = true)
    private java.lang.String alias;

    @Column (name = "ortid", nullable = false)
    private java.lang.Integer ortid;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public java.lang.String getKommkod(){
        return kommkod;
    }

    public void setKommkod(java.lang.String v){
        this.kommkod = v;
    }

    public java.lang.String getOrtKommun(){
        return ortKommun;
    }

    public void setOrtKommun(java.lang.String v){
        this.ortKommun = v;
    }

    public java.lang.String getAlias(){
        return alias;
    }

    public void setAlias(java.lang.String v){
        this.alias = v;
    }

    public java.lang.Integer getOrtid(){
        return ortid;
    }

    public void setOrtid(java.lang.Integer v){
        this.ortid = v;
    }


}