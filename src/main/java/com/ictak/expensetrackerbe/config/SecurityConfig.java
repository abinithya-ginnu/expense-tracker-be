package com.ictak.expensetrackerbe.config;

import com.ictak.expensetrackerbe.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/addexpense").authenticated()
                .antMatchers("/addincome").authenticated()
                .antMatchers("/monthlysummary").authenticated()
                .antMatchers("/categoricalexpenses").authenticated()
                .antMatchers("/cashflow").authenticated()
                .antMatchers("/recenttransactions").authenticated()
                .antMatchers("/profile").authenticated()
                .antMatchers("/profile/update").authenticated()
                .antMatchers("/insights").authenticated()
                .antMatchers("/signup", "/login", "/emailexists", "/codeexists","/categories","/paymenttypes","/insights").permitAll()  // Allow sign-up and login without authentication
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);  // Stateless session management

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}