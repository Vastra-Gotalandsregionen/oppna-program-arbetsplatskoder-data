package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "vgr_obstetrix_lookup_log")
public class VgrObstetrixLookupLog extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Long id;

    @Column (name = "lastmodificationdate", nullable = true)
    private java.sql.Timestamp lastmodificationdate;

    @Column (name = "personal_number", nullable = true, length = 255)
    private java.lang.String personalNumber;

    @Column (name = "time", nullable = true)
    private java.sql.Timestamp time;

    @Column (name = "vgr_id", nullable = true, length = 255)
    private java.lang.String vgrId;

    public java.lang.Long getId(){
        return id;
    }

    public void setId(java.lang.Long v){
        this.id = v;
    }

    public java.sql.Timestamp getLastmodificationdate(){
        return lastmodificationdate;
    }

    public void setLastmodificationdate(java.sql.Timestamp v){
        this.lastmodificationdate = v;
    }

    public java.lang.String getPersonalNumber(){
        return personalNumber;
    }

    public void setPersonalNumber(java.lang.String v){
        this.personalNumber = v;
    }

    public java.sql.Timestamp getTime(){
        return time;
    }

    public void setTime(java.sql.Timestamp v){
        this.time = v;
    }

    public java.lang.String getVgrId(){
        return vgrId;
    }

    public void setVgrId(java.lang.String v){
        this.vgrId = v;
    }


}