package org.personal.mason.ass.domain.repository.oauth2;

import org.personal.mason.ass.common.jpa.AssJpaRepository;
import org.personal.mason.ass.common.oauth2.service.RefreshTokenService;
import org.personal.mason.ass.domain.model.oauth2.ORefreshToken;

import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
public interface RefreshTokenRepository extends AssJpaRepository<ORefreshToken, Long>, RefreshTokenService<ORefreshToken> {
    @Override
    ORefreshToken newInstance();

    @Override
    ORefreshToken save(ORefreshToken entity);

    @Override
    ORefreshToken saveAndFlush(ORefreshToken entity);

    @Override
    List<ORefreshToken> findByTokenId(String tokenId);

    @Override
    void delete(Iterable<? extends ORefreshToken> entities);
}
