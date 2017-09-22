package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "fakturering")
public class Fakturering extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "faktureringid", nullable = true)
    private java.lang.Integer faktureringid;

    @Column (name = "faktureringtext", nullable = true)
    private java.lang.String faktureringtext;

    @Column (name = "fakturering_kort_text", nullable = true)
    private java.lang.String faktureringKortText;

    @Column (name = "raderad", nullable = false)
    private java.lang.Boolean raderad;

    @Column (name = "ssma_timestamp", nullable = false)
    private Byte[] ssmaTimestamp;

    public java.lang.Integer getId(){
        return id;
    }

    public void setId(java.lang.Integer v){
        this.id = v;
    }

    public java.lang.Integer getFaktureringid(){
        return faktureringid;
    }

    public void setFaktureringid(java.lang.Integer v){
        this.faktureringid = v;
    }

    public java.lang.String getFaktureringtext(){
        return faktureringtext;
    }

    public void setFaktureringtext(java.lang.String v){
        this.faktureringtext = v;
    }

    public java.lang.Boolean getRaderad(){
        return raderad;
    }

    public void setRaderad(java.lang.Boolean v){
        this.raderad = v;
    }

    public Byte[] getSsmaTimestamp(){
        return ssmaTimestamp;
    }

    public void setSsmaTimestamp(Byte[] v){
        this.ssmaTimestamp = v;
    }


    public String getFaktureringKortText() {
        return faktureringKortText;
    }

    public void setFaktureringKortText(String faktureringKortText) {
        this.faktureringKortText = faktureringKortText;
    }
}