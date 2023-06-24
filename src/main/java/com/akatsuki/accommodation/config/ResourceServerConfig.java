package com.akatsuki.accommodation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/v1/accommodation/per-host").hasRole("HOST")
                        .requestMatchers(HttpMethod.POST, "/api/v1/accommodation").hasRole("HOST")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/accommodation/{id}/default-price/{price}").hasRole("HOST")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/accommodation/{id}").hasRole("HOST")
                        .requestMatchers(HttpMethod.POST, "/api/v1/accommodation/{id}/custom-price").hasRole("HOST")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/accommodation/custom-price/{id}").hasRole("HOST")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/accommodation/{id}/custom-price/{idOfPrice}").hasRole("HOST")
                        .requestMatchers(HttpMethod.POST, "/api/v1/accommodation/{id}/check-availability").hasRole("GUEST")
                        .requestMatchers(HttpMethod.POST, "/api/v1/accommodation/{id}/availability").hasRole("HOST")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/accommodation/availability/{id}").hasRole("HOST")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/accommodation/{id}/availability/{idOfAvailability").hasRole("HOST")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                        )
                );
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtGrantedAuthoritiesConverter gac = new JwtGrantedAuthoritiesConverter();
        gac.setAuthoritiesClaimName("roles");
        gac.setAuthorityPrefix("ROLE_");

        final JwtAuthenticationConverter jac = new JwtAuthenticationConverter();
        jac.setJwtGrantedAuthoritiesConverter(gac);
        return jac;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withIssuerLocation(issuer).build();
    }

}