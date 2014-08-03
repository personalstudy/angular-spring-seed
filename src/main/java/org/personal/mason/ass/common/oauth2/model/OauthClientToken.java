package org.personal.mason.ass.common.oauth2.model;

/**
 * Created by mason on 8/3/14.
 */
public interface OauthClientToken {

    byte[] getToken();

    void setTokenId(String tokenId);

    void setToken(byte[] token);

    void setAuthenticationId(String authenticationId);

    void setUsername(String username);

    void setClientId(String clientId);
}
