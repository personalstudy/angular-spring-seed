package org.personal.mason.ass.common.auditing;

import org.personal.mason.ass.common.authority.model.User;
import org.personal.mason.ass.common.authority.service.UserService;
import org.personal.mason.ass.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Created by mason on 6/30/14.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<User>, ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(SpringSecurityAuditorAware.class);

    @Autowired
    private UserService userService;
    private User systemUser;

    @Override
    public User getCurrentAuditor() {
        String currentLogin = SecurityUtils.getCurrentLogin();
        if(currentLogin == null){
            return systemUser;
        } else {
            try {
                return userService.loadUserByUsername(currentLogin);
            } catch (Exception e) {
            }
        }

        logger.info(String.format("Current auditor is >>> %s", systemUser));
        return systemUser;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (this.systemUser == null) {
            logger.info("%s >>> loading system user");
            systemUser = this.userService.findSystemUser();
        }
    }
}
