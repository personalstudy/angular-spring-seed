package org.personal.mason.ass.common.oauth2.service;

import org.personal.mason.ass.common.oauth2.model.OauthClientToken;

import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
public interface ClientTokenService<T extends OauthClientToken> {

    T save(T entity);

    void delete(Iterable<? extends T> entities);

    T saveAndFlush(T entity);

    T newInstance();

    List<T> findByAuthenticationId(String authenticationId);
}
