package org.personal.mason.ass.common.rememberme.service;

import org.personal.mason.ass.common.rememberme.model.RememberMeToken;

/**
 * Created by mason on 7/22/14.
 */
public interface RememberMeTokenService<T extends RememberMeToken> {

    T save(T entity);

    T saveAndFlush(T token);

    void delete(T token);

    T findOne(Long id);

    T newInstance();
}
