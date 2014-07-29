package org.personal.mason.ass.domain.repository.authentication;

import org.personal.mason.ass.common.jpa.AssJpaRepository;
import org.personal.mason.ass.domain.model.authentication.ARole;

/**
 * Created by mason on 7/23/14.
 */
public interface RoleRepository extends AssJpaRepository<ARole, Long> {

}
