package com.employee.service.app.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class RetryCountService {

    private static final String RETRY_FILE = "retry_count.txt";

    private int retryCount;

    public int incrementRetryCount() {
        this.retryCount++; // Increment retry count in memory
        updateRetryCountInFile();  // Persist retry count to the file
        return this.retryCount;
    }

    private void updateRetryCountInFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RETRY_FILE))) {
            writer.write(String.valueOf(this.retryCount));  // Write retry count to the file
            writer.flush();
            System.out.println("Retry count updated to: " + this.retryCount);
        } catch (IOException e) {
            System.err.println("Error writing to retry count file: " + e.getMessage());
        }
    }

    @PostConstruct
    public void loadRetryCountFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RETRY_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                this.retryCount = Integer.parseInt(line);  // Load retry count from the file
            }
            System.out.println("Loaded retry count: " + this.retryCount);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading retry count file, starting with count 0: " + e.getMessage());
            this.retryCount = 0;  // Default to 0 if file doesn't exist or there's an error
        }
    }

    public void resetRetryCount() {
        this.retryCount = 0;
        updateRetryCountInFile();  // Persist reset retry count to file
        System.out.println("Retry count reset.");
    }
}
