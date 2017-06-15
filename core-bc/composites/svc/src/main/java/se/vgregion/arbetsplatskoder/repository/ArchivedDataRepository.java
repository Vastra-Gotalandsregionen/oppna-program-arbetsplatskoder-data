package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.ArchivedData;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;

import java.util.List;

/**
 * @author Patrik Björk
 */
public interface ArchivedDataRepository extends JpaRepository<ArchivedData, Integer> {

    List<ArchivedData> findAllByReplacerEqualsOrderByAndringsdatumDesc(Data replacer);

}