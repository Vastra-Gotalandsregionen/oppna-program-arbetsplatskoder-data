package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;

@Entity
@Table(name = "data", indexes = {
        @Index(columnList = "benamning"),
        @Index(columnList = "prodn1"),
        @Index(columnList = "arbetsplatskodlan", unique = true)
})
public class Data extends AbstractData {

    // This property is used for lookup. It's denormalized to have it since it actually is given by prodn3.
    @JoinColumn(name = "prodn1")
    @ManyToOne(fetch = FetchType.EAGER)
    private Prodn1 prodn1;

    @JoinColumn(name = "prodn3")
    @ManyToOne(fetch = FetchType.EAGER)
    private Prodn3 prodn3;

    public Prodn1 getProdn1() {
        return prodn1;
    }

    public void setProdn1(Prodn1 prodn1) {
        this.prodn1 = prodn1;
    }

    public Prodn3 getProdn3() {
        return prodn3;
    }

    public void setProdn3(Prodn3 prodn3) {
        this.prodn3 = prodn3;
    }
}