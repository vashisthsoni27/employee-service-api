package com.employee.service.app.service;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class RetryCountServiceTest {

    private RetryCountService retryCountService;

    private static final String RETRY_FILE = "retry_count.txt";

    @BeforeEach
    void setUp() throws IOException {
        // Ensure test starts with a clean file state
        Files.deleteIfExists(Path.of(RETRY_FILE));
        retryCountService = new RetryCountService();
        retryCountService.loadRetryCountFromFile();
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up after each test
        retryCountService.resetRetryCount();
    }

    @Test
    void incrementRetryCount_ShouldIncreaseCountAndUpdateFile() throws IOException {
        // Act
        int count1 = retryCountService.incrementRetryCount();
        int count2 = retryCountService.incrementRetryCount();

        // Assert
        assertEquals(1, count1);
        assertEquals(2, count2);

        // Verify file content
        try (BufferedReader reader = new BufferedReader(new FileReader(RETRY_FILE))) {
            String fileContent = reader.readLine();
            assertEquals("2", fileContent);
        }
    }

    @Test
    void loadRetryCountFromFile_ShouldLoadCountCorrectly() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RETRY_FILE))) {
            writer.write("5");
        }

        retryCountService.loadRetryCountFromFile();
        int count = retryCountService.incrementRetryCount();
        assertEquals(6, count);
    }

    @Test
    void loadRetryCountFromFile_FileNotFound_ShouldStartWithZero() {
        retryCountService.loadRetryCountFromFile();
        assertEquals(1, retryCountService.incrementRetryCount());
    }

    @Test
    void loadRetryCountFromFile_InvalidContent_ShouldStartWithZero() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RETRY_FILE))) {
            writer.write("invalid");
        }
        retryCountService.loadRetryCountFromFile();
        assertEquals(1, retryCountService.incrementRetryCount());
    }

    @Test
    void resetRetryCount_ShouldSetCountToZeroAndUpdateFile() throws IOException {
        retryCountService.incrementRetryCount(); // Count = 1
        retryCountService.incrementRetryCount(); // Count = 2

        retryCountService.resetRetryCount();
        assertEquals(1, retryCountService.incrementRetryCount());

        try (BufferedReader reader = new BufferedReader(new FileReader(RETRY_FILE))) {
            String fileContent = reader.readLine();
            assertEquals("1", fileContent);
        }
    }
}

