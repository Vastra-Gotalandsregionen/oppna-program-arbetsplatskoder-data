package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.repository.extension.DataExtendedRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * @author Patrik Björk
 */
public interface DataRepository extends JpaRepository<Data, Integer>, DataExtendedRepository {

    @Query("select e from Data e where (lower(e.benamning) like :field1) or lower(e.arbetsplatskod) like :field1")
    Page<Data> advancedSearch(@Param("field1") String field1, Pageable page);

    @Query("select e from Data e where ((lower(e.benamning) like :field1) or lower(e.arbetsplatskod) like :field1) and e.prodn1 in :prodn1s")
    Page<Data> advancedSearchByProdn1In(@Param("field1") String field1, @Param("prodn1s") Set<Prodn1> prodn1s, Pageable page);

    Page<Data> findAllByProdn1In(Set<Prodn1> prodn1s, Pageable page);

    List<Data> findAllByProdn1In(Set<Prodn1> prodn1s);

    List<Data> findAllByProdn1IsNull();

    @Query("select d from Data d left join fetch d.prodn3 p")
    List<Data> findAllJoinProdn3();

    @Query("select distinct(d.userIdNew) from Data d")
    List<String> findAllUserIdsWithData();

    /*@Query("select e from Data e where (concat(lower(e.benamning), e.arbetsplatskod, e.andringsdatum, e.tillDatum) like '%sa%')")
    List<Data> foo();*/

    @Query("select d from Data d where d.tillDatum >= '2199-12-01' or d.tillDatum is null")
    List<Data> findAllValidWithoutEndDate();

    @Query("select d from Data d where d.tillDatum >= :than")
    List<Data> findAllTillDatumGreater(@Param("than") Timestamp that);

    @Query("select d from Data d where d.tillDatum >= :than or d.tillDatum is null")
    List<Data> findAllTillDatumGreaterOrForTheTimeBeing(@Param("than") Timestamp that);

    @Query("select d from Data d where d.tillDatum < '2199-12-01' and d.tillDatum >= current_date and d.tillDatum is not null")
    List<Data> findAllValidWithEndDate();

    @Query("select d from Data d where length(d.andringsdatum) = 16 and d.andringsdatum >= :fromDate and d.andringsdatum < :toDate")
    List<Data> findAllUpdatedBetween(@Param("fromDate") String fromDate, @Param("toDate") String toDate);

    List<Data> findAllByArbetsplatskodlanEquals(String arbetsplatskodlan);

    List<Data> findAllByErsattavEquals(String ersattav);

    @Query("select d1 from Data d1 where d1.arbetsplatskodlan = (select max(d.arbetsplatskodlan) from Data d where d.arbetsplatskodlan like :arbetsplatskodlanBeginning%)")
    Data findHighestBeginningWith(@Param("arbetsplatskodlanBeginning") String arbetsplatskodlanBeginning);

    List<Data> findAllByProdn1Raderad(Boolean raderad);
}
