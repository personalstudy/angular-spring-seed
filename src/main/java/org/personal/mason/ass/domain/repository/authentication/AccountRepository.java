package org.personal.mason.ass.domain.repository.authentication;

import org.personal.mason.ass.common.authority.service.UserService;
import org.personal.mason.ass.common.jpa.AssJpaRepository;
import org.personal.mason.ass.domain.model.authentication.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by mason on 7/23/14.
 */
public interface AccountRepository extends AssJpaRepository<Account, Long>, UserService<Account> {


    @Query("select a from Account a where a.username = :username")
    @Override
    Account loadUserByUsername(@Param("username") String username);

    @Override
    Account save(Account entity);

    @Override
    Account saveAndFlush(Account entity);

    @Override
    void delete(Account entity);

    @Query("select a from Account a where a.username = 'SYSTEM'")
    @Override
    Account findSystemUser();
}
