package org.personal.mason.ass.common.authority.service;

import org.personal.mason.ass.common.authority.model.Group;
import org.personal.mason.ass.common.authority.model.Authority;
import org.personal.mason.ass.common.authority.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Created by mason on 6/30/14.
 */
public interface AuthorityService<A extends Authority> {

    List <A> findAllAuthorities();

    List<? extends User> findUsersWithAuthority(String name);

    List<? extends Group> findGroupsWithAuthority(String name);

    void createAuthority(String name);

    void deleteAuthority(String name);

    void renameAuthority(String oldName, String newName);

    boolean authorityExists(String name);

    void addUserAuthority(String username, GrantedAuthority authority);

    void removeUserAuthority(String username, GrantedAuthority authority);
}
