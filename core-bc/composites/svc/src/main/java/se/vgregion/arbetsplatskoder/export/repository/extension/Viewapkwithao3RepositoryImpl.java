package se.vgregion.arbetsplatskoder.export.repository.extension;

import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkwithao3;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Viewapkwithao3RepositoryImpl implements Viewapkwithao3ExtendedRepository {

    @PersistenceContext(unitName = "export")
    private EntityManager entityManager;

    @Override
    public void persist(Viewapkwithao3 viewapkwithao3) {
        entityManager.persist(viewapkwithao3);
    }
}
