package org.personal.mason.ass.domain.model.authentication;

import org.personal.mason.ass.common.auditing.model.Auditing;
import org.personal.mason.ass.common.authority.model.Authority;
import org.personal.mason.ass.common.authority.model.Group;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mason on 7/23/14.
 */
@Entity
@Table(name = "ass_group")
public class AGroup extends Auditing<Account, Long> implements Group {
    @Column(name = "name", length = 64, unique = true)
    private String name;
    @Column(name = "is_enabled")
    private boolean isEnabled;

    @ManyToMany(targetEntity = Account.class)
    @JoinTable(name = "ass_group_member", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
    private Set<Account> accounts = new HashSet<>();

    @ManyToMany(targetEntity = ARole.class)
    @JoinTable(name = "ass_group_role", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<ARole> roles = new HashSet<>();
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public Set<ARole> getRoles() {
        return this.roles;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public void setRoles(Set<ARole> roles) {
        this.roles = roles;
    }
}
