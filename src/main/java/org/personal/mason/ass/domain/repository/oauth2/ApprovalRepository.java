package org.personal.mason.ass.domain.repository.oauth2;

import org.personal.mason.ass.common.jpa.AssJpaRepository;
import org.personal.mason.ass.common.oauth2.service.ApprovalService;
import org.personal.mason.ass.domain.model.oauth2.OApprovals;

import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
public interface ApprovalRepository extends AssJpaRepository<OApprovals, Long>, ApprovalService<OApprovals> {

    @Override
    OApprovals save(OApprovals entity);

    @Override
    void delete(Iterable<? extends OApprovals> entities);

    @Override
    OApprovals saveAndFlush(OApprovals entity);

    @Override
    List<OApprovals> findByUserIdAndClientId(String userId, String clientId);

    @Override
    List<OApprovals> findByUserIdAndClientIdAndScope(String userId, String clientId, String scope);

    @Override
    OApprovals newInstance();
}
