package com.pm.notificationapp.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

    @Override
    public JwtAuthenticationToken convert(Jwt source) {
        Map<String, Object> realms = source.getClaimAsMap("realm_access");

        if(realms == null || realms.isEmpty() || realms.get("roles") == null) {
            return new JwtAuthenticationToken(source, List.of());
        }

        List<String> roles = (List<String>) realms.get("roles");

        if(roles == null || roles.isEmpty()) {
            return new JwtAuthenticationToken(source, List.of());
        }

        return new JwtAuthenticationToken(
                source,
                roles.stream()
                        .map(role -> "ROLE_" + role)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
    }
}
