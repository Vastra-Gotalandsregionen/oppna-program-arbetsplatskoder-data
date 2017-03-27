package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "kommunort")
public class Kommunort extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column (name = "kommkod", nullable = true)
    private java.lang.String kommkod;

    @Column (name = "kommunnamn", nullable = true)
    private java.lang.String kommunnamn;

    @Column (name = "lan", nullable = true)
    private java.lang.String lan;

    @Column (name = "lan2", nullable = true)
    private java.lang.String lan2;

    @Column (name = "lokkod", nullable = true)
    private java.lang.String lokkod;

    @Column (name = "kommkod_int", nullable = true)
    private java.lang.Integer kommkodInt;

    @Column (name = "forsamling", nullable = true)
    private java.lang.String forsamling;
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

    public java.lang.String getKommunnamn(){
        return kommunnamn;
    }

    public void setKommunnamn(java.lang.String v){
        this.kommunnamn = v;
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

    public java.lang.String getForsamling(){
        return forsamling;
    }

    public void setForsamling(java.lang.String v){
        this.forsamling = v;
    }


}