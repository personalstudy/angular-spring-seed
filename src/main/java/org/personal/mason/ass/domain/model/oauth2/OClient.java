package org.personal.mason.ass.domain.model.oauth2;

import org.personal.mason.ass.common.auditing.model.Auditing;
import org.personal.mason.ass.common.oauth2.model.OauthClient;
import org.personal.mason.ass.domain.model.authentication.Account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by mason on 8/3/14.
 */
@Entity
@Table(name = "ass_oauth2_client")
public class OClient extends Auditing<Account, Long> implements OauthClient {
    private static final long serialVersionUID = -668331777493158496L;

    @Column(name = "access_token_validity_seconds")
    private Integer accessTokenValiditySeconds;

    @Column(name = "additional_information", length = 4096)
    private String additionalInformation;

    private String authorities;

    @Column(name = "authorized_grant_types")
    private String grantTypes;

    @Column(name = "client_id", unique = true)
    private String clientId;

    @Column(name = "secret")
    private String secret;

    @Column(name = "refresh_token_validity_seconds")
    private Integer refreshTokenValiditySeconds;

    @Column(name = "resource_ids")
    private String resourceIds;

    @Column(name = "scopes")
    private String scopes;

    @Column(name = "redirect_uris")
    private String redirectUris;

    @Column(name = "autoApprove")
    private String autoApprove;

    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public String getResourceIds() {
        return this.resourceIds;
    }

    @Override
    public String getScopes() {
        return this.scopes;
    }

    @Override
    public String getGrantTypes() {
        return this.grantTypes;
    }

    @Override
    public String getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getRedirectUris() {
        return this.redirectUris;
    }

    @Override
    public String getSecret() {
        return this.secret;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.refreshTokenValiditySeconds;
    }

    @Override
    public String getAdditionalInformation() {
        return this.additionalInformation;
    }

    @Override
    public String getAutoApprove() {
        return this.autoApprove;
    }

    @Override
    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    @Override
    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    @Override
    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    @Override
    public void setRedirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
    }

    @Override
    public void setAuthorities(String authorities) {
this.authorities = authorities;
    }

    @Override
    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    @Override
    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    @Override
    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @Override
    public void setAutoApprove(String autoApprove) {
        this.autoApprove = autoApprove;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public void setSecret(String secret) {
        this.secret = secret;
    }


}
