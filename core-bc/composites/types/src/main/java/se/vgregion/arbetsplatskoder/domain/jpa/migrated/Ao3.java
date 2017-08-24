package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ao3")
public class Ao3 extends AbstractEntity {

    @Id
    @Column (name = "id", nullable = false)
    private java.lang.Integer id;

    @Column (name = "ao3id", nullable = true)
    private java.lang.String ao3id;

    @Column (name = "foretagsnamn", nullable = true)
    private java.lang.String foretagsnamn;

    @Column (name = "producent", nullable = true)
    private java.lang.String producent;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Prodn1> prodn1s = new ArrayList<>();

    @Column (name = "kontaktperson", nullable = true)
    private java.lang.String kontaktperson;

    @Column (name = "foretagsnr", nullable = true)
    private java.lang.String foretagsnr;

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

    public java.lang.String getAo3id(){
        return ao3id;
    }

    public void setAo3id(java.lang.String v){
        this.ao3id = v;
    }

    public java.lang.String getForetagsnamn(){
        return foretagsnamn;
    }

    public void setForetagsnamn(java.lang.String v){
        this.foretagsnamn = v;
    }

    public java.lang.String getProducent(){
        return producent;
    }

    public void setProducent(java.lang.String v){
        this.producent = v;
    }

    public List<Prodn1> getProdn1s() {
        return prodn1s;
    }

    public void setProdn1s(List<Prodn1> prodn1s) {
        this.prodn1s = prodn1s;
    }

    public java.lang.String getKontaktperson(){
        return kontaktperson;
    }

    public void setKontaktperson(java.lang.String v){
        this.kontaktperson = v;
    }

    public java.lang.String getForetagsnr(){
        return foretagsnr;
    }

    public void setForetagsnr(java.lang.String v){
        this.foretagsnr = v;
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


}