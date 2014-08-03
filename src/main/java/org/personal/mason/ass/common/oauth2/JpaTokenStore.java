package org.personal.mason.ass.common.oauth2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.personal.mason.ass.common.oauth2.model.OauthAccessToken;
import org.personal.mason.ass.common.oauth2.model.OauthRefreshToken;
import org.personal.mason.ass.common.oauth2.service.AccessTokenService;
import org.personal.mason.ass.common.oauth2.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
public class JpaTokenStore implements TokenStore {
    private static final Log LOG = LogFactory.getLog(JpaTokenStore.class);
    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        OAuth2Authentication authentication = null;

        try {
            List<OauthAccessToken> accessTokens = accessTokenService.findByTokenId(extractTokenKey(token));
            if(accessTokens == null || accessTokens.isEmpty()){
                throw new EmptyResultDataAccessException(1);
            }

            authentication = SerializationUtils.deserialize(accessTokens.get(0).getAuthentication());
        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for token " + token);
            }
        }
        catch (IllegalArgumentException e) {
            LOG.warn("Failed to de-serialize authentication for " + token, e);
            removeAccessToken(token);
        }

        return authentication;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        OauthAccessToken accessToken = toOauthAccessToken(token, authentication);

        List<OauthAccessToken> accessTokensWithToken = accessTokenService.findByTokenId(accessToken.getTokenId());
        if(accessTokensWithToken == null || accessTokensWithToken.isEmpty()){
            if (LOG.isInfoEnabled()) {
                LOG.debug("No accessToken with token " + accessToken.getTokenId());
            }
        } else {
            accessTokenService.delete(accessTokensWithToken);
        }

        List<OauthAccessToken> accessTokensWithAuthentication = accessTokenService.findByAuthenticationId(accessToken.getAuthenticationId());
        if(accessTokensWithAuthentication == null || accessTokensWithAuthentication.isEmpty()){
            if (LOG.isInfoEnabled()) {
                LOG.debug("No accessToken with authentication id " + accessToken.getAuthenticationId());
            }
        } else {
            accessTokenService.delete(accessTokensWithAuthentication);
        }

        accessTokenService.save(accessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken accessToken = null;

        try {
            String tokenId = extractTokenKey(tokenValue);
            List<OauthAccessToken> accessTokens = accessTokenService.findByTokenId(tokenId);

            if(accessTokens == null || accessTokens.isEmpty()){
                throw new EmptyResultDataAccessException(1);
            }

            accessToken = SerializationUtils.deserialize(accessTokens.get(0).getToken());
        } catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for token " + tokenValue);
            }
        } catch (IllegalArgumentException e) {
            LOG.warn("Failed to de-serialize access token for " + tokenValue);
            removeAccessToken(tokenValue);
        }

        return accessToken;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        removeAccessToken(token.getValue());
    }

    private void removeAccessToken(String tokenValue) {
        try {
            String tokenId = extractTokenKey(tokenValue);
            List<OauthAccessToken> accessTokens = accessTokenService.findByTokenId(tokenId);
            accessTokenService.delete(accessTokens);
        }catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for token " + tokenValue);
            }
        }
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        OauthRefreshToken oauthRefreshToken = refreshTokenService.newInstance();
        oauthRefreshToken.setTokenId(extractTokenKey(refreshToken.getValue()));
        oauthRefreshToken.setToken(SerializationUtils.serialize(refreshToken));
        oauthRefreshToken.setAuthentication(SerializationUtils.serialize(authentication));
        refreshTokenService.save(oauthRefreshToken);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String token) {
            OAuth2RefreshToken refreshToken = null;

            try {
                String tokenId = extractTokenKey(token);
                List<OauthRefreshToken> refreshTokens = refreshTokenService.findByTokenId(tokenId);

                if(refreshTokens == null || refreshTokens.isEmpty()){
                    throw new EmptyResultDataAccessException(1);
                }

                refreshToken = SerializationUtils.deserialize(refreshTokens.get(0).getToken());
            } catch (EmptyResultDataAccessException e) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Failed to find refresh token for token " + token);
                }
            } catch (IllegalArgumentException e) {
                LOG.warn("Failed to de-serialize refresh token for token " + token);
                removeRefreshToken(token);
            }

            return refreshToken;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return readAuthenticationForRefreshToken(token.getValue());
    }

    private OAuth2Authentication readAuthenticationForRefreshToken(String value) {
        OAuth2Authentication authentication = null;

        try {
            String tokenId = extractTokenKey(value);
            List<OauthRefreshToken> refreshTokens = refreshTokenService.findByTokenId(tokenId);

            if(refreshTokens == null || refreshTokens.isEmpty()){
                throw new EmptyResultDataAccessException(1);
            }

            authentication = SerializationUtils.deserialize(refreshTokens.get(0).getAuthentication());
        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for token " + value);
            }
        }
        catch (IllegalArgumentException e) {
            LOG.warn("Failed to de-serialize access token for " + value);
            removeRefreshToken(value);
        }

        return authentication;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        removeRefreshToken(token.getValue());
    }


    private void removeRefreshToken(String token) {
        try {
            String tokenId = extractTokenKey(token);
            List<OauthRefreshToken> refreshTokens = refreshTokenService.findByTokenId(tokenId);
            refreshTokenService.delete(refreshTokens);
        }catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find refresh token for token " + token);
            }
        }
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    private void removeAccessTokenUsingRefreshToken(String refreshToken) {
       try{
        String refreshTokenId = extractTokenKey(refreshToken);
        List<OauthAccessToken> accessTokens = accessTokenService.findByRefreshTokenId(refreshTokenId);

        if(accessTokens == null || accessTokens.isEmpty()){
            throw new EmptyResultDataAccessException(1);
        }
           accessTokenService.delete(accessTokens);
    }catch (EmptyResultDataAccessException e) {
        if (LOG.isInfoEnabled()) {
            LOG.info("Failed to find refresh token for refresh token " + refreshToken);
        }
    }
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken = null;

        String key = authenticationKeyGenerator.extractKey(authentication);
        try {
            List<OauthAccessToken> accessTokens = accessTokenService.findByAuthenticationId(key);

            if(accessTokens == null || accessTokens.isEmpty()){
                throw new EmptyResultDataAccessException(1);
            }

            accessToken = toOAuth2AccessToken(accessTokens.get(0));
        } catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.debug("Failed to find access token for authentication " + authentication);
            }
        } catch (IllegalArgumentException e) {
            LOG.error("Could not extract access token for authentication " + authentication);
        }

        if (accessToken != null && !key.equals(authenticationKeyGenerator.extractKey(readAuthentication(accessToken.getValue())))) {
            removeAccessToken(accessToken.getValue());
            storeAccessToken(accessToken, authentication);
        }

        return accessToken;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        List<OAuth2AccessToken> auth2AccessTokens = new ArrayList<OAuth2AccessToken>();

        try {
            List<OauthAccessToken> accessTokens = accessTokenService.findByTokenIdAndUsername(clientId, userName);

            if(accessTokens == null || accessTokens.isEmpty()){
                throw new EmptyResultDataAccessException(1);
            }

            for (OauthAccessToken token : accessTokens){
                OAuth2AccessToken auth2AccessToken = toOAuth2AccessToken(token);
                if (auth2AccessToken != null) {
                    auth2AccessTokens.add(auth2AccessToken);
                }
            }
        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("Failed to find access token for clientId %s and username %s", clientId, userName));
            }
        }

        return auth2AccessTokens;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List<OAuth2AccessToken> auth2AccessTokens = new ArrayList<OAuth2AccessToken>();

        try {
            List<OauthAccessToken> accessTokens = accessTokenService.findByTokenId(clientId);

            if(accessTokens == null || accessTokens.isEmpty()){
                throw new EmptyResultDataAccessException(1);
            }

            for (OauthAccessToken token : accessTokens){
                OAuth2AccessToken auth2AccessToken = toOAuth2AccessToken(token);
                if (auth2AccessToken != null) {
                    auth2AccessTokens.add(auth2AccessToken);
                }
            }
        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find access token for clientId " + clientId);
            }
        }

        return auth2AccessTokens;
    }

    //~=========

    protected String extractTokenKey(String value) {
        if (value == null) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        try {
            byte[] bytes = digest.digest(value.getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }

    private OAuth2AccessToken toOAuth2AccessToken(OauthAccessToken oauthAccessToken) {
        return SerializationUtils.deserialize(oauthAccessToken.getToken());
    }

    private OauthAccessToken toOauthAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        String refreshToken = null;
        if(token.getRefreshToken() != null){
            refreshToken = token.getRefreshToken().getValue();
        }

        OauthAccessToken at = accessTokenService.newInstance();
        at.setTokenId(extractTokenKey(token.getValue()));
        at.setToken(SerializationUtils.serialize(token));

        at.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
        at.setUsername(authentication.isClientOnly() ? null : authentication.getName());
        at.setClientId(authentication.getOAuth2Request().getClientId());
        at.setAuthentication(SerializationUtils.serialize(authentication));
        at.setRefreshToken(extractTokenKey(refreshToken));

        return at;
    }
}
