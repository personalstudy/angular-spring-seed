package org.personal.mason.ass.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	public final String HEADER_SECURITY_TOKEN = "X-Token"; 
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    
	protected TokenAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
		super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(defaultFilterProcessesUrl));
//		setAuthenticationManager(new NoOpAuthenticationManager());
//		setAuthenticationSuccessHandler(new TokenS);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {
		String token = request.getHeader(HEADER_SECURITY_TOKEN);
		logger.info("token found:" + token);
		AbstractAuthenticationToken authenticationToken = authenticationWithToken(token);
		if(authenticationToken == null){
			throw new AuthenticationServiceException(MessageFormat.format("Error | {0}", "Bad Token"));
		}
		
		return authenticationToken;
	}

	private AbstractAuthenticationToken authenticationWithToken(String token) {
		if (token == null){
			return null;
		}
//		AbstractAuthenticationToken authenticationToken = new ;
		return null;
	}

}
