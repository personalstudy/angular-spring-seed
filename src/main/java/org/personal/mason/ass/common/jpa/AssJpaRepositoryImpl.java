package org.personal.mason.ass.common.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.PersistenceProvider;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by mason on 7/27/14.
 */

@NoRepositoryBean
public class AssJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements AssJpaRepository<T, ID>, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(AssJpaRepositoryImpl.class);

    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager entityManager;
    private final PersistenceProvider persistenceProvider;

    private  Class<?> springDataRepositoryInterface;
    public Class<?> getSpringDataRepositoryInterface() {
        return springDataRepositoryInterface;
    }

    public void setSpringDataRepositoryInterface(
            Class<?> springDataRepositoryInterface) {
        this.springDataRepositoryInterface = springDataRepositoryInterface;
    }

    public AssJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
        this.persistenceProvider = PersistenceProvider.fromEntityManager(entityManager);
    }

    public AssJpaRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        this(JpaEntityInformationSupport.getMetadata(domainClass, entityManager), entityManager);
    }

    @Override
    public T newInstance() {
        try {
            return getDomainClass().newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }
}
