package org.personal.mason.ass.common.oauth2.service;

import org.personal.mason.ass.common.oauth2.model.OauthAccessToken;

import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
public interface AccessTokenService<T extends OauthAccessToken> {

    T save(T entity);

    void delete(Iterable<? extends T> entities);

    T saveAndFlush(T entity);

    T newInstance();

    List<T> findByTokenId(String tokenKey);

    List<T> findByAuthenticationId(String authenticationId);

    List<T> findByTokenIdAndUsername(String clientId, String username);

    List<T> findByRefreshToken(String refreshToken);
}
