package com.employee.service.app.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeDatabaseServiceAppDtoTest {

    @Test
    void testGetterAndSetter() {
        // Create a new EmployeeDatabaseServiceAppDto object
        EmployeeDatabaseServiceAppDto employee = new EmployeeDatabaseServiceAppDto();

        // Set values using setters
        employee.setId(1);
        employee.setName("John Doe");
        employee.setRoleId(2);

        // Assert that the getter methods return the correct values
        assertEquals(1, employee.getId());
        assertEquals("John Doe", employee.getName());
        assertEquals(2, employee.getRoleId());
    }

    @Test
    void testToString() {
        // Create a new EmployeeDatabaseServiceAppDto object
        EmployeeDatabaseServiceAppDto employee = new EmployeeDatabaseServiceAppDto();
        employee.setId(1);
        employee.setName("John Doe");
        employee.setRoleId(2);

        // Create the expected string
        String expectedString = "EmployeeDatabaseServiceAppDto(id=1, name=John Doe, roleId=2)";

        // Verify the toString method
        assertEquals(expectedString, employee.toString());
    }

    @Test
    void testConstructor() {
        // Create an instance of EmployeeDatabaseServiceAppDto using the constructor
        EmployeeDatabaseServiceAppDto employee = new EmployeeDatabaseServiceAppDto();
        employee.setId(1);
        employee.setName("John Doe");
        employee.setRoleId(2);

        // Verify that all fields have been initialized correctly
        assertNotNull(employee);
        assertEquals(1, employee.getId());
        assertEquals("John Doe", employee.getName());
        assertEquals(2, employee.getRoleId());
    }
}

