package se.vgregion.arbetsplatskoder.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Patrik Björk
 */
@Entity
@Table(name = "link")
public class Link {

    @Id
    private Integer id;

    @Column
    private String label;

    @Column
    private String url;

    @Column
    private Boolean privateContent;

    public Link() {
    }

    public Link(Integer id, String label, String url) {
        this.id = id;
        this.label = label;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean isPrivateContent() {
        return privateContent;
    }

    public void setUrl(Boolean privateContent) {
        this.privateContent = privateContent;
    }


}
