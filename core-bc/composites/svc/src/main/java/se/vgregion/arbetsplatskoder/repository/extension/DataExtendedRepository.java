package se.vgregion.arbetsplatskoder.repository.extension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;

import java.util.Set;

/**
 * Created by clalu4 on 2017-06-05.
 */
public interface DataExtendedRepository {
    Page<Data> advancedSearch(String withTextFilter, Pageable pageable);

    Page<Data> advancedSearch(String withTextFilter, Pageable pageable, Set<Prodn1> prodn1s);

    Data saveAndArchive(Data data);

}
