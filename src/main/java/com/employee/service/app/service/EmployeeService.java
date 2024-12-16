package com.employee.service.app.service;

import com.employee.service.app.model.EmployeeDatabaseServiceAppDto;
import com.employee.service.app.model.EmployeeServiceAppDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeMappingService employeeMappingService;

    @Autowired
    private RestTemplate restTemplate;

    private static final String EMPLOYEE_API_BASE_URL = "http://localhost:8082/api/employees";

    /**
     * Helper method to build employee API URLs.
     */
    private String buildUrl(Long id) {
        return EMPLOYEE_API_BASE_URL + (id != null ? "/" + id : "");
    }

    /**
     * Fetches all employees from the database service.
     */
    @Retryable(value = { HttpServerErrorException.class, ResourceAccessException.class }, backoff = @Backoff(delay = 2000))
    public List<EmployeeServiceAppDto> getAllEmployees() {
        ResponseEntity<EmployeeDatabaseServiceAppDto[]> response =
                restTemplate.getForEntity(buildUrl(null), EmployeeDatabaseServiceAppDto[].class);

        return mapToEmployeeServiceAppDtoList(response.getBody());
    }

    /**
     * Fetches an employee by their ID.
     */
    @Retryable(value = { HttpServerErrorException.class, ResourceAccessException.class }, backoff = @Backoff(delay = 2000))
    public EmployeeServiceAppDto getEmployeeById(Long id) {
        EmployeeDatabaseServiceAppDto employeeDto =
                restTemplate.getForObject(buildUrl(id), EmployeeDatabaseServiceAppDto.class);

        return employeeMappingService.toEmployeeServiceAppDto(employeeDto);
    }

    /**
     * Creates a new employee in the database service.
     */
    @Retryable(value = { HttpServerErrorException.class, ResourceAccessException.class }, backoff = @Backoff(delay = 2000))
    public EmployeeServiceAppDto createEmployee(EmployeeServiceAppDto employee, String role) {
        EmployeeDatabaseServiceAppDto employeeDto =
                employeeMappingService.toEmployeeDatabaseServiceAppDto(employee, role);

        EmployeeDatabaseServiceAppDto createdDto =
                restTemplate.postForObject(buildUrl(null), employeeDto, EmployeeDatabaseServiceAppDto.class);

        return employeeMappingService.toEmployeeServiceAppDto(createdDto);
    }

    /**
     * Updates an existing employee.
     */
    @Retryable(value = { HttpServerErrorException.class, ResourceAccessException.class }, backoff = @Backoff(delay = 2000))
    public EmployeeServiceAppDto updateEmployee(Long id, EmployeeServiceAppDto employeeServiceAppDto, String role) {
        String url = buildUrl(id);

        EmployeeDatabaseServiceAppDto employeeDto =
                employeeMappingService.toEmployeeDatabaseServiceAppDto(employeeServiceAppDto, role);

        restTemplate.put(url, employeeDto);

        EmployeeDatabaseServiceAppDto updatedDto = restTemplate.getForObject(url, EmployeeDatabaseServiceAppDto.class);

        return employeeMappingService.toEmployeeServiceAppDto(updatedDto);
    }

    /**
     * Deletes an employee.
     */
    @Retryable(value = { HttpServerErrorException.class, ResourceAccessException.class }, backoff = @Backoff(delay = 2000))
    public Boolean deleteEmployee(Long id) {
        return restTemplate.getForObject(buildUrl(id), Boolean.class);
    }

    /**
     * Helper method to map array of EmployeeDatabaseServiceAppDto to List of EmployeeServiceAppDto.
     */
    private List<EmployeeServiceAppDto> mapToEmployeeServiceAppDtoList(EmployeeDatabaseServiceAppDto[] employeeArray) {
        return employeeArray == null
                ? List.of()
                : Arrays.stream(employeeArray)
                .map(employeeMappingService::toEmployeeServiceAppDto)
                .collect(Collectors.toList());
    }
}
