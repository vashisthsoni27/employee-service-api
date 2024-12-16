package com.employee.service.app.controller;

import com.employee.service.app.model.EmployeeServiceAppDto;
import com.employee.service.app.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Operation(summary = "Get all employees", description = "Retrieve a list of all employees.")
    @ApiResponse(responseCode = "200", description = "List of employees retrieved successfully")
    @GetMapping
    public List<EmployeeServiceAppDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @Operation(summary = "Get Employee by ID", description = "Fetch an employee by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeServiceAppDto.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public EmployeeServiceAppDto getEmployeeById(@PathVariable("id") Long id) {
        return employeeService.getEmployeeById(id);
    }

    @Operation(summary = "Create a new employee", description = "Create a new employee with the provided details.")
    @ApiResponse(responseCode = "201", description = "Employee created successfully")
    @PostMapping
    public EmployeeServiceAppDto createEmployee(@RequestHeader("X-User-Role") String role, @RequestBody EmployeeServiceAppDto employeeServiceAppDto) throws Throwable {
        return employeeService.createEmployee(employeeServiceAppDto, role);
    }

    @Operation(summary = "Update an existing employee", description = "Update an employee's details by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeServiceAppDto.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PutMapping("/{id}")
    public EmployeeServiceAppDto updateEmployee(@PathVariable("id") Long id, @RequestHeader("X-User-Role") String role, @RequestBody EmployeeServiceAppDto employee) throws Throwable {
        return employeeService.updateEmployee(id, employee, role);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable("id") Long id) {
        Boolean isDeleted = employeeService.deleteEmployee(id);
        Map<String, String> response = new HashMap<>();
        if (Boolean.TRUE.equals(isDeleted)) {
            response.put("message", "Employee deleted successfully");
            return ResponseEntity.ok(response);  // HTTP 200
        } else {
            response.put("message", "Employee not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // HTTP 404
        }
    }
}

