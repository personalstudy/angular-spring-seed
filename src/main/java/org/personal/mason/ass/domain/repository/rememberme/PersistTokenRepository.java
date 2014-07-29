package org.personal.mason.ass.domain.repository.rememberme;

import org.personal.mason.ass.common.jpa.AssJpaRepository;
import org.personal.mason.ass.common.rememberme.service.RememberMeTokenService;
import org.personal.mason.ass.domain.model.rememberme.APersistToken;

/**
 * Created by mason on 7/27/14.
 */
public interface PersistTokenRepository extends AssJpaRepository<APersistToken, Long>, RememberMeTokenService<APersistToken> {

    @Override
    APersistToken save(APersistToken entity);

    @Override
    APersistToken saveAndFlush(APersistToken token);

    @Override
    void delete(APersistToken entity);

    @Override
    APersistToken findOne(Long id);

    @Override
    APersistToken newInstance();
}
