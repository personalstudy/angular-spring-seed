package org.personal.mason.ass.domain.repository.rememberme;

import org.personal.mason.ass.common.jpa.AssJpaRepository;
import org.personal.mason.ass.common.rememberme.service.RememberMeTokenService;
import org.personal.mason.ass.domain.model.rememberme.RPersistToken;

/**
 * Created by mason on 7/27/14.
 */
public interface PersistTokenRepository extends AssJpaRepository<RPersistToken, Long>, RememberMeTokenService<RPersistToken> {

    @Override
    RPersistToken save(RPersistToken entity);

    @Override
    RPersistToken saveAndFlush(RPersistToken token);

    @Override
    void delete(RPersistToken entity);

    @Override
    RPersistToken findOne(Long id);

    @Override
    RPersistToken newInstance();
}
