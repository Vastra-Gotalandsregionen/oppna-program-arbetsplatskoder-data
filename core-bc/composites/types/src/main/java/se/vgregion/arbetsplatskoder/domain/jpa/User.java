package se.vgregion.arbetsplatskoder.domain.jpa;

import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Patrik Björk
 */
@Entity
@Table(name = "_user")
public class User {

    // Normally VGR ID
    @Id
    private String id;

    @Column
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Prodn1.class)
    private Set<Prodn1> prodn1s = new HashSet<>();

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String mail;

    @Column
    private String displayName;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private byte[] thumbnailPhoto;

    @Column
    private Boolean inactivated = false;

    @Column
    private Boolean prodnChangeAware;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Prodn1> getProdn1s() {
        return prodn1s;
    }

    public void setProdn1s(Set<Prodn1> prodn1s) {
        this.prodn1s = prodn1s;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setThumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
    }

    public byte[] getThumbnailPhoto() {
        return thumbnailPhoto;
    }

    public Boolean getInactivated() {
        return inactivated;
    }

    public void setInactivated(Boolean inactivated) {
        this.inactivated = inactivated;
    }

    public Boolean getProdnChangeAware() {
        return prodnChangeAware;
    }

    public void setProdnChangeAware(Boolean prodnChangeAware) {
        this.prodnChangeAware = prodnChangeAware;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", prodn1s=" + prodn1s +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mail='" + mail + '\'' +
                ", displayName='" + displayName + '\'' +
                ", role=" + role +
                // ", thumbnailPhoto=" + Arrays.toString(thumbnailPhoto) +
                ", inactivated=" + inactivated +
                ", prodnChangeAware=" + prodnChangeAware +
                '}';
    }

}
