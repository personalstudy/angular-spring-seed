package org.personal.mason.ass.common.oauth2.model;

import java.util.Date;

/**
 * Created by mason on 8/3/14.
 */
public interface OauthApproval {

    void setUserId(String userId);

    String getUserId();

    void setStatus(String status);

    String getStatus();

    void setScope(String scope);

    String getScope();

    void setExpiresAt(Date expiresAt);

    Date getExpiresAt();

    void setClientId(String clientId);

    String getClientId();
}
