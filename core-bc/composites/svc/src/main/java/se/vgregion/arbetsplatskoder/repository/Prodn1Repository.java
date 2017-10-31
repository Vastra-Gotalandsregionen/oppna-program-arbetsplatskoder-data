package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;

import java.util.Collection;
import java.util.List;

/**
 * @author Patrik Björk
 */
public interface Prodn1Repository extends JpaRepository<Prodn1, Integer> {

    List<Prodn1> findAllByIdIn(Collection<Integer> ids);

    List<Prodn1> getAllByIdIn(Collection<Integer> ids);
}