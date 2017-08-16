package se.vgregion.arbetsplatskoder.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Patrik Björk
 */
@Entity
@Table(name = "content")
public class Content {

    @Id
    private String id;

    @Column
    private String content;

    public Content() {
    }

    public Content(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}