package org.personal.mason.ass.domain.model.authentication;

import org.personal.mason.ass.common.auditing.model.Auditing;
import org.personal.mason.ass.common.authority.model.Authority;
import org.personal.mason.ass.common.authority.model.Group;
import org.personal.mason.ass.common.authority.model.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mason on 7/23/14.
 */
@Entity
@Table(name = "ass_account")
public class Account extends Auditing<Account, Long> implements User {

    @Column(name = "username", length = 128, unique = true)
    private String username;
    @Column(name = "password", length = 128)
    private String password;
    @Column(name = "is_non_expired")
    private boolean isNonExpired;
    @Column(name = "is_non_locked")
    private boolean isNonLocked;
    @Column(name = "is_credentials_non_expired")
    private boolean isCredentialsNonExpired;
    @Column(name = "is_enabled")
    private boolean isEnabled;

    @ManyToMany(targetEntity = ARole.class)
    @JoinTable(name = "ass_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<ARole> roles = new HashSet<>();

    @ManyToMany(mappedBy = "accounts")
    private Set<AGroup> groups = new HashSet<>();

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNonExpired(boolean isNonExpired) {
        this.isNonExpired = isNonExpired;
    }

    public void setNonLocked(boolean isNonLocked) {
        this.isNonLocked = isNonLocked;
    }

    public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public Set<? extends Authority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public Set<? extends Group> getGroups() {
        return getGroups();
    }

    @Override
    public boolean isNonExpired() {
        return this.isNonExpired;
    }

    @Override
    public boolean isNonLocked() {
        return this.isNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<ARole> getRoles() {
        return roles;
    }

    public void setRoles(Set<ARole> roles) {
        this.roles = roles;
    }

    public void setGroups(Set<AGroup> groups) {
        this.groups = groups;
    }
}
