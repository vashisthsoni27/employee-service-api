package com.employee.service.app.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceAppDtoTest {

    @Test
    void testGetterAndSetter() {
        // Create a new EmployeeServiceAppDto object
        EmployeeServiceAppDto employee = new EmployeeServiceAppDto();

        // Set values using setters
        employee.setId(1);
        employee.setFirstName("John");
        employee.setSurname("Doe");
        employee.setRoleId(2);

        // Assert that the getter methods return the correct values
        assertEquals(1, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getSurname());
        assertEquals(2, employee.getRoleId());
    }

    @Test
    void testToString() {
        // Create a new EmployeeServiceAppDto object
        EmployeeServiceAppDto employee = new EmployeeServiceAppDto();
        employee.setId(1);
        employee.setFirstName("John");
        employee.setSurname("Doe");
        employee.setRoleId(2);

        // Create the expected string representation
        String expectedString = "EmployeeServiceAppDto(id=1, firstName=John, surname=Doe, roleId=2)";

        // Verify the toString method
        assertEquals(expectedString, employee.toString());
    }

    @Test
    void testConstructor() {
        // Create an instance of EmployeeServiceAppDto using the setter methods
        EmployeeServiceAppDto employee = new EmployeeServiceAppDto();
        employee.setId(1);
        employee.setFirstName("John");
        employee.setSurname("Doe");
        employee.setRoleId(2);

        // Verify that all fields have been initialized correctly
        assertNotNull(employee);
        assertEquals(1, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getSurname());
        assertEquals(2, employee.getRoleId());
    }
}

