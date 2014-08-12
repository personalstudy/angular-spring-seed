package org.personal.mason.ass.common.oauth2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.personal.mason.ass.config.*;
import org.personal.mason.ass.domain.model.authentication.AGroup;
import org.personal.mason.ass.domain.model.authentication.ARole;
import org.personal.mason.ass.domain.repository.authentication.GroupRepository;
import org.personal.mason.ass.domain.repository.oauth2.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;


@ActiveProfiles(profiles = {"local"})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        AppConfig.class, PropertiesConfig.class, DataSourceConfig.class, SecurityConfiguration.class, OAuth2ServerConfig.class
})
public class JpaClientDetailsServiceTest {

    @Autowired
    private JpaClientDetailsService clientDetailsService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private GroupRepository groupRepository;

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

        AGroup group = new AGroup();
        group.setName("group1");
        group.setEnabled(true);
//        ARole aRole = new ARole();
//        aRole.setName("USER");
//        group.getRoles().add(aRole);

        List<AGroup> byExample = groupRepository.findByExample(group);
        assertEquals(byExample.size(), 1);

        group = new AGroup();
        group.setName("group");
        group.setEnabled(true);
        byExample = groupRepository.findByExample(group);
        assertEquals(byExample.size(), 0);

        group = new AGroup();
        group.setEnabled(true);
        ARole aRole = new ARole();
        aRole.setName("USER");
        group.getRoles().add(aRole);
        byExample = groupRepository.findByExample(group);
        assertEquals(byExample.size(), 1);

        group = new AGroup();
        group.setEnabled(true);
        aRole = new ARole();
        aRole.setName("USER1");
        group.getRoles().add(aRole);
        byExample = groupRepository.findByExample(group);
        assertEquals(byExample.size(), 0);
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