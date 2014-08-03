package org.personal.mason.ass.common.oauth2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.personal.mason.ass.common.oauth2.model.OauthClientToken;
import org.personal.mason.ass.common.oauth2.service.ClientTokenService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.client.token.DefaultClientKeyGenerator;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;

import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
public class JpaClientTokenServices implements ClientTokenServices {
    private static final Log LOG = LogFactory.getLog(JpaClientTokenServices.class);

    private ClientKeyGenerator keyGenerator = new DefaultClientKeyGenerator();
    private ClientTokenService clientTokenService;

    public void setKeyGenerator(ClientKeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication) {
        OAuth2AccessToken accessToken = null;
        try {
            List<OauthClientToken> clientTokens = clientTokenService.findByAuthenticationId(keyGenerator.extractKey(resource, authentication));
            if(clientTokens == null || clientTokens.isEmpty()){
                throw new EmptyResultDataAccessException(1);
            }
            byte[] token = clientTokens.get(0).getToken();
            accessToken = SerializationUtils.deserialize(token);
        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.debug("Failed to find access token for authentication " + authentication);
            }
        }
        return accessToken;
    }

    @Override
    public void saveAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication, OAuth2AccessToken accessToken) {
        removeAccessToken(resource, authentication);

        OauthClientToken clientToken = clientTokenService.newInstance();
        clientToken.setTokenId(accessToken.getValue());
        clientToken.setToken(SerializationUtils.serialize(accessToken));
        clientToken.setAuthenticationId(keyGenerator.extractKey(resource, authentication));
        clientToken.setUsername(authentication.getName());
        clientToken.setClientId(resource.getClientId());
        clientTokenService.save(clientToken);
    }

    @Override
    public void removeAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication) {
        try {
            List<OauthClientToken> clientTokens = clientTokenService.findByAuthenticationId(keyGenerator.extractKey(resource, authentication));
            if(clientTokens == null || clientTokens.isEmpty()){
                throw new EmptyResultDataAccessException(1);
            }
            clientTokenService.delete(clientTokens);
        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.debug("Failed to find access token for authentication " + authentication);
            }
        }
    }
}
