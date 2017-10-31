package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn2;

import java.util.Collection;

/**
 * @author Patrik Björk
 */
public interface Prodn2Repository extends JpaRepository<Prodn2, Integer> {

    Page<Prodn2> findAllByProdn1Equals(Prodn1 prodn1, Pageable pageable);

    Prodn2 findProdn2ByIdEquals(Integer id);

    Page<Prodn2> findAllByProdn1In(Collection<Prodn1> prodn1s, Pageable pageable);

}