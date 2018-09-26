package com.example.zuulgateway.security;

import com.example.zuulgateway.security.filter.CustomBasicTokenFilter;
import com.example.zuulgateway.security.filter.CustomSimpleTokenFilter;
import com.example.zuulgateway.security.provider.CustomSimpleAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfiguration {

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        private AuthenticationManagerBuilder authenticationManagerBuilder;
        @Autowired
        private CustomSimpleAuthenticationProvider simpleAuthenticationProvider;

        protected void configure(HttpSecurity http) throws Exception {
            authenticationManagerBuilder.authenticationProvider(simpleAuthenticationProvider);
            http
                .antMatcher("/api/**")
                    .authorizeRequests()
                        .anyRequest().authenticated()
                    .and()
                        .addFilterAfter(new CustomSimpleTokenFilter(authenticationManager()), BasicAuthenticationFilter.class)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }

    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/tokens")
                    .authorizeRequests()
                        .anyRequest().authenticated()
                    .and()
                        .addFilterBefore(new CustomBasicTokenFilter(authenticationManager()), BasicAuthenticationFilter.class)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource  dataSource, JdbcTemplate jdbcTemplate) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT id, password, enable FROM users WHERE ? IN (id, username)");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT  userId, authority FROM authorities WHERE userId=?");
        jdbcUserDetailsManager.setDataSource(dataSource);
        jdbcUserDetailsManager.setJdbcTemplate(jdbcTemplate);
        return jdbcUserDetailsManager;
    }

}
