package com.pm.ex2.security;

import com.pm.ex2.entity.Authority;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class SecurityAuthority implements GrantedAuthority {

    private final Authority authority;

    @Override
    public @Nullable String getAuthority() {
        return authority.getName();
    }
}
