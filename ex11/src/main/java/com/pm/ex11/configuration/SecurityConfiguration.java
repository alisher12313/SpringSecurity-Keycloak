package com.pm.ex11.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
//                .csrf(AbstractHttpConfigurer::disable) //Do if you have stateless server for like jwt
                .csrf(c -> c.ignoringRequestMatchers("/ignored/**"))
                //can use that if you have some specific endpoint that you may allow csrf ignore some mutating function
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().permitAll(); //even if permitAll if
                    // the request is for mutation csrf wont allow it since its protected
                    // and send 403
                })
                .build();
    }
}
