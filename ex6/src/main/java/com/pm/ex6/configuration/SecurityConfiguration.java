package com.pm.ex6.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var uManager = new InMemoryUserDetailsManager();

        var u = User.withUsername("alisher")
                .password(passwordEncoder().encode("password"))
                .authorities("read")
                .build();

        var u2 = User.withUsername("john")
                .password(passwordEncoder().encode("password"))
                .authorities("write", "delete")
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
//                    auth.requestMatchers("/test1").authenticated();
//                    auth.requestMatchers("/test2").hasAuthority("read");
                    auth.requestMatchers(HttpMethod.GET, "/demo/**").hasAuthority("read");
                    auth.anyRequest().authenticated();
                    //can do /** and /demo/*/routeName
                })
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
