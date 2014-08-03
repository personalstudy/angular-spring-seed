package org.personal.mason.ass.domain.repository.oauth2;

import org.personal.mason.ass.common.jpa.AssJpaRepository;
import org.personal.mason.ass.common.oauth2.model.OauthClient;
import org.personal.mason.ass.common.oauth2.service.ClientService;
import org.personal.mason.ass.domain.model.oauth2.OClient;

import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
public interface ClientRepository extends AssJpaRepository<OClient, Long>, ClientService<OClient> {
    @Override
    OClient save(OClient entity);

    @Override
    OClient saveAndFlush(OClient entity);

    @Override
    List<OClient> findByClientId(String clientId);

    @Override
    OClient newInstance();

    @Override
    void delete(Iterable<? extends OClient> entities);
}
