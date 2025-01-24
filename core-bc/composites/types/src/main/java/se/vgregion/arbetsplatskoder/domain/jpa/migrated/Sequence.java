package se.vgregion.arbetsplatskoder.domain.jpa.migrated;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "sequence")
public class Sequence extends AbstractEntity {

    @Id
    @Column (name = "seq_name", nullable = false, length = 50)
    private java.lang.String seqName;

    @Column (name = "seq_count", nullable = true)
    private java.math.BigDecimal seqCount;

    public java.lang.String getSeqName(){
        return seqName;
    }

    public void setSeqName(java.lang.String v){
        this.seqName = v;
    }

    public java.math.BigDecimal getSeqCount(){
        return seqCount;
    }

    public void setSeqCount(java.math.BigDecimal v){
        this.seqCount = v;
    }

    @Override
    public String getId() {
        return getSeqName();
    }
}