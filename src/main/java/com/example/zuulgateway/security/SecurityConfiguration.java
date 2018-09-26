package com.example.zuulgateway.security;

import com.example.zuulgateway.security.filter.CustomBasicTokenFilter;
import com.example.zuulgateway.security.filter.CustomSimpleTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/tokens").permitAll()
                .anyRequest().authenticated()
            .and()
                .addFilterBefore(new CustomBasicTokenFilter(authenticationManager()), BasicAuthenticationFilter.class)
                .addFilterAfter(new CustomSimpleTokenFilter(), CustomBasicTokenFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource  dataSource, JdbcTemplate jdbcTemplate) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT id, password, enable FROM users WHERE username=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT  userId, authority FROM authorities WHERE userId=?");
        jdbcUserDetailsManager.setGroupAuthoritiesByUsernameQuery("SELECT userId, authority FROM authorities WHERE userId=?");
        jdbcUserDetailsManager.setDataSource(dataSource);
        jdbcUserDetailsManager.setJdbcTemplate(jdbcTemplate);
        return jdbcUserDetailsManager;
    }
}
