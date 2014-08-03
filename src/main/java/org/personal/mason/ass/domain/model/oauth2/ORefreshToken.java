package org.personal.mason.ass.domain.model.oauth2;

import org.personal.mason.ass.common.auditing.model.Auditing;
import org.personal.mason.ass.common.oauth2.model.OauthRefreshToken;
import org.personal.mason.ass.domain.model.authentication.Account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by mason on 8/3/14.
 */
@Entity
@Table(name = "ass_oauth2_refresh_token")
public class ORefreshToken extends Auditing<Account, Long> implements OauthRefreshToken {

    @Lob
    @Column(name = "authentication")
    private byte[] authentication;

    @Lob
    @Column(name = "token")
    private byte[] token;

    @Column(name = "token_id")
    private String tokenId;

    @Override
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public void setToken(byte[] token) {
        this.token = token;
    }

    @Override
    public void setAuthentication(byte[] authentication) {
        this.authentication = authentication;
    }

    @Override
    public byte[] getToken() {
        return this.token;
    }

    @Override
    public byte[] getAuthentication() {
        return this.authentication;
    }

    public String getTokenId() {
        return tokenId;
    }
}
