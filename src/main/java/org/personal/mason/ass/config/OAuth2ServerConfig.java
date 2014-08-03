package org.personal.mason.ass.config;

import org.personal.mason.ass.common.oauth2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;

/**
 * Created by mason on 8/3/14.
 */
@Configuration
public class OAuth2ServerConfig {

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private JpaClientDetailsService jpaClientDetailsService;

        @Autowired
        private JpaApprovalStore jpaApprovalStore;

        @Autowired
        private JpaAuthorizationCodeServices jpaAuthorizationCodeServices;

        @Autowired
        private JpaClientDetailsServiceBuilder jpaClientDetailsServiceBuilder;

        @Autowired
        private JpaTokenStore jpaTokenStore;

        @Bean
        public UserApprovalHandler userApprovalHandler(){
            MyUserApprovalHandler userApprovalHandler = new MyUserApprovalHandler();
            userApprovalHandler.setClientDetailsService(jpaClientDetailsService);
            userApprovalHandler.setUseApprovalStore(true);
            userApprovalHandler.setApprovalStore(jpaApprovalStore);
            userApprovalHandler.setRequestFactory(new DefaultOAuth2RequestFactory(jpaClientDetailsService));
            return userApprovalHandler;
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.approvalStore(jpaApprovalStore)
                    .authenticationManager(authenticationManager)
                    .clientDetailsService(jpaClientDetailsService)
                    .authorizationCodeServices(jpaAuthorizationCodeServices)
                    .tokenStore(jpaTokenStore)
                    .userApprovalHandler(userApprovalHandler())
            ;
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.setBuilder(jpaClientDetailsServiceBuilder);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer.realm("sparklr2/client");
        }

    }


}
