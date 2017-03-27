package se.vgregion.arbetsplatskoder.domain;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "andra_ansvar2")
public class AndraAnsvar2 extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column (name = "gammalt", nullable = true)
    private java.lang.String gammalt;

    @Column (name = "nytt", nullable = true)
    private java.lang.Integer nytt;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public java.lang.String getGammalt(){
        return gammalt;
    }

    public void setGammalt(java.lang.String v){
        this.gammalt = v;
    }

    public java.lang.Integer getNytt(){
        return nytt;
    }

    public void setNytt(java.lang.Integer v){
        this.nytt = v;
    }


}