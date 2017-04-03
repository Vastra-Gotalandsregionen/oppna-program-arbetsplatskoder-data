package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "alla_apk_608_20150121")
public class AllaApk60820150121 extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column (name = "lÄn_arbetsplatskod", nullable = true)
    private java.lang.String länArbetsplatskod;

    @Column (name = "ao3", nullable = true)
    private java.lang.String ao3;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public java.lang.String getLänArbetsplatskod(){
        return länArbetsplatskod;
    }

    public void setLänArbetsplatskod(java.lang.String v){
        this.länArbetsplatskod = v;
    }

    public java.lang.String getAo3(){
        return ao3;
    }

    public void setAo3(java.lang.String v){
        this.ao3 = v;
    }


}