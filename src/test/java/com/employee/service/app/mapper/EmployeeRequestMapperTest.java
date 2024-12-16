package com.employee.service.app.mapper;

import com.employee.service.app.model.EmployeeDatabaseServiceAppDto;
import com.employee.service.app.model.EmployeeServiceAppDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeRequestMapperTest {

    private EmployeeRequestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(EmployeeRequestMapper.class);
    }

    @Test
    void toEmployeeDatabaseServiceAppDto_ValidInput_ShouldMapCorrectly() {
        // Arrange
        EmployeeServiceAppDto dto = new EmployeeServiceAppDto();
        dto.setFirstName("John");
        dto.setSurname("Doe");
        dto.setRoleId(1);
        String role = "ADMIN";

        // Act
        EmployeeDatabaseServiceAppDto mappedDto = mapper.toEmployeeDatabaseServiceAppDto(dto, role);

        // Assert
        assertEquals("John Doe", mappedDto.getName());
        assertEquals(1, mappedDto.getRoleId());
    }

    @Test
    void toEmployeeServiceAppDto_ValidInput_ShouldMapCorrectly() {
        // Arrange
        EmployeeDatabaseServiceAppDto databaseDto = new EmployeeDatabaseServiceAppDto();
        databaseDto.setName("Jane Smith");
        databaseDto.setRoleId(2);

        // Act
        EmployeeServiceAppDto mappedDto = mapper.toEmployeeServiceAppDto(databaseDto);

        // Assert
        assertEquals("Jane", mappedDto.getFirstName());
        assertEquals("Smith", mappedDto.getSurname());
        assertEquals(2, mappedDto.getRoleId());
    }

    @Test
    void splitSurname_ValidInput_ShouldReturnCorrectSurname() {
        // Act & Assert
        assertEquals("Doe", mapper.splitSurname("John Doe"));
        assertEquals("", mapper.splitSurname("John")); // Single name should return empty surname
    }

    @Test
    void splitFirstName_ValidInput_ShouldReturnCorrectFirstName() {
        // Act & Assert
        assertEquals("John", mapper.splitFirstName("John Doe"));
        assertEquals("John", mapper.splitFirstName("John")); // Single name should return the name as first name
    }

    @Test
    void mapRoleHeader_ValidRoles_ShouldReturnCorrectRoleId() {
        // Act & Assert
        assertEquals(1, mapper.mapRoleHeader("ADMIN"));
        assertEquals(2, mapper.mapRoleHeader("USER"));
        assertEquals(3, mapper.mapRoleHeader("MANAGER"));
    }

    @Test
    void mapRoleHeader_InvalidRole_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mapper.mapRoleHeader("UNKNOWN"));
        assertEquals("Invalid role header value: UNKNOWN", exception.getMessage());
    }

    @Test
    void mapRoleHeader_NumberFormatException_ShouldThrowException() {
        // Simulate a NumberFormatException scenario, for instance by mocking or introducing invalid data logic.
        // In this implementation, we assume the role switch cannot directly throw NumberFormatException.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mapper.mapRoleHeader("123"));
        assertEquals("Invalid role header value: 123", exception.getMessage());
    }

    @Test
    void combineName_ValidInput_ShouldCombineCorrectly() {
        // Act & Assert
        assertEquals("John Doe", mapper.combineName("John", "Doe"));
        assertEquals("John ", mapper.combineName("John", "")); // Missing surname
        assertEquals(" Doe", mapper.combineName("", "Doe")); // Missing first name
    }
}
