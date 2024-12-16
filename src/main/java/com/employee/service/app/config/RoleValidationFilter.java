package com.employee.service.app.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Set;

@Component
@Order(1) // Adjust to match your URL pattern
public class RoleValidationFilter extends OncePerRequestFilter {

    private static final Set<String> ALLOWED_ROLES = Set.of("ADMIN", "USER", "MANAGER");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String role = request.getHeader("X-User-Role");

        if (role == null || role.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Role header is missing.");
            return;
        }

        if (role.length() < 3 || role.length() > 50) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Role length must be between 3 and 50 characters.");
            return;
        }

        if (!ALLOWED_ROLES.contains(role.toUpperCase())) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid role. Allowed roles are ADMIN, USER, MANAGER.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

