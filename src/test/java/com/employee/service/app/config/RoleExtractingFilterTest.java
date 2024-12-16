package com.employee.service.app.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RoleExtractingFilterTest {

    private RoleExtractingFilter roleExtractingFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        roleExtractingFilter = new RoleExtractingFilter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = Mockito.mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSetAuthenticationWhenRoleHeaderIsPresent() throws ServletException, IOException {
        String role = "ADMIN";
        request.addHeader("X-User-Role", role);

        roleExtractingFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication, "Authentication should be set in SecurityContext");
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")), "Role should match 'ROLE_ADMIN'");
        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotSetAuthenticationWhenRoleHeaderIsAbsent() throws ServletException, IOException {
        roleExtractingFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(), "Authentication should be null when role header is absent");
        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldPassControlToNextFilterInChain() throws ServletException, IOException {
        roleExtractingFilter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain).doFilter(request, response);
    }
}
