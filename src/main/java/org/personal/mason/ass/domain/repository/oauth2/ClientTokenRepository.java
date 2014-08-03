package org.personal.mason.ass.domain.repository.oauth2;

import org.personal.mason.ass.common.jpa.AssJpaRepository;
import org.personal.mason.ass.common.oauth2.service.ClientTokenService;
import org.personal.mason.ass.domain.model.oauth2.OClientToken;

import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
public interface ClientTokenRepository extends AssJpaRepository<OClientToken, Long>, ClientTokenService<OClientToken> {

    @Override
    OClientToken save(OClientToken entity);

    @Override
    OClientToken saveAndFlush(OClientToken entity);

    @Override
    List<OClientToken> findByAuthenticationId(String authenticationId);

    @Override
    void delete(Iterable<? extends OClientToken> entities);

    @Override
    OClientToken newInstance();
}
