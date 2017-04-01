package com.csrf.anti.security;

import com.csrf.anti.security.csrf.AntiCSRFAuthenticationProvider;
import com.csrf.anti.security.csrf.AntiCSRFProcessingFilter;
import com.csrf.anti.security.tools.SkipPathRequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

/**
 * The main security configuration of the app
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final String LOGIN_ENDPOINT = "/api/user/login";
    private final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";

    @Autowired
    private AuthenticationManager authenticationManager;
    private AntiCSRFAuthenticationProvider antiCSRFAuthenticationProvider;

    @Autowired
    public WebSecurityConfig(AntiCSRFAuthenticationProvider antiCSRFAuthenticationProvider) {
        this.antiCSRFAuthenticationProvider = antiCSRFAuthenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.antiCSRFAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .and()
                .authorizeRequests().antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated()
                .and()
                .addFilterBefore(buildAntiCSRFProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private AntiCSRFProcessingFilter buildAntiCSRFProcessingFilter() throws Exception {
        SkipPathRequestMatcher matcher = generateListOfUnAuthenticatedPathsToSkip();
        AntiCSRFProcessingFilter filter = new AntiCSRFProcessingFilter(matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    private SkipPathRequestMatcher generateListOfUnAuthenticatedPathsToSkip() {
        List<String> pathsToSkip = Arrays.asList(LOGIN_ENDPOINT);
        return new SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT);
    }
}
