package org.personal.mason.ass.common.rememberme;

import org.apache.commons.lang.time.DateUtils;
import org.personal.mason.ass.common.authority.JPAUserDetailsService;
import org.personal.mason.ass.common.authority.model.User;
import org.personal.mason.ass.common.authority.service.UserService;
import org.personal.mason.ass.common.rememberme.model.RememberMeToken;
import org.personal.mason.ass.common.rememberme.service.RememberMeTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mason on 7/1/14.
 */
@Service
public class JPARememberMeService extends AbstractRememberMeServices {
    private static final String DEFAULT = "remember_me_key";
    private static final String KEY = "fl.security.rememberme.key";
    private final Logger logger = LoggerFactory.getLogger(JPARememberMeService.class);

    private static final int TOKEN_VALIDITY_DAYS = 31;
    private static final int TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * TOKEN_VALIDITY_DAYS;
    private static final int DEFAULT_TOKEN_LENGTH = 16;

    private SecureRandom random;

    @Autowired
    public JPARememberMeService(Environment env, JPAUserDetailsService userDetailsService){
        super(env.getProperty(KEY, DEFAULT), userDetailsService);
        random = new SecureRandom();
    }

    @Autowired
    private UserService userService;

    @Autowired
    private RememberMeTokenService rememberMeTokenService;


    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        String login = successfulAuthentication.getName();

        User user = userService.loadUserByUsername(login);
        RememberMeToken token = rememberMeTokenService.newInstance();
        token.setUser(user);
        token.setTokenValue(generateTokenData());
        token.setTokenDate(Calendar.getInstance().getTime());
        token.setIpAddress(request.getRemoteAddr());
        token.setUserAgent(request.getHeader("User-Agent"));
        try{
            rememberMeTokenService.save(token);
            addCookie(token, request, response);
        } catch (DataAccessException e){
            logger.error("Failed to save persistent token ", e);
        }
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) throws RememberMeAuthenticationException, UsernameNotFoundException {

        RememberMeToken token = getPersistentToken(cookieTokens);
        String login = token.getUser().getUsername();

        logger.debug("Refreshing persistent login token for user '{}', series '{}'", login, token.getId());
        token.setTokenDate(Calendar.getInstance().getTime());
        token.setTokenValue(generateTokenData());
        token.setIpAddress(request.getRemoteAddr());
        token.setUserAgent(request.getHeader("User-Agent"));

        try {
            rememberMeTokenService.saveAndFlush(token);
            addCookie(token, request, response);
        } catch (DataAccessException e) {
            logger.error("Failed to update token: ", e);
            throw new RememberMeAuthenticationException("Autologin failed due to data access problem", e);
        }
        return getUserDetailsService().loadUserByUsername(login);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String rememberMeCookie = extractRememberMeCookie(request);
        if (rememberMeCookie != null && rememberMeCookie.length() != 0) {
            try {
                String[] cookieTokens = decodeCookie(rememberMeCookie);
                RememberMeToken token = getPersistentToken(cookieTokens);
                rememberMeTokenService.delete(token);
            } catch (InvalidCookieException ice) {
                logger.info("Invalid cookie, no persistent token could be deleted");
            } catch (RememberMeAuthenticationException rmae) {
                logger.debug("No persistent token found, so no token could be deleted");
            }
        }
        super.logout(request, response, authentication);
    }

    private RememberMeToken getPersistentToken(String[] cookieTokens) {
        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2 +
                    " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }

        final Long presentedId = new Long(cookieTokens[0]);
        final String presentedToken = cookieTokens[1];

        RememberMeToken token = rememberMeTokenService.findOne(presentedId);

        if (token == null) {
            // No series match, so we can't authenticate using this cookie
            throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedId);
        }

        // We have a match for this user/series combination
        logger.info("presentedToken={} / tokenValue={}", presentedToken, token.getTokenValue());
        if (!presentedToken.equals(token.getTokenValue())) {
            // Token doesn't match series value. Delete this session and throw an exception.
            rememberMeTokenService.delete(token);
            throw new CookieTheftException("Invalid rememberme-me token (Series/token) mismatch. Implies previous cookie theft attack.");
        }

        Date date = token.getTokenDate();
        if (DateUtils.addDays(date, TOKEN_VALIDITY_DAYS).before(Calendar.getInstance().getTime())){
            rememberMeTokenService.delete(token);
            throw new RememberMeAuthenticationException("Remember-me login has expired");
        }
        return token;
    }
    
    private void addCookie(RememberMeToken token, HttpServletRequest request, HttpServletResponse response) {
        setCookie(
                new String[]{token.getId().toString(), token.getTokenValue()},
                TOKEN_VALIDITY_SECONDS, request, response);
    }

    private String generateTokenData() {
        byte[] newToken = new byte[DEFAULT_TOKEN_LENGTH];
        random.nextBytes(newToken);
        return new String(Base64.encode(newToken));
    }

}
