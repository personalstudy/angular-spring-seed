package org.personal.mason.ass.common.authority;


import org.personal.mason.ass.common.authority.model.User;
import org.personal.mason.ass.common.authority.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * Created by mason on 6/30/14.
 */

@Service
public class JPAUserDetailsManager extends JPAUserDetailsService implements UserDetailsManager {

	private final Logger logger = LoggerFactory.getLogger(JPAUserDetailsService.class);
	
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean enableEncodePassword = true;
    
    private AuthenticationManager authenticationManager;

    private UserCache userCache = new NullUserCache();

    private JpaUserDetailsDefaults jpaUserDetailsDefaults = new JpaUserDetailsDefaults.NullJpaUserDetailsDefaults();

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJpaUserDetailsDefaults(JpaUserDetailsDefaults jpaUserDetailsDefaults) {
        this.jpaUserDetailsDefaults = jpaUserDetailsDefaults;
    }

    //~ UserDetailsManager implementation ==============================================================================
    @Override
    public void createUser(final UserDetails userDetails) {
        validateUserDetails(userDetails);

        JPAUserDetails jpaUserDetails = (JPAUserDetails) userDetails;

        User user = jpaUserDetails.getUser();

        if(enableEncodePassword){
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        setDefaultInformation(user);
        userService.save(user);
    }

    @Override
    public void updateUser(final UserDetails userDetails) {
    	validateUserDetails(userDetails);

        JPAUserDetails jpaUserDetails = (JPAUserDetails) userDetails;

        User model = jpaUserDetails.getUser();

        userService.saveAndFlush(model);

    	userCache.removeUserFromCache(userDetails.getUsername());
    }

    @Override
    public void deleteUser(String username) {
    	User model = userService.loadUserByUsername(username);
    	if(model != null){
    		userService.delete(model);
    	}
    	
    	userCache.removeUserFromCache(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
    	Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        if (currentUser == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException("Can't change password as no Authentication object found in context " +
                    "for current userDetails.");
        }
        String username = currentUser.getName();

     // If an authentication manager has been set, re-authenticate the userDetails with the supplied password.
        if (authenticationManager != null) {
            logger.debug("Reauthenticating userDetails '"+ username + "' for password change request.");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
        } else {
            logger.debug("No authentication manager set. Password won't be re-checked.");
        }

        logger.debug("Changing password for userDetails '"+ username + "'");

        User model = userService.loadUserByUsername(username);
        
        if(enableEncodePassword){
        	model.setPassword(passwordEncoder.encode(newPassword));
        } else {
            model.setPassword(newPassword);
        }
        
        SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(currentUser, newPassword));

        userCache.removeUserFromCache(username);
    }

	@Override
    public boolean userExists(String username) {
        User model = userService.loadUserByUsername(username);

        return model != null;
    }


    private void validateUserDetails(UserDetails user) {
        Assert.hasText(user.getUsername(), "Username may not be empty or null");
        Assert.isInstanceOf(JPAUserDetails.class, user, "UserDetails is not JPAUserDetails or the subclass");
        validateAuthorities(user.getAuthorities());
    }

    private void validateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Authorities list must not be null");

        for (GrantedAuthority authority : authorities) {
            Assert.notNull(authority, "Authorities list contains a null entry");
            Assert.hasText(authority.getAuthority(), "getAuthority() method must return a non-empty string");
        }
    }

    private void setDefaultInformation(User model) {
        jpaUserDetailsDefaults.initialSettings(model);
    }

    private Authentication createNewAuthentication(Authentication currentUser,
                                                   String newPassword) {
        UserDetails userDetails = super.loadUserByUsername(currentUser.getName());

        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        newAuthentication.setDetails(currentUser.getDetails());

        return newAuthentication;
    }

}
