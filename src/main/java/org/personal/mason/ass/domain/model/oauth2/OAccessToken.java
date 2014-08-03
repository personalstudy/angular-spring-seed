package org.personal.mason.ass.domain.model.oauth2;

import org.personal.mason.ass.common.auditing.model.Auditing;
import org.personal.mason.ass.common.oauth2.model.OauthAccessToken;
import org.personal.mason.ass.domain.model.authentication.Account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by mason on 8/3/14.
 */
@Entity
@Table(name = "ass_oauth2_access_token")
public class OAccessToken extends Auditing<Account, Long> implements OauthAccessToken {
    private static final long serialVersionUID = 7411844913452272834L;

    @Lob
    @Column(name = "authentication")
    private byte[] authentication;

    @Column(name = "authentication_id", unique = true)
    private String authenticationId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Lob
    private byte[] token;

    @Column(name = "token_id", unique = true)
    private String tokenId;

    @Column(name = "user_name")
    private String username;

    @Override
    public byte[] getAuthentication() {
        return this.authentication;
    }

    @Override
    public String getTokenId() {
        return this.tokenId;
    }

    @Override
    public String getAuthenticationId() {
        return this.authenticationId;
    }

    @Override
    public byte[] getToken() {
        return this.token;
    }

    @Override
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public void setToken(byte[] token) {
        this.token = token;
    }

    @Override
    public void setAuthenticationId(String authenticationId) {
        this.authenticationId = authenticationId;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setAuthentication(byte[] authentication) {
        this.authentication = authentication;
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUsername() {
        return username;
    }
}
