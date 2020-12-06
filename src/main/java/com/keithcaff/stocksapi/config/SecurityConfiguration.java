package com.keithcaff.stocksapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login() //Comment out to disable Okta login form when unauthorized endpoints accessed
                .and()
                .oauth2ResourceServer()
                .jwt(); //call to jwt() using the oauth2ResourceServer() to indicate the type of tokens supported by our server here.

    }
}
