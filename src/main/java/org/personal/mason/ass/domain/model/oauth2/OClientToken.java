package org.personal.mason.ass.domain.model.oauth2;

import org.personal.mason.ass.common.auditing.model.Auditing;
import org.personal.mason.ass.common.oauth2.model.OauthClientToken;
import org.personal.mason.ass.domain.model.authentication.Account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by mason on 8/3/14.
 */
@Entity
@Table(name = "ass_oauth2_client_token")
public class OClientToken extends Auditing<Account, Long> implements OauthClientToken {
    private static final long serialVersionUID = 5124879682905975648L;

    @Column(name = "authentication_id")
    private String authenticationId;

    @Column(name = "client_id")
    private String clientId;

    @Lob
    private byte[] token;

    @Column(name = "token_id")
    private String tokenId;

    @Column(name = "user_name")
    private String userName;

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
        this.userName = username;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAuthenticationId() {
        return authenticationId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
