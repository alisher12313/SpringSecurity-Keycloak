package com.pm.ex7.configuration.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class Demo4ConditionEvaluator {

    public boolean hasAnyAuthorityReadAndCorrectUsername(String username, Authentication authentication) {
        if(authentication == null || !authentication.isAuthenticated()){
            return false;
        }

        String logged = authentication.getName();
        boolean correctUsername = logged.equals(username);

        boolean authoritiesRead = authentication.getAuthorities()
                .stream()
                .anyMatch(
                        authority -> authority.getAuthority().equals("read")
                );

        return correctUsername && authoritiesRead;
    }
}
