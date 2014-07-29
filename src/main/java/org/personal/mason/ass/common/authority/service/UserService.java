package org.personal.mason.ass.common.authority.service;

import org.personal.mason.ass.common.authority.model.User;

/**
 * Created by mason on 7/22/14.
 */
public interface UserService<U extends User> {

    U loadUserByUsername(String username);

    U save(U user);

    U saveAndFlush(U entity);

    void delete(U entity);

    U findSystemUser();
}
