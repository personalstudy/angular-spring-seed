package org.personal.mason.ass.domain.model;

import org.personal.mason.ass.common.auditing.model.Auditing;
import org.personal.mason.ass.common.authority.model.Authority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mason on 7/23/14.
 */

@Entity
@Table(name = "ass_role")
public class ARole extends Auditing<Account, Long> implements Authority {
    @Column(name = "name", length = 64, unique = true)
    private String name;
    @Column(name = "is_enabled")
    private boolean isEnabled;

    @ManyToMany(mappedBy = "roles")
    private Set<Account> accounts = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    private Set<AGroup> groups = new HashSet<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<AGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<AGroup> groups) {
        this.groups = groups;
    }
}
