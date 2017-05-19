package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn2;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn3;
import se.vgregion.arbetsplatskoder.repository.extension.Prodn3ExtendedRepository;

import java.util.List;
import java.util.Set;

/**
 * @author Patrik Björk
 */
public interface Prodn3Repository extends JpaRepository<Prodn3, Integer>, Prodn3ExtendedRepository {

    Page<Prodn3> findAllByProdn2In(List<Prodn2> prodn2s, Pageable pageable);

    Page<Prodn3> findAllByProdn2Equals(Prodn2 prodn2, Pageable pageable);

    Prodn3 findProdn3ByIdEquals(Integer id);

    Page<Prodn3> findAllByIdNotIn(Set<Integer> ids, Pageable pageable);

    Page<Prodn3> findAllByProdn2IsNull(Pageable pageable);
}