package com.employee.service.app.config;

import com.employee.service.app.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private static final String X_USER_ROLE_HEADER = "X-User-Role";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String USER_ROLE = "USER";

    @BeforeEach
    void setUp() {
        // Setup if needed, like mocking services or security filters
    }

    @Test
    void testAdminAccessToPostEmployees() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .header(X_USER_ROLE_HEADER, ADMIN_ROLE)
                        .contentType("application/json")
                        .content("{\"firstName\": \"John\", \"surname\": \"Doe\", \"roleId\": 2}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUserCannotAccessPostEmployees() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .header(X_USER_ROLE_HEADER, USER_ROLE)
                        .contentType("application/json")
                        .content("{\"firstName\": \"John\", \"surname\": \"Doe\", \"roleId\": 2}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUserCannotAccessDeleteEmployees() throws Exception {
        mockMvc.perform(delete("/api/employees")
                        .header(X_USER_ROLE_HEADER, USER_ROLE))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAccessToAnyOtherUrlRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/other-endpoint"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAdminRoleValidation() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .header(X_USER_ROLE_HEADER, ADMIN_ROLE)
                        .contentType("application/json")
                        .content("{\"firstName\": \"John\", \"surname\": \"Doe\", \"roleId\": 2}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUserRoleValidation() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .header(X_USER_ROLE_HEADER, USER_ROLE)
                        .contentType("application/json")
                        .content("{\"firstName\": \"John\", \"surname\": \"Doe\", \"roleId\": 2}"))
                .andExpect(status().isForbidden());
    }
}
