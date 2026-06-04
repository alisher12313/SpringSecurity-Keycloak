package com.pm.eventapp.configuration;

public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(auth -> {
            auth
                    .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())
            );
        });

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/actuator/**").hasRole("eventapp-admin");
            auth.requestMatchers("/api/v1/events/**").hasAnyRole("eventapp-user", "eventapp-admin");
        });

        http.sessionManagement(session -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("http://localhost:9090/realms/proselyte");
    }
}
