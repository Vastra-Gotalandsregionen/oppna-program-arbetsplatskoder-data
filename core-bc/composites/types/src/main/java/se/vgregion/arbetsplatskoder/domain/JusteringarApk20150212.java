package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "justeringar_apk_20150212")
public class JusteringarApk20150212 extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column (name = "arbetsplatskodlan", nullable = true)
    private java.lang.String arbetsplatskodlan;

    @Column (name = "ao3", nullable = true)
    private java.lang.String ao3;

    @Column (name = "ansvar", nullable = true)
    private java.lang.String ansvar;
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

    public java.lang.String getAo3(){
        return ao3;
    }

    public void setAo3(java.lang.String v){
        this.ao3 = v;
    }

    public java.lang.String getAnsvar(){
        return ansvar;
    }

    public void setAnsvar(java.lang.String v){
        this.ansvar = v;
    }


}