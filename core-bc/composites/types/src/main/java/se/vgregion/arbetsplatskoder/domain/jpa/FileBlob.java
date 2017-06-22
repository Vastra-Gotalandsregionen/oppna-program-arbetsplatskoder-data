package se.vgregion.arbetsplatskoder.domain.jpa;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author Patrik Björk
 */
@Entity
@Table(name = "file_blob")
public class FileBlob {

    @Id
    private String filename;

    @Lob
    @Basic(fetch=FetchType.EAGER)
    @Column(name="content", nullable = false)
    protected byte[] content;

    public FileBlob() {
    }

    public FileBlob(String filename, byte[] content) {
        this.filename = filename;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}