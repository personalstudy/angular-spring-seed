package org.personal.mason.ass.utils;

import org.personal.mason.ass.domain.model.authentication.ARole;
import org.personal.mason.ass.domain.model.authentication.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mason on 7/27/14.
 */
public class DataUtils {

    public List<ARole> initRoles(){
        List<ARole> roles = new ArrayList<>();

        ARole role = new ARole();
        role.setEnabled(true);
        role.setName("USER");
        roles.add(role);

        role = new ARole();
        role.setEnabled(true);
        role.setName("ADMIN");
        roles.add(role);

        return roles;
    }

    public Account initUser(){
        Account account = new Account();
        account.setEnabled(true);
        account.setCredentialsNonExpired(true);
        account.setNonExpired(true);
        account.setNonLocked(true);
        account.setUsername("SYSTEM");
        return account;
    }
}
