package se.vgregion.obstetrix.lookup.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Claes Lundahl
 */
@Entity
@Table(name = "vgr_obstetrix_lookup_log")
public class VgrObstetrixUserLookupLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "personal_number")
    private String personalNumber;

    @Column(name = "vgr_id")
    private String vgrId;

    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModificationDate;

    @PrePersist
    void beforeSave() {
        if (time == null) {
            time = new Date();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getVgrId() {
        return vgrId;
    }

    public void setVgrId(String vgrId) {
        this.vgrId = vgrId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }
}
