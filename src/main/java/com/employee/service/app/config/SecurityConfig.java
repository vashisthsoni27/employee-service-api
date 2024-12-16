package com.employee.service.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final RoleValidationFilter roleValidationFilter;

    public SecurityConfig(RoleValidationFilter roleValidationFilter) {
        this.roleValidationFilter = roleValidationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/swagger-ui.html").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/employees").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/employees").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/employees").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(roleValidationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new RoleExtractingFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}


