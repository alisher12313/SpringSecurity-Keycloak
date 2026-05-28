package com.pm.ex8.configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

@Configuration
public class SecurityConfiguration {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServiceSecurity(HttpSecurity http) throws Exception {
        // This creates the Authorization Server configuration.
        // It enables OAuth2 endpoints like:
        // /oauth2/authorize
        // /oauth2/token
        // /oauth2/jwks
        OAuth2AuthorizationServerConfigurer configuration = OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                // This security filter chain works only for Authorization Server endpoints.
                // Example: /oauth2/authorize, /oauth2/token, /oauth2/jwks
                .securityMatcher(configuration.getEndpointsMatcher())

                // Apply Authorization Server configuration to Spring Security.
                // oidc() enables OpenID Connect support.
                // This is needed for scopes like openid and for ID token.
                .with(configuration, authServer -> {
                    authServer.oidc(Customizer.withDefaults());
                })
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().authenticated();
                })

                // If user is not logged in and tries to access /oauth2/authorize,
                // redirect them to /login page.
                .exceptionHandling(e -> {
                    e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
                });

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().authenticated();
                })
                .build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        var u = User.withUsername("user")
//                .password(passwordEncoder().encode("password"))
//                .authorities("read")
//                .build();
//
//        return new InMemoryUserDetailsManager(u);
//    } Actual bean of UserDetailsService for user is not in CustomUserDetailsService

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public RegisteredClientRepository registeredClientRepository() {
//        // This creates one OAuth2 client application.
//        // The client is not the human user.
//        // The client is the app that requests tokens from Authorization Server.
//        RegisteredClient r1 = RegisteredClient.withId(UUID.randomUUID().toString())
//
//                // OAuth2 client id.
//                // Used in /oauth2/authorize URL as client_id=client.
//                .clientId("client")
//
//                // OAuth2 client secret.
//                // Used in Postman Basic Auth when calling /oauth2/token.
//                .clientSecret(passwordEncoder().encode("secret"))
//
//                // Allowed OIDC/OAuth2 scopes.
//                // openid is required for OpenID Connect.
//                // profile asks for basic user profile info.
//                .scope(OidcScopes.OPENID)
//                .scope(OidcScopes.PROFILE)
//
//                // Where Authorization Server redirects after login.
//                // Must exactly match redirect_uri in your authorization URL.
//                .redirectUri("https://springone.io/authorized")
//
//                // Client must authenticate with Basic Auth at /oauth2/token.
//                // Username: client
//                // Password: secret
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//
//                // This client can use Authorization Code flow.
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//
//                // This client can receive refresh tokens.
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .tokenSettings(TokenSettings.builder()
//                        // Access token will expire after 15 minutes
//                        .accessTokenTimeToLive(Duration.ofSeconds(900))
//                        // makes it OPAQUE token
//                        //means: Do not create a readable JWT access token.
//                        //Create a random-looking token value.
//                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
//                        .build())
//                .build();
//
//        return new InMemoryRegisteredClientRepository(r1);
//    } again replaced by CustomClientService

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){
        // Uses default Authorization Server settings.
        // Default endpoints include:
        // /oauth2/authorize
        // /oauth2/token
        // /oauth2/jwks
        // /.well-known/openid-configuration
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
        // Create RSA key generator.
        // RSA is used for signing JWT tokens.
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

        // Key size. 2048 is common and secure enough for learning.
        generator.initialize(2048);

        // Generate public/private key pair.
        KeyPair keyPair = generator.generateKeyPair();

        // Public key can be shared.
        // Other services use it to verify JWT signature.
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // Private key must stay secret.
        // Authorization Server uses it to sign JWT tokens.
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        // Convert RSA keys into JWK format.
        // JWK = JSON Web Key.
        RSAKey key = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)

                // Key ID. JWT header can contain this ID.
                .keyID(UUID.randomUUID().toString())
                .build();

        // Put the key into a key set.
        JWKSet set = new JWKSet(key);

        // Give this key set to Spring Authorization Server.
        // Spring uses it to sign JWT tokens and expose public key at /oauth2/jwks.
        return new ImmutableJWKSet<>(set);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
        // This runs before Spring creates/signs the JWT.
        // You can add custom claims into the token here.
        return context -> {
            context.getClaims().claim("test", "test");
        };
    }
}
