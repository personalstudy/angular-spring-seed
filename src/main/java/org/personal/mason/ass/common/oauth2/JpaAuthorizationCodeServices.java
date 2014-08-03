package org.personal.mason.ass.common.oauth2;

import org.personal.mason.ass.common.oauth2.model.OauthCode;
import org.personal.mason.ass.common.oauth2.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
@Component
public class JpaAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    @Autowired
    private CodeService codeService;

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        OauthCode oauthCode = codeService.newInstance();
        oauthCode.setAuthentication(SerializationUtils.serialize(authentication));
        oauthCode.setCode(code);
        codeService.save(oauthCode);
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        OAuth2Authentication authentication;

        try {
            List<OauthCode> queryResult = codeService.findByCode(code);
            if(queryResult == null || queryResult.isEmpty()){
                throw new EmptyResultDataAccessException(1);
            }
            authentication = SerializationUtils.deserialize(queryResult.get(0).getAuthentication());

            if (authentication != null) {
                codeService.delete(queryResult);
            }
            return authentication;
        }catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


}
