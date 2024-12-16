package com.employee.service.app.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpServerErrorException;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Map;

@WebMvcTest(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(globalExceptionHandler).build();
    }

    @Test
    void testHandleGenericException() {
        // Test for generic exception
        Exception exception = new Exception("Generic exception");
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        ResponseEntity<Map<String, String>> responseEntity = exceptionHandler.handleGenericException(exception);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, String> body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals("Generic exception", body.get("message"));
    }

    @Test
    void testHandleHttpServerErrorException() {
        HttpServerErrorException exception = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error occurred");
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        ResponseEntity<Map<String, String>> responseEntity = exceptionHandler.handleHttpServerErrorException(exception);

        // Assertions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Map<String, String> body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals("500 Server error occurred", body.get("message"));
    }
}

