package se.vgregion.arbetsplatskoder.repository.extension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn2;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn3;

import java.util.Collection;
import java.util.Set;

/**
 * @author Patrik Björk
 */
public interface Prodn3ExtendedRepository {

    Page<Prodn3> findProdn3s(Set<Integer> notInProdn3Ids, Collection<Prodn2> inProdn2s, Pageable pageable);
}
