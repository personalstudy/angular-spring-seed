package org.personal.mason.ass.common.oauth2.model;

/**
 * Created by mason on 8/3/14.
 */
public interface OauthClient {

    String getClientId();

    String getResourceIds();

    String getScopes();

    String getGrantTypes();

    String getAuthorities();

    String getRedirectUris();

    String getSecret();

    Integer getAccessTokenValiditySeconds();

    Integer getRefreshTokenValiditySeconds();

    String getAdditionalInformation();

    String getAutoApprove();

    void setResourceIds(String resourceIds);

    void setScopes(String scopes);

    void setGrantTypes(String grantTypes);

    void setRedirectUris(String redirectUris);

    void setAuthorities(String authorities);

    void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds);

    void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds);

    void setAdditionalInformation(String additionalInformation);

    void setAutoApprove(String autoApprove);

    void setClientId(String clientId);

    void setSecret(String secret);
}
