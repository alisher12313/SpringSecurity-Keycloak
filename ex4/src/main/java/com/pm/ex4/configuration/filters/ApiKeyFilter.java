package com.pm.ex4.configuration.filters;

import com.pm.ex4.configuration.authentication.ApiKeyAuthentication;
import com.pm.ex4.configuration.managers.CustomAuthenticationManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${app.secret}")
    private final String key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CustomAuthenticationManager manager = new CustomAuthenticationManager(key);
        var requestKey = request.getHeader("x-api-key");
        if (requestKey == null || requestKey.isBlank()) {
            filterChain.doFilter(request, response);
        }
        var auth = new ApiKeyAuthentication(requestKey);

        try {
            manager.authenticate(auth);

            if(auth.isAuthenticated()){
                SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
            }
        }catch (AuthenticationException e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
