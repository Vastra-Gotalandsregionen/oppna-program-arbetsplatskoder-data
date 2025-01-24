package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "vc_ansvar")
public class VcAnsvar extends AbstractEntity {

    @Column (name = "vc_namn", nullable = true)
    private java.lang.String vcNamn;

    @Column (name = "ansvar", nullable = true)
    private java.lang.String ansvar;

    @Id
    @Column (name = "arbetsplatskod", nullable = false)
    private java.lang.String arbetsplatskod;

    public java.lang.String getVcNamn(){
        return vcNamn;
    }

    public void setVcNamn(java.lang.String v){
        this.vcNamn = v;
    }

    public java.lang.String getAnsvar(){
        return ansvar;
    }

    public void setAnsvar(java.lang.String v){
        this.ansvar = v;
    }

    public java.lang.String getArbetsplatskod(){
        return arbetsplatskod;
    }

    public void setArbetsplatskod(java.lang.String v){
        this.arbetsplatskod = v;
    }

    @Override
    public String getId() {
        return getArbetsplatskod();
    }
}