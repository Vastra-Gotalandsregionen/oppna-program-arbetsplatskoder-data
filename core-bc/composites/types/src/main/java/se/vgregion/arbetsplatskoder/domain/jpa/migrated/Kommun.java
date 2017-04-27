package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "kommun")
public class Kommun extends AbstractEntity {

    @Column (name = "kommkod", nullable = true)
    private java.lang.String kommkod;

    @Id
    @Column (name = "kommun", nullable = false)
    private java.lang.String kommun;

    @Column (name = "lan", nullable = true)
    private java.lang.String lan;

    @Column (name = "lan2", nullable = true)
    private java.lang.String lan2;

    @Column (name = "lokkod", nullable = true)
    private java.lang.String lokkod;

    @Column (name = "kommkod_int", nullable = true)
    private java.lang.Integer kommkodInt;

    public java.lang.String getKommkod(){
        return kommkod;
    }

    public void setKommkod(java.lang.String v){
        this.kommkod = v;
    }

    public java.lang.String getKommun(){
        return kommun;
    }

    public void setKommun(java.lang.String v){
        this.kommun = v;
    }

    public java.lang.String getLan(){
        return lan;
    }

    public void setLan(java.lang.String v){
        this.lan = v;
    }

    public java.lang.String getLan2(){
        return lan2;
    }

    public void setLan2(java.lang.String v){
        this.lan2 = v;
    }

    public java.lang.String getLokkod(){
        return lokkod;
    }

    public void setLokkod(java.lang.String v){
        this.lokkod = v;
    }

    public java.lang.Integer getKommkodInt(){
        return kommkodInt;
    }

    public void setKommkodInt(java.lang.Integer v){
        this.kommkodInt = v;
    }


}