package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

@Entity
@Table(name = "alla_apk_608_20150121")
public class AllaApk60820150121 extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column (name = "lan_arbetsplatskod", nullable = true)
    private java.lang.String lanArbetsplatskod;

    @Column (name = "ao3", nullable = true)
    private java.lang.String ao3;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public java.lang.String getLanArbetsplatskod(){
        return lanArbetsplatskod;
    }

    public void setLanArbetsplatskod(java.lang.String v){
        this.lanArbetsplatskod = v;
    }

    public java.lang.String getAo3(){
        return ao3;
    }

    public void setAo3(java.lang.String v){
        this.ao3 = v;
    }


}