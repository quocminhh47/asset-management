package com.nashtech.assetmanagement.sercurity.config;

import com.nashtech.assetmanagement.sercurity.jwt.AuthenticationEntryPointHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    AuthenticationEntryPointHandler authenticationEntryPointHandler;

    @Autowired
    public WebSecurityConfig(AuthenticationEntryPointHandler authenticationEntryPointHandler) {
        this.authenticationEntryPointHandler = authenticationEntryPointHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/**").permitAll()
                .and().exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointHandler)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                        .authorizeRequests()
                // .antMatchers("/admin/**").hasAuthority(
                           //     "ADMIN").antMatchers("/user/**").hasAuthority("USER")
                .antMatchers("/auth/**").permitAll();



        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html/**");
    }

    @Bean
    public AuthenticationEntryPointHandler unauthorizedHandler() {
        return new AuthenticationEntryPointHandler();
    }

}
