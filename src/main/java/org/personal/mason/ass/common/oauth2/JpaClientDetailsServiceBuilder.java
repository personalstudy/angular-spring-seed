package org.personal.mason.ass.common.oauth2;

import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mason on 8/3/14.
 */
public class JpaClientDetailsServiceBuilder extends ClientDetailsServiceBuilder<JpaClientDetailsServiceBuilder> {
    private Set<ClientDetails> clientDetails = new HashSet<ClientDetails>();

    @Override
    protected void addClient(String clientId, ClientDetails value) {
        clientDetails.add(value);
    }

    @Override
    protected ClientDetailsService performBuild() {
        JpaClientDetailsService clientDetailsService = new JpaClientDetailsService();
        clientDetails.forEach(clientDetailsService::addClientDetails);
        return clientDetailsService;
    }
}
