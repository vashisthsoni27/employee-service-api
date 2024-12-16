package com.employee.service.app.service;

import com.employee.service.app.mapper.EmployeeRequestMapper;
import com.employee.service.app.model.EmployeeDatabaseServiceAppDto;
import com.employee.service.app.model.EmployeeServiceAppDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeMappingServiceTest {

    @Mock
    private EmployeeRequestMapper employeeRequestMapper;

    @InjectMocks
    private EmployeeMappingService employeeMappingService;

    private EmployeeServiceAppDto employeeServiceAppDto;
    private EmployeeDatabaseServiceAppDto employeeDatabaseServiceAppDto;

    @BeforeEach
    void setUp() {
        employeeServiceAppDto = new EmployeeServiceAppDto();
        employeeServiceAppDto.setFirstName("John");
        employeeServiceAppDto.setSurname("Doe");
        employeeServiceAppDto.setRoleId(1);

        employeeDatabaseServiceAppDto = new EmployeeDatabaseServiceAppDto();
        employeeDatabaseServiceAppDto.setName("John Doe");
        employeeDatabaseServiceAppDto.setRoleId(1);
    }

    @Test
    void toEmployeeDatabaseServiceAppDto_ShouldReturnMappedDto() {
        String role = "ADMIN";
        EmployeeDatabaseServiceAppDto result = employeeMappingService.toEmployeeDatabaseServiceAppDto(employeeServiceAppDto, role);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void toEmployeeServiceAppDto_ShouldReturnMappedDto() {
        EmployeeServiceAppDto result = employeeMappingService.toEmployeeServiceAppDto(employeeDatabaseServiceAppDto);
        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getSurname());
    }
}
