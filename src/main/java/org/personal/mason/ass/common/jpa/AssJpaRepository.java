package org.personal.mason.ass.common.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by mason on 7/27/14.
 */

@NoRepositoryBean
public interface AssJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    T newInstance();

}
