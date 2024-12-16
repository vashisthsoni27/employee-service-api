package com.employee.service.app.controller;

import com.employee.service.app.model.EmployeeServiceAppDto;
import com.employee.service.app.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private EmployeeServiceAppDto mockEmployeeServiceAppDto;

    @BeforeEach
    void setUp() {
        mockEmployeeServiceAppDto = new EmployeeServiceAppDto();
        mockEmployeeServiceAppDto.setId(1);
        mockEmployeeServiceAppDto.setFirstName("John");
        mockEmployeeServiceAppDto.setSurname("Doe");
        mockEmployeeServiceAppDto.setRoleId(2);
    }

    private static final String X_USER_ROLE_HEADER = "X-User-Role";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String USER_ROLE = "USER";

    @Test
    void getEmployeeById_ShouldReturnEmployee_WhenRoleIsAdmin() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(mockEmployeeServiceAppDto);

        mockMvc.perform(get("/api/employees/{id}", 1)
                        .header(X_USER_ROLE_HEADER, ADMIN_ROLE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expecting OK when role is admin
                .andExpect(jsonPath("$.first_name").value("John"));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees() throws Exception {
        List<EmployeeServiceAppDto> mockEmployees = Collections.singletonList(mockEmployeeServiceAppDto);
        when(employeeService.getAllEmployees()).thenReturn(mockEmployees);

        ResultActions result = mockMvc.perform(get("/api/employees")
                .header(X_USER_ROLE_HEADER, ADMIN_ROLE)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].first_name").value("John"));
        verify(employeeService, times(1)).getAllEmployees();
    }


    @Test
    void getEmployeeById_ShouldReturnEmployee() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(mockEmployeeServiceAppDto);

        ResultActions result = mockMvc.perform(get("/api/employees/{id}", 1)
                .header(X_USER_ROLE_HEADER, ADMIN_ROLE)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value("John"));
        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void createEmployee_ShouldReturnCreatedEmployee() throws Exception {
        String content = "{\"id\": 1, \"first_name\": \"John\", \"surname\": \"Doe\", \"role_id\": 2}";
        content = content.trim();
        when(employeeService.createEmployee(mockEmployeeServiceAppDto, ADMIN_ROLE)).thenReturn(mockEmployeeServiceAppDto);

        ResultActions result = mockMvc.perform(post("/api/employees")
                .header(X_USER_ROLE_HEADER, ADMIN_ROLE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        result.andExpect(status().isOk());
    }

    @Test
    void updateEmployee_ShouldReturnUpdatedEmployee() throws Exception {
        when(employeeService.updateEmployee(1L, mockEmployeeServiceAppDto, ADMIN_ROLE)).thenReturn(mockEmployeeServiceAppDto);

        ResultActions result = mockMvc.perform(put("/api/employees/{id}", 1)
                .header(X_USER_ROLE_HEADER, ADMIN_ROLE)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, \"first_name\": \"John\", \"surname\": \"Doe\", \"role_id\": 2}"));

        result.andExpect(status().isOk());
    }

    @Test
    void deleteEmployee_ShouldReturnSuccessResponse() throws Exception {
        when(employeeService.deleteEmployee(1L)).thenReturn(true);

        ResultActions result = mockMvc.perform(delete("/api/employees/{id}", 1)
                .header(X_USER_ROLE_HEADER, ADMIN_ROLE)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee deleted successfully"));
        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    void deleteEmployee_ShouldReturnNotFoundResponse() throws Exception {
        when(employeeService.deleteEmployee(2L)).thenReturn(false);

        ResultActions result = mockMvc.perform(delete("/api/employees/{id}", 2)
                .header(X_USER_ROLE_HEADER, USER_ROLE)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"));
        verify(employeeService, times(1)).deleteEmployee(2L);
    }
}
