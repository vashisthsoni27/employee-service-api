package com.employee.service.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class EmployeeServiceApplicationTest {

    @Test
    void mainMethodRunsWithoutExceptions() {
        // Verifies that the main method runs without throwing any exceptions.
        assertDoesNotThrow(() -> {
            String[] args = {};
            EmployeeServiceApplication.main(args);
        });
    }
}

