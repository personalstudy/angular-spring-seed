package org.personal.mason.ass.common.oauth2.service;

import org.personal.mason.ass.common.oauth2.model.OauthRefreshToken;

import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
public interface RefreshTokenService<T extends OauthRefreshToken> {

    T save(T entity);

    void delete(Iterable<? extends T> entities);

    T saveAndFlush(T entity);

    T newInstance();

    List<T> findByTokenId(String tokenId);
}
