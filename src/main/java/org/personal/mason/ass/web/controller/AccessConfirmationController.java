package org.personal.mason.ass.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.Map;

import java.security.Principal;

/**
 * Created by mason on 8/3/14.
 */
@Controller
@SessionAttributes("authorizationRequest")
public class AccessConfirmationController {
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private ApprovalStore approvalStore;

    @RequestMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, Principal principal) throws Exception {
        AuthorizationRequest clientAuth = (AuthorizationRequest) model.remove("authorizationRequest");
        ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
        model.put("auth_request", clientAuth);
        model.put("client", client);
        Map<String, String> scopes = new LinkedHashMap<String, String>();
        for (String scope : clientAuth.getScope()) {
            scopes.put(OAuth2Utils.SCOPE_PREFIX + scope, "false");
        }
        approvalStore.getApprovals(principal.getName(), client.getClientId()).stream().filter(approval -> clientAuth.getScope().contains(approval.getScope())).forEach(approval -> {
            scopes.put(OAuth2Utils.SCOPE_PREFIX + approval.getScope(),
                    approval.getStatus() == Approval.ApprovalStatus.APPROVED ? "true" : "false");
        });
        model.put("scopes", scopes);
        return new ModelAndView("access_confirmation", model);
    }

    @RequestMapping("/oauth/error")
    public String handleError(Map<String, Object> model) throws Exception {
        // We can add more stuff to the model here for JSP rendering. If the client was a machine then
        // the JSON will already have been rendered.
        model.put("message", "There was a problem with the OAuth2 protocol");
        return "oauth_error";
    }
}
