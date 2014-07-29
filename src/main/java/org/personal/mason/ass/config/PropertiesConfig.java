package org.personal.mason.ass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by mason on 7/13/14.
 */
@Configuration
@PropertySource({
        "classpath:db/db.${spring.profiles.active:local}.properties",
        "classpath:config/config.${spring.profiles.active:local}.properties"
})
public class PropertiesConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }

}
