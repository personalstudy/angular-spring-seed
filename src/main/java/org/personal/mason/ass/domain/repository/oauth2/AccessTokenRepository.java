package org.personal.mason.ass.domain.repository.oauth2;

import org.personal.mason.ass.common.jpa.AssJpaRepository;
import org.personal.mason.ass.common.oauth2.service.AccessTokenService;
import org.personal.mason.ass.domain.model.oauth2.OAccessToken;

import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
public interface AccessTokenRepository extends AssJpaRepository<OAccessToken, Long>, AccessTokenService<OAccessToken> {
    @Override
    OAccessToken save(OAccessToken entity);

    @Override
    OAccessToken saveAndFlush(OAccessToken entity);

    @Override
    void delete(Iterable<? extends OAccessToken> entities);

    @Override
    OAccessToken newInstance();

    @Override
    List<OAccessToken> findByTokenId(String tokenKey);

    @Override
    List<OAccessToken> findByAuthenticationId(String authenticationId);

    @Override
    List<OAccessToken> findByTokenIdAndUsername(String clientId, String username);

    @Override
    List<OAccessToken> findByRefreshToken(String refreshToken);
}
