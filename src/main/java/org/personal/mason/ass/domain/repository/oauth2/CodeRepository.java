package org.personal.mason.ass.domain.repository.oauth2;

import org.personal.mason.ass.common.jpa.AssJpaRepository;
import org.personal.mason.ass.common.oauth2.service.CodeService;
import org.personal.mason.ass.domain.model.oauth2.OCodes;

import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
public interface CodeRepository extends AssJpaRepository<OCodes, Long>, CodeService<OCodes> {

    @Override
    OCodes save(OCodes entity);

    @Override
    void delete(Iterable<? extends OCodes> entities);

    @Override
    OCodes saveAndFlush(OCodes entity);

    @Override
    List<OCodes> findByCode(String code);

    @Override
    OCodes newInstance();
}
