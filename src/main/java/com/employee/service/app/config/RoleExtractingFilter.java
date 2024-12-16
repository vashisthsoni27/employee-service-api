package com.employee.service.app.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RoleExtractingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String role = request.getHeader("X-User-Role");
        if (role != null) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", null, AuthorityUtils.createAuthorityList("ROLE_" + role)));
        }

        filterChain.doFilter(request, response);
    }
}



