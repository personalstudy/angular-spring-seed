package org.personal.mason.ass.common.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mason on 7/27/14.
 */

@NoRepositoryBean
public interface AssJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    T newInstance();

    List<T> findByExample(T example);
    List<T> findByExample(T example, Sort sort);
    Page<T> findByExample(T example, Pageable pageable);


    long countByExample(T example);

}
