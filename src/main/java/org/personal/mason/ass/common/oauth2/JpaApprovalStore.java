package org.personal.mason.ass.common.oauth2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.personal.mason.ass.common.oauth2.model.OauthApproval;
import org.personal.mason.ass.common.oauth2.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
@Component
public class JpaApprovalStore implements ApprovalStore {
    private final Log logger = LogFactory.getLog(getClass());
    private boolean handleRevocationsAsExpiry = false;

    @Autowired
    private ApprovalService approvalService;

    public void setHandleRevocationsAsExpiry(boolean handleRevocationsAsExpiry) {
        this.handleRevocationsAsExpiry = handleRevocationsAsExpiry;
    }

    @Override
    public boolean addApprovals(Collection<Approval> approvals) {
        logger.debug(String.format("adding approvals: [%s]", approvals));
        boolean success = true;

        for (Approval approval : approvals) {
            if (!updateApproval(approval)) {
                if (!addApproval(approval)) {
                    success = false;
                }
            }
        }

        return success;
    }

    @Override
    public boolean revokeApprovals(Collection<Approval> approvals) {
        logger.debug(String.format("Revoking approvals: [%s]", approvals));
        boolean success = true;
        for (final Approval approval : approvals) {
            if (handleRevocationsAsExpiry) {
                approval.setExpiresAt(new Timestamp(System.currentTimeMillis()));
                boolean refreshed = updateApproval(approval);
                return refreshed;
            }
            else {
                boolean refreshed = deleteApproval(approval);
                return refreshed;
            }
        }
        return success;
    }

    @Override
    public Collection<Approval> getApprovals(String userId, String clientId) {
        return findByUserIdAndClientId(userId, clientId);
    }

    // ========

    private boolean addApproval(Approval approval) {
        try {
            approvalService.save(toOauthApproval(approval));
            return true;
        } catch (Exception e){
            logger.error("Failed to create Approval", e);
            return false;
        }
    }

    private boolean updateApproval(Approval approval) {
        try {
            OauthApproval oauthApproval = toOauthApproval(approval);
            List<OauthApproval> approvals = approvalService.findByUserIdAndClientIdAndScope(oauthApproval.getUserId(), oauthApproval.getClientId(), oauthApproval.getScope());

            if(approvals == null || approvals.isEmpty()){
                return false;
            } else {
                for (OauthApproval oa : approvals){
                    oa.setExpiresAt(oauthApproval.getExpiresAt());
                    oa.setStatus(oauthApproval.getStatus());
                    approvalService.saveAndFlush(oa);
                }

                return true;
            }


        } catch (Exception e){
            logger.error("Failed to create Approval", e);
            return false;
        }
    }

    private boolean deleteApproval(Approval approval) {
        OauthApproval oauthApproval = toOauthApproval(approval);
        List<OauthApproval> approvals = approvalService.findByUserIdAndClientIdAndScope(oauthApproval.getUserId(), oauthApproval.getClientId(), oauthApproval.getScope());
        if(approvals == null || approvals.isEmpty()){
            return false;
        } else {
            approvalService.delete(approvals);
            return true;
        }
    }

    private Collection<Approval> findByUserIdAndClientId(String userId, String clientId) {
        List<OauthApproval> oauthApprovals = approvalService.findByUserIdAndClientId(userId, clientId);
        List<Approval> approvals = new ArrayList<>();

        for (OauthApproval oa : oauthApprovals){
            Approval approval = toApproval(oa);
            if(approval != null){
                approvals.add(approval);
            }
        }

        return approvals;
    }

    private Approval toApproval(OauthApproval oa) {
        if(oa != null){
            Approval approval= new Approval(oa.getUserId(),
                    oa.getClientId(),
                    oa.getScope(),
                    oa.getExpiresAt(),
                    toStatus(oa.getStatus()));
        }
        return null;
    }


    public OauthApproval toOauthApproval(Approval approval){
        if(approval != null){
            OauthApproval oauthApproval = approvalService.newInstance();
            oauthApproval.setClientId(approval.getClientId());
            oauthApproval.setExpiresAt(approval.getExpiresAt());
            //oauthApproval.setLastUpdatedAt(approval.getLastUpdatedAt());
            oauthApproval.setScope(approval.getScope());
            oauthApproval.setStatus(toStatusName(approval.getStatus()));
            oauthApproval.setUserId(approval.getUserId());
            return oauthApproval;
        }
        return null;
    }

    public static String toStatusName(Approval.ApprovalStatus status){
        if(status == null){
            return "";
        }
        return status.name();
    }

    public static Approval.ApprovalStatus toStatus(String name){
        if(name == null || name.isEmpty()){
            return Approval.ApprovalStatus.DENIED;
        }
        return Approval.ApprovalStatus.valueOf(name);
    }


}
