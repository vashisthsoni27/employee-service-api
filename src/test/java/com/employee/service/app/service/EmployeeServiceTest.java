package com.employee.service.app.service;

import com.employee.service.app.model.EmployeeDatabaseServiceAppDto;
import com.employee.service.app.model.EmployeeServiceAppDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeMappingService employeeMappingService;

    @Mock
    private RestTemplate restTemplate;

    private static final String EMPLOYEE_API_BASE_URL = "http://localhost:8082/api/employees";

    private EmployeeServiceAppDto employeeServiceAppDto;
    private EmployeeDatabaseServiceAppDto employeeDatabaseServiceAppDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create sample EmployeeServiceAppDto for testing
        employeeServiceAppDto = new EmployeeServiceAppDto();
        employeeServiceAppDto.setId(1);
        employeeServiceAppDto.setFirstName("John");
        employeeServiceAppDto.setSurname("Doe");
        employeeServiceAppDto.setRoleId(2);

        // Create sample EmployeeDatabaseServiceAppDto for testing
        employeeDatabaseServiceAppDto = new EmployeeDatabaseServiceAppDto();
        employeeDatabaseServiceAppDto.setId(1);
        employeeDatabaseServiceAppDto.setName("John Doe");
        employeeDatabaseServiceAppDto.setRoleId(2);
    }

    @Test
    void getAllEmployees_shouldReturnListOfEmployees() {
        // Given
        EmployeeDatabaseServiceAppDto[] employeeArray = {employeeDatabaseServiceAppDto};
        when(restTemplate.getForEntity(EMPLOYEE_API_BASE_URL, EmployeeDatabaseServiceAppDto[].class))
                .thenReturn(ResponseEntity.ok(employeeArray));

        when(employeeMappingService.toEmployeeServiceAppDto(any(EmployeeDatabaseServiceAppDto.class)))
                .thenReturn(employeeServiceAppDto);

        // When
        List<EmployeeServiceAppDto> employees = employeeService.getAllEmployees();

        // Then
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("John", employees.get(0).getFirstName());
        assertEquals("Doe", employees.get(0).getSurname());
    }

    @Test
    void getEmployeeById_shouldReturnEmployee() {
        // Given
        when(restTemplate.getForObject(EMPLOYEE_API_BASE_URL + "/1", EmployeeDatabaseServiceAppDto.class))
                .thenReturn(employeeDatabaseServiceAppDto);

        when(employeeMappingService.toEmployeeServiceAppDto(employeeDatabaseServiceAppDto))
                .thenReturn(employeeServiceAppDto);

        // When
        EmployeeServiceAppDto employee = employeeService.getEmployeeById(1L);

        // Then
        assertNotNull(employee);
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getSurname());
    }

    @Test
    void createEmployee_shouldReturnCreatedEmployee() {
        // Given
        when(employeeMappingService.toEmployeeDatabaseServiceAppDto(any(), any()))
                .thenReturn(employeeDatabaseServiceAppDto);
        when(restTemplate.postForObject(EMPLOYEE_API_BASE_URL, employeeDatabaseServiceAppDto, EmployeeDatabaseServiceAppDto.class))
                .thenReturn(employeeDatabaseServiceAppDto);
        when(employeeMappingService.toEmployeeServiceAppDto(employeeDatabaseServiceAppDto))
                .thenReturn(employeeServiceAppDto);

        // When
        EmployeeServiceAppDto createdEmployee = employeeService.createEmployee(employeeServiceAppDto, "admin");

        // Then
        assertNotNull(createdEmployee);
        assertEquals("John", createdEmployee.getFirstName());
        assertEquals("Doe", createdEmployee.getSurname());
    }

    @Test
    void updateEmployee_ShouldReturnUpdatedEmployee() {
        // Given
        Long employeeId = 1L;
        String role = "admin";

        // Input DTO
        EmployeeServiceAppDto inputEmployeeDto = new EmployeeServiceAppDto();
        inputEmployeeDto.setId(1);
        inputEmployeeDto.setFirstName("John");
        inputEmployeeDto.setSurname("Doe");
        inputEmployeeDto.setRoleId(1);

        // Mocked database DTO
        EmployeeDatabaseServiceAppDto mockDatabaseEmployeeDto = new EmployeeDatabaseServiceAppDto();
        mockDatabaseEmployeeDto.setId(1);
        mockDatabaseEmployeeDto.setName("John Doe");
        mockDatabaseEmployeeDto.setRoleId(1);

        // Expected Output DTO
        EmployeeServiceAppDto expectedEmployeeDto = new EmployeeServiceAppDto();
        expectedEmployeeDto.setId(1);
        expectedEmployeeDto.setFirstName("John");
        expectedEmployeeDto.setSurname("Doe");
        expectedEmployeeDto.setRoleId(1);

        // Mock the mapping service to map between the DTOs
        when(employeeMappingService.toEmployeeDatabaseServiceAppDto(inputEmployeeDto, role))
                .thenReturn(mockDatabaseEmployeeDto);
        when(employeeMappingService.toEmployeeServiceAppDto(mockDatabaseEmployeeDto))
                .thenReturn(expectedEmployeeDto);

        // Mock the RestTemplate behavior
        doNothing().when(restTemplate).put(eq(EMPLOYEE_API_BASE_URL + "/1"), eq(mockDatabaseEmployeeDto));
        when(restTemplate.getForObject(eq(EMPLOYEE_API_BASE_URL + "/1"), eq(EmployeeDatabaseServiceAppDto.class)))
                .thenReturn(mockDatabaseEmployeeDto);

        // When
        EmployeeServiceAppDto actualEmployeeDto = employeeService.updateEmployee(employeeId, inputEmployeeDto, role);

        // Then
        assertNotNull(actualEmployeeDto); // Ensure the result is not null
        assertEquals(expectedEmployeeDto.getId(), actualEmployeeDto.getId());
        assertEquals(expectedEmployeeDto.getFirstName(), actualEmployeeDto.getFirstName());
        assertEquals(expectedEmployeeDto.getSurname(), actualEmployeeDto.getSurname());
        assertEquals(expectedEmployeeDto.getRoleId(), actualEmployeeDto.getRoleId());

        // Verify interactions with mocks
        verify(restTemplate).put(eq(EMPLOYEE_API_BASE_URL + "/1"), eq(mockDatabaseEmployeeDto));
        verify(restTemplate).getForObject(eq(EMPLOYEE_API_BASE_URL + "/1"), eq(EmployeeDatabaseServiceAppDto.class));
        verify(employeeMappingService).toEmployeeDatabaseServiceAppDto(inputEmployeeDto, role);
        verify(employeeMappingService).toEmployeeServiceAppDto(mockDatabaseEmployeeDto);
    }

    @Test
    void deleteEmployee_shouldReturnTrueWhenDeleted() {
        // Given
        when(restTemplate.getForObject(EMPLOYEE_API_BASE_URL + "/1", Boolean.class))
                .thenReturn(true);

        // When
        Boolean isDeleted = employeeService.deleteEmployee(1L);

        // Then
        assertTrue(isDeleted);
    }

    @Test
    void deleteEmployee_shouldReturnFalseWhenNotDeleted() {
        // Given
        when(restTemplate.getForObject(EMPLOYEE_API_BASE_URL + "/1", Boolean.class))
                .thenReturn(false);

        // When
        Boolean isDeleted = employeeService.deleteEmployee(1L);

        // Then
        assertFalse(isDeleted);
    }

    // Retry Test Cases

    @Test
    void getAllEmployees_shouldRetryOnHttpServerErrorException() {
        // Given
        when(restTemplate.getForEntity(EMPLOYEE_API_BASE_URL, EmployeeDatabaseServiceAppDto[].class))
                .thenThrow(new HttpServerErrorException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR));

        // When & Then
        assertThrows(HttpServerErrorException.class, () -> employeeService.getAllEmployees());
    }

    @Test
    void getAllEmployees_shouldRetryOnResourceAccessException() {
        // Given
        when(restTemplate.getForEntity(EMPLOYEE_API_BASE_URL, EmployeeDatabaseServiceAppDto[].class))
                .thenThrow(new ResourceAccessException("Timeout"));

        // When & Then
        assertThrows(ResourceAccessException.class, () -> employeeService.getAllEmployees());
    }
}
