package org.personal.mason.ass.config;

import org.personal.mason.ass.common.authentication.AjaxAuthenticationFailureHandler;
import org.personal.mason.ass.common.authentication.AjaxAuthenticationSuccessHandler;
import org.personal.mason.ass.common.authentication.AjaxLogoutSuccessHandler;
import org.personal.mason.ass.common.authentication.Http401UnauthorizedEntryPoint;
import org.personal.mason.ass.common.rememberme.JPARememberMeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by mason on 7/1/14.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Autowired
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Autowired
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Autowired
    private Http401UnauthorizedEntryPoint unauthorizedEntryPoint;

    @Autowired
    private JPARememberMeService rememberMeService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/")
                .antMatchers("/view**")
                .antMatchers("/");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login").permitAll().and()
            .authorizeRequests()
                .anyRequest().hasRole("USER").and()
            .exceptionHandling()
                .accessDeniedPage("/login.jsp?authorization_error=true").and()
            .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize")).disable()
            .logout()
                .logoutSuccessUrl("/index.jsp").logoutUrl("/logout.do").and()
            .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/login?authentication_error=true")
                .loginPage("/login")
                .loginProcessingUrl("/login.do");

//        http.exceptionHandling()
//                .authenticationEntryPoint(unauthorizedEntryPoint)
//                .and()
//            .rememberMe().
//                rememberMeServices(rememberMeService)
//                .key(env.getProperty("fl.security.rememberme.key", "remember_me_key"))
//                .and()
//            .formLogin()
//                .loginProcessingUrl("/user/login")
//                .successHandler(ajaxAuthenticationSuccessHandler)
//                .failureHandler(ajaxAuthenticationFailureHandler)
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .permitAll()
//                .and()
//            .logout()
//                .logoutUrl("/user/logout")
//                .logoutSuccessHandler(ajaxLogoutSuccessHandler)
//                .deleteCookies("JSESSIONID")
//                .permitAll()
//                .and()
//            .csrf()
//                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
//                .disable()
//            .headers()
//                .frameOptions().disable()
//            .authorizeRequests()
//                .antMatchers("/account/register", "/user/login", "/user/logout", "/account/exist", "/public**").permitAll()
//            .and()
//            .authorizeRequests().anyRequest().hasRole("USER").and()
        ;



    }
}
