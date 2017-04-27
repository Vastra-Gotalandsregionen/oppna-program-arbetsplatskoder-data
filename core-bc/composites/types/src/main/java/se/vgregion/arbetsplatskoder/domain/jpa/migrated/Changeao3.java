package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "changeao3")
public class Changeao3 extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column (name = "oldao3", nullable = true, length = 10)
    private java.lang.String oldao3;

    @Column (name = "newao3", nullable = true, length = 10)
    private java.lang.String newao3;

    @Column (name = "changemonth", nullable = true, length = 10)
    private java.lang.String changemonth;

    @Column (name = "valid", nullable = true)
    private java.lang.Boolean valid;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public java.lang.String getOldao3(){
        return oldao3;
    }

    public void setOldao3(java.lang.String v){
        this.oldao3 = v;
    }

    public java.lang.String getNewao3(){
        return newao3;
    }

    public void setNewao3(java.lang.String v){
        this.newao3 = v;
    }

    public java.lang.String getChangemonth(){
        return changemonth;
    }

    public void setChangemonth(java.lang.String v){
        this.changemonth = v;
    }

    public java.lang.Boolean getValid(){
        return valid;
    }

    public void setValid(java.lang.Boolean v){
        this.valid = v;
    }


}