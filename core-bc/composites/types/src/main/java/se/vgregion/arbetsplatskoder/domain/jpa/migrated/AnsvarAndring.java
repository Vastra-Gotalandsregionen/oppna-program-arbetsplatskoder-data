package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "ansvar_andring")
public class AnsvarAndring extends AbstractEntity {

    @Id
    @Column (name = "gammaltansvar", nullable = false)
    private java.lang.String gammaltansvar;

    @Column (name = "nyttansvar", nullable = true)
    private java.lang.String nyttansvar;

    public java.lang.String getGammaltansvar(){
        return gammaltansvar;
    }

    public void setGammaltansvar(java.lang.String v){
        this.gammaltansvar = v;
    }

    public java.lang.String getNyttansvar(){
        return nyttansvar;
    }

    public void setNyttansvar(java.lang.String v){
        this.nyttansvar = v;
    }

    @Override
    public Object getId() {
        return getGammaltansvar();
    }

}