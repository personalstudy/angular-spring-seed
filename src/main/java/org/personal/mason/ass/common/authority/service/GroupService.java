package org.personal.mason.ass.common.authority.service;

import org.personal.mason.ass.common.authority.model.Group;
import org.personal.mason.ass.common.authority.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Created by mason on 7/22/14.
 */
public interface GroupService<T extends Group> {

    List<T> findAllGroups();

    List<? extends User> findUsersInGroup(String groupName);

    void createGroup(String groupName, List<GrantedAuthority> authorities);

    void deleteGroup(String groupName);

    void renameGroup(String oldName, String newName);

    void addUserToGroup(String username, String group);

    void removeUserFromGroup(String username, String groupName);

    List<GrantedAuthority> findGroupAuthorities(String groupName);

    void addGroupAuthority(String groupName, GrantedAuthority authority);

    void removeGroupAuthority(String groupName, GrantedAuthority authority);
}
