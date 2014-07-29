package org.personal.mason.ass.config;

import org.personal.mason.ass.common.authority.JpaUserDetailsDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * Created by mason on 7/13/14.
 */
@Configuration
@ComponentScan(basePackages = "org.personal.mason.ass")
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new StandardPasswordEncoder();
    }

    @Bean
    public JpaUserDetailsDefaults jpaUserDetailsDefaults(){
        return new JpaUserDetailsDefaults.NullJpaUserDetailsDefaults();
    }
}

