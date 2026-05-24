package com.pm.ex7.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
//PreAuthorize, PostAuthorize, PreFilter, PostFilter
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var uManager = new InMemoryUserDetailsManager();

        var u = User.withUsername("alisher")
                .password(passwordEncoder().encode("12345"))
                .authorities("read")
                .build();

        var u2 = User.withUsername("bob")
                .password(passwordEncoder().encode("12345"))
                .authorities("write")
                .build();

        uManager.createUser(u);
        uManager.createUser(u2);

        return uManager;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().authenticated();
                })
                .build();
    }
}
