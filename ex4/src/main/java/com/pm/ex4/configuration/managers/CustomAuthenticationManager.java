package com.pm.ex4.configuration.managers;

import com.pm.ex4.configuration.authentication.ApiKeyAuthentication;
import com.pm.ex4.configuration.providers.ApiKeyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final String key;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var provider = new ApiKeyProvider(key);

        if(provider.supports(authentication.getClass())){
            return Objects.requireNonNull(provider.authenticate(authentication));
        }

        throw new BadCredentialsException("Bad credentials");
    }
}
