package org.personal.mason.ass.common.oauth2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.personal.mason.ass.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;


@ActiveProfiles(profiles = {"local"})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        AppConfig.class, PropertiesConfig.class, DataSourceConfig.class, SecurityConfiguration.class, OAuth2ServerConfig.class
})
public class JpaClientDetailsServiceTest {

    @Autowired
    private JpaClientDetailsService clientDetailsService;

    @Test
    public void testLoadClientByClientId() throws Exception {

    }

    @Test
    public void testAddClientDetails() throws Exception {
        BaseClientDetails clientDetails = new BaseClientDetails(
                "test",
                "resource",
                "scope",
                "authorization_code,refresh_token",
                "ROLE_CLIENT, ROLE_USER",
                "http://localhost/"
        );
        clientDetails.setAccessTokenValiditySeconds(3600);
        clientDetails.setRefreshTokenValiditySeconds(3600*24*30);
        clientDetails.setClientSecret("rdisfun");
        clientDetailsService.addClientDetails(clientDetails);

    }

    @Test
    public void testUpdateClientDetails() throws Exception {

    }

    @Test
    public void testUpdateClientSecret() throws Exception {

    }

    @Test
    public void testRemoveClientDetails() throws Exception {

    }

    @Test
    public void testListClientDetails() throws Exception {

    }
}