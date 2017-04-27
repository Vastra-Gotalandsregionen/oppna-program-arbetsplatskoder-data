package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "namnd_pv_ao3")
public class NamndPvAo3 extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column (name = "ao3_namnd", nullable = false)
    private java.lang.String ao3Namnd;

    @Column (name = "namnd_beskrivning", nullable = true)
    private java.lang.String namndBeskrivning;

    @Column (name = "ao3_pv", nullable = true)
    private java.lang.String ao3Pv;

    @Column (name = "pv_beskrivning", nullable = true)
    private java.lang.String pvBeskrivning;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public java.lang.String getAo3Namnd(){
        return ao3Namnd;
    }

    public void setAo3Namnd(java.lang.String v){
        this.ao3Namnd = v;
    }

    public java.lang.String getNamndBeskrivning(){
        return namndBeskrivning;
    }

    public void setNamndBeskrivning(java.lang.String v){
        this.namndBeskrivning = v;
    }

    public java.lang.String getAo3Pv(){
        return ao3Pv;
    }

    public void setAo3Pv(java.lang.String v){
        this.ao3Pv = v;
    }

    public java.lang.String getPvBeskrivning(){
        return pvBeskrivning;
    }

    public void setPvBeskrivning(java.lang.String v){
        this.pvBeskrivning = v;
    }


}