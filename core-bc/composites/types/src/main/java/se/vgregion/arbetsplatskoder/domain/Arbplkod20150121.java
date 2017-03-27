package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "arbplkod_20150121")
public class Arbplkod20150121 extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column (name = "lÄn_arbetsplatskod", nullable = true)
    private java.lang.String lÄnArbetsplatskod;

    @Column (name = "ao3", nullable = true)
    private java.lang.String ao3;

    @Column (name = "ansvar", nullable = true)
    private java.lang.String ansvar;

    @Column (name = "frivillig_uppgift", nullable = true)
    private java.lang.String frivilligUppgift;

    @Column (name = "benamning", nullable = true)
    private java.lang.String benamning;

    @Column (name = "kontaktperson", nullable = true)
    private java.lang.String kontaktperson;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public java.lang.String getLÄnArbetsplatskod(){
        return lÄnArbetsplatskod;
    }

    public void setLÄnArbetsplatskod(java.lang.String v){
        this.lÄnArbetsplatskod = v;
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

    public java.lang.String getFrivilligUppgift(){
        return frivilligUppgift;
    }

    public void setFrivilligUppgift(java.lang.String v){
        this.frivilligUppgift = v;
    }

    public java.lang.String getBenamning(){
        return benamning;
    }

    public void setBenamning(java.lang.String v){
        this.benamning = v;
    }

    public java.lang.String getKontaktperson(){
        return kontaktperson;
    }

    public void setKontaktperson(java.lang.String v){
        this.kontaktperson = v;
    }


}