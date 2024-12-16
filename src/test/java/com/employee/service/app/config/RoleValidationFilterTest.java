package com.employee.service.app.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.*;

public class RoleValidationFilterTest {

    @InjectMocks
    private RoleValidationFilter roleValidationFilter;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testRoleHeaderIsMissing() throws Exception {
        request.removeHeader("X-User-Role");
        roleValidationFilter.doFilterInternal(request, response, filterChain);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        assertEquals("Role header is missing.", response.getErrorMessage());
        verify(filterChain, times(0)).doFilter(request, response);
    }

    @Test
    void testRoleHeaderHasInvalidLength() throws Exception {
        request.addHeader("X-User-Role", "AD");
        roleValidationFilter.doFilterInternal(request, response, filterChain);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        assertEquals("Role length must be between 3 and 50 characters.", response.getErrorMessage());
        verify(filterChain, times(0)).doFilter(request, response);
    }

    @Test
    void testRoleHeaderHasInvalidRole() throws Exception {
        request.addHeader("X-User-Role", "INVALID_ROLE");
        roleValidationFilter.doFilterInternal(request, response, filterChain);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        assertEquals("Invalid role. Allowed roles are ADMIN, USER, MANAGER.", response.getErrorMessage());
        verify(filterChain, times(0)).doFilter(request, response);
    }

    @Test
    void testRoleHeaderIsValid() throws Exception {
        request.addHeader("X-User-Role", "ADMIN");
        roleValidationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    void testRoleHeaderIsValidWithDifferentRole() throws Exception {
        request.addHeader("X-User-Role", "USER");
        roleValidationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }
}
