package org.personal.mason.ass.domain.model.oauth2;

import org.personal.mason.ass.common.auditing.model.Auditing;
import org.personal.mason.ass.common.oauth2.model.OauthApproval;
import org.personal.mason.ass.domain.model.authentication.Account;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mason on 8/3/14.
 */

@Entity
@Table(name = "ass_oauth2_approval")
public class OApprovals extends Auditing<Account, Long> implements OauthApproval {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "scope")
    private String scope;

    @Column(name = "status")
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_at")
    private Date expiresAt;

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getUserId() {
        return this.userId;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public Date getExpiresAt() {
        return this.expiresAt;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }
}
