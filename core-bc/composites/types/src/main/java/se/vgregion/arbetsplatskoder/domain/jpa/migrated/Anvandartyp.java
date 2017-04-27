package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "anvandartyp")
public class Anvandartyp extends AbstractEntity {

    @Id
    @Column (name = "atypid", nullable = false)
    private java.lang.Integer atypid;

    @Column (name = "usertype", nullable = true)
    private java.lang.String usertype;

    public java.lang.Integer getAtypid(){
        return atypid;
    }

    public void setAtypid(java.lang.Integer v){
        this.atypid = v;
    }

    public java.lang.String getUsertype(){
        return usertype;
    }

    public void setUsertype(java.lang.String v){
        this.usertype = v;
    }


}