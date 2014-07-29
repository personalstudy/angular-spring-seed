package org.personal.mason.ass.common.jpa;

import static org.springframework.data.querydsl.QueryDslUtils.*;

import org.springframework.data.jpa.repository.query.QueryExtractor;
import org.springframework.data.jpa.repository.support.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by mason on 7/27/14.
 */
public class AssRepositoryFactory extends JpaRepositoryFactory {

    private final EntityManager entityManager;
    private final QueryExtractor extractor;

    /**
     * Creates a new {@link org.springframework.data.jpa.repository.support.JpaRepositoryFactory}.
     *
     * @param entityManager must not be {@literal null}
     */
    public AssRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
        Assert.notNull(entityManager);
        this.entityManager = entityManager;
        this.extractor = PersistenceProvider.fromEntityManager(entityManager);
    }

    @Override
    protected <T, ID extends Serializable> SimpleJpaRepository<T, ID> getTargetRepository(RepositoryMetadata metadata, EntityManager entityManager) {
        Class<?> repositoryInterface = metadata.getRepositoryInterface();
        Class<T> domainType = (Class<T>) metadata.getDomainType();
        JpaEntityInformation<T, ID> entityInformation = getEntityInformation(domainType);

        if(isQueryDslExecutor(repositoryInterface)){
            return new QueryDslJpaRepository<T, ID>(entityInformation, entityManager);
        } else {
            return new AssJpaRepositoryImpl<T, ID>(entityInformation, entityManager);
        }
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
            return QueryDslJpaRepository.class;
        } else {
            return AssJpaRepositoryImpl.class;
        }
    }

    /**
     * Returns whether the given repository interface requires a QueryDsl
     * specific implementation to be chosen.
     *
     * @param repositoryInterface
     * @return
     */
    private boolean isQueryDslExecutor(Class<?> repositoryInterface) {

        return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
    }
}
