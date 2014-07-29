package org.personal.mason.ass.common.authority.model;

import java.util.Set;

/**
 * Created by mason on 7/20/14.
 */
public interface User {
    Set<? extends Authority> getAuthorities();

    String getPassword();

    String getUsername();

    Set<? extends Group> getGroups();

    boolean isNonExpired();

    boolean isNonLocked();

    boolean isCredentialsNonExpired();

    boolean isEnabled();

    void setPassword(String password);
}
