package org.personal.mason.ass.common.oauth2.model;

/**
 * Created by mason on 8/3/14.
 */
public interface OauthRefreshToken {
    void setTokenId(String tokenId);

    void setToken(byte[] token);

    void setAuthentication(byte[] authentication);

    byte[] getToken();

    byte[] getAuthentication();
}
