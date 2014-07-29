package org.personal.mason.ass.domain.repository;

import org.personal.mason.ass.common.authority.service.AuthorityService;
import org.personal.mason.ass.common.jpa.AssJpaRepository;
import org.personal.mason.ass.domain.model.ARole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mason on 7/23/14.
 */
public interface RoleRepository extends AssJpaRepository<ARole, Long> {

}
