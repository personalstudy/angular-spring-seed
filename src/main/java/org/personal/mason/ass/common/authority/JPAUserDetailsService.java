package org.personal.mason.ass.common.authority;

import org.personal.mason.ass.common.authority.model.User;
import org.personal.mason.ass.common.authority.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by mason on 6/30/14.
 */
public class JPAUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(JPAUserDetailsService.class);
    protected final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Autowired
    private UserService userService;

    private boolean enableGroups = true;

    public void setEnableGroups(boolean enableGroups) {
        this.enableGroups = enableGroups;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = loadUserDetailsByUsername(username);

        if (userDetails == null) {
            logger.debug(String.format("Query returned no result for account '%s'", username));

            throw new UsernameNotFoundException(
                    messages.getMessage("JPAUserDetailsService.notFound", new Object[]{username}, "Username {0} not found"));
        }

        addCustomAuthorities(username, userDetails);

        return userDetails;
    }

    protected void addCustomAuthorities(String username, UserDetails authorities) {}

    protected UserDetails loadUserDetailsByUsername(String username){
        User user =  userService.loadUserByUsername(username);

        if(user != null){
            JPAUserDetails userDetail = new JPAUserDetails(user, enableGroups);
            return userDetail;
        }

        return null;
    }
}
