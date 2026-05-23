package com.pm.ex5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
//                    auth.anyRequest().authenticated(); any request must be authorized
//                    auth.anyRequest().permitAll(); permit any request without authorization
//                    auth.anyRequest().denyAll();
//                    auth.anyRequest().hasAnyAuthority("read", "write");
//                    auth.anyRequest().hasRole("ADMIN");
//                    auth.anyRequest().hasAnyRole("ADMIN", "MANAGER");
                auth.requestMatchers("/demo").hasAuthority("read");
                auth.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var ulist = new InMemoryUserDetailsManager();

        var u = User.withUsername("alisher")
                .password(passwordEncoder().encode("12345"))
//                .roles("ADMIN")
                .authorities("read")
                .build();

        var u2 = User.withUsername("john")
                .password(passwordEncoder().encode("12345"))
//                .authorities("ROLE_MANAGER")
                .authorities("write")
                .build();

        ulist.createUser(u);
        ulist.createUser(u2);

        return ulist;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
