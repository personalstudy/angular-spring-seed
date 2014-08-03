package org.personal.mason.ass.common.oauth2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.personal.mason.ass.common.oauth2.model.OauthClient;
import org.personal.mason.ass.common.oauth2.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by mason on 8/3/14.
 */
@Component
public class JpaClientDetailsService implements ClientDetailsService, ClientRegistrationService {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ClientService clientService;
    private PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

    private JsonMapper jsonMapper = createJsonMapper();

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Client Details Service
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        try {
            List<OauthClient> clients = clientService.findByClientId(clientId);
            if(clients == null || clients.isEmpty()){
                throw new EmptyResultDataAccessException(1);
            }
            OauthClient oauthClient = clients.get(0);
            BaseClientDetails clientDetails = toClientDetails(oauthClient);
            return clientDetails;
        }
        catch (EmptyResultDataAccessException e) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }

    // Client Registration Service
    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        try {
            OauthClient client = clientService.newInstance();
            client.setClientId(clientDetails.getClientId());
            String clientSecret = clientDetails.getClientSecret();
            clientSecret = passwordEncoder.encode(clientSecret);
            client.setSecret(clientSecret);
            mergeToClient(client, clientDetails);
            clientService.save(client);
        }
        catch (DuplicateKeyException e) {
            throw new ClientAlreadyExistsException("Client already exists: " + clientDetails.getClientId(), e);
        }
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        try {
            List<OauthClient> clients = clientService.findByClientId(clientDetails.getClientId());
            if(clients == null || clients.isEmpty()){
                throw new EmptyResultDataAccessException(1);
            }
            OauthClient oauthClient = clients.get(0);
            mergeToClient(oauthClient, clientDetails);
            clientService.saveAndFlush(oauthClient);
        }
        catch (EmptyResultDataAccessException e) {
            throw new NoSuchClientException("No client with requested id: " + clientDetails.getClientId());
        }
    }

    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        try {
            List<OauthClient> clients = clientService.findByClientId(clientId);
            if(clients == null || clients.isEmpty()){
                throw new EmptyResultDataAccessException(1);
            }
            OauthClient oauthClient = clients.get(0);
            oauthClient.setSecret(passwordEncoder.encode(secret));
            clientService.saveAndFlush(oauthClient);
        }
        catch (EmptyResultDataAccessException e) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        try {
            List<OauthClient> clients = clientService.findByClientId(clientId);
            if(clients == null || clients.isEmpty()){
                throw new EmptyResultDataAccessException(1);
            }
            clientService.delete(clients);
        }
        catch (EmptyResultDataAccessException e) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        List<OauthClient> clients = clientService.findAll();
        List<ClientDetails> clientDetailsList = new ArrayList<>();
        for(OauthClient client : clients){
            ClientDetails clientDetails = toClientDetails(client);
            clientDetailsList.add(clientDetails);
        }
        return clientDetailsList;
    }


    //===
    private BaseClientDetails toClientDetails(OauthClient client){
        if(client == null){
            return null;
        }
        BaseClientDetails clientDetails = new BaseClientDetails(client.getClientId(),
                client.getResourceIds(),
                client.getScopes(),
                client.getGrantTypes(),
                client.getAuthorities(),
                client.getRedirectUris());
        clientDetails.setClientSecret(client.getSecret());

        clientDetails.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
        clientDetails.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
        String additionalInformation = client.getAdditionalInformation();
        if(additionalInformation != null){
            try {
                clientDetails.setAdditionalInformation(jsonMapper.read(additionalInformation, Map.class));
            }
            catch (Exception e) {
                logger.warn("Could not decode JSON for additional information: " + clientDetails, e);
            }
        }

        if(client.getAutoApprove() != null){
            clientDetails.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(client.getScopes()));
        }

        return clientDetails;
    }

    private void mergeToClient(OauthClient client, ClientDetails clientDetails){
        String json = null;
        try {
            json = jsonMapper.write(clientDetails.getAdditionalInformation());
        }
        catch (Exception e) {
            logger.warn("Could not serialize additional information: " + clientDetails, e);
        }
        client.setResourceIds(clientDetails.getResourceIds() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails.getResourceIds()) : null);
        client.setScopes(clientDetails.getScope() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails.getScope()) : null);
        client.setGrantTypes(clientDetails.getAuthorizedGrantTypes() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails.getAuthorizedGrantTypes()) : null);
        client.setRedirectUris(clientDetails.getRegisteredRedirectUri() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails.getRegisteredRedirectUri()) : null);
        client.setAuthorities(clientDetails.getAuthorities() != null ? StringUtils.collectionToCommaDelimitedString(clientDetails.getAuthorities()) : null);
        client.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds());
        client.setRefreshTokenValiditySeconds(clientDetails.getRefreshTokenValiditySeconds());
        client.setAdditionalInformation(json);
        client.setAutoApprove(getAutoApproveScopes(clientDetails));
    }

    private String getAutoApproveScopes(ClientDetails clientDetails) {
        if (clientDetails.isAutoApprove("true")) {
            return "true"; // all scopes autoapproved
        }
        Set<String> scopes = clientDetails.getScope().stream().filter(scope -> clientDetails.isAutoApprove(scope)).collect(Collectors.toSet());
        return StringUtils.collectionToCommaDelimitedString(scopes);
    }

    //============
    interface JsonMapper {
        String write(Object input) throws Exception;

        <T> T read(String input, Class<T> type) throws Exception;
    }

    private static JsonMapper createJsonMapper() {
        if (ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper", null)) {
            return new JacksonMapper();
        }
        else if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", null)) {
            return new Jackson2Mapper();
        }
        return new NotSupportedJsonMapper();
    }

    private static class JacksonMapper implements JsonMapper {
        private org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();

        @Override
        public String write(Object input) throws Exception {
            return mapper.writeValueAsString(input);
        }

        @Override
        public <T> T read(String input, Class<T> type) throws Exception {
            return mapper.readValue(input, type);
        }
    }

    private static class Jackson2Mapper implements JsonMapper {
        private com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

        @Override
        public String write(Object input) throws Exception {
            return mapper.writeValueAsString(input);
        }

        @Override
        public <T> T read(String input, Class<T> type) throws Exception {
            return mapper.readValue(input, type);
        }
    }

    private static class NotSupportedJsonMapper implements JsonMapper {
        @Override
        public String write(Object input) throws Exception {
            throw new UnsupportedOperationException(
                    "Neither Jackson 1 nor 2 is available so JSON conversion cannot be done");
        }

        @Override
        public <T> T read(String input, Class<T> type) throws Exception {
            throw new UnsupportedOperationException(
                    "Neither Jackson 1 nor 2 is available so JSON conversion cannot be done");
        }
    }
}

