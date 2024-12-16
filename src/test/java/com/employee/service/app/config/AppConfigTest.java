package com.employee.service.app.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AppConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testRestTemplateBeanCreation() {
        RestTemplate restTemplate = applicationContext.getBean(RestTemplate.class);
        assertNotNull(restTemplate, "RestTemplate bean should not be null");
    }
}

