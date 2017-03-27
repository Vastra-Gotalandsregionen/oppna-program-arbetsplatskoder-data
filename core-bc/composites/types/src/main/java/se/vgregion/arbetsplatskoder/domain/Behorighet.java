package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "behorighet")
public class Behorighet extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = true)
    private java.lang.Integer id;

    @Column (name = "behorighettext", nullable = true)
    private java.lang.String behorighettext;

    public java.lang.Integer getId(){
        return id;
    }

    public void setId(java.lang.Integer v){
        this.id = v;
    }

    public java.lang.String getBehorighettext(){
        return behorighettext;
    }

    public void setBehorighettext(java.lang.String v){
        this.behorighettext = v;
    }


}