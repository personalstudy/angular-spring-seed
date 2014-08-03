package org.personal.mason.ass.common.oauth2.model;

/**
 * Created by mason on 8/3/14.
 */
public interface OauthAccessToken {
    byte[] getAuthentication();

    String getTokenId();

    String getAuthenticationId();

    byte[] getToken();

    void setTokenId(String tokenId);

    void setToken(byte[] token);

    void setAuthenticationId(String authenticationId);

    void setUsername(String username);

    void setAuthentication(byte[] authentication);

    void setRefreshToken(String refreshToken);

    void setClientId(String clientId);
}
