package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Ao3;

/**
 * @author Patrik Björk
 */
public interface Ao3Repository extends JpaRepository<Ao3, Integer> {

    Ao3 findByAo3id(String ao3id);

}
