package se.vgregion.arbetsplatskoder.domain.jpa;

import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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

}