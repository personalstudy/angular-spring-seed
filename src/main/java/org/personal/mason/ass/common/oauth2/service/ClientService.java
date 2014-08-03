package org.personal.mason.ass.common.oauth2.service;

import org.personal.mason.ass.common.oauth2.model.OauthClient;

import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
public interface ClientService<T extends OauthClient> {

    T save(T entity);

    void delete(Iterable<? extends T> entities);

    T saveAndFlush(T entity);

    T newInstance();

    List<T> findByClientId(String clientId);

    List<T> findAll();
}
