package org.personal.mason.ass.common.oauth2.model;

/**
 * Created by mason on 8/3/14.
 */
public interface OauthCode {

    byte[] getAuthentication();

    void setAuthentication(byte[] authentication);

    void setCode(String code);
}
