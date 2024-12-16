package com.employee.service.app.service;

import com.employee.service.app.mapper.EmployeeRequestMapper;
import com.employee.service.app.model.EmployeeDatabaseServiceAppDto;
import com.employee.service.app.model.EmployeeServiceAppDto;
import org.springframework.stereotype.Service;

@Service
public class EmployeeMappingService {

    public EmployeeDatabaseServiceAppDto toEmployeeDatabaseServiceAppDto(EmployeeServiceAppDto employee, String role) {
        return EmployeeRequestMapper.INSTANCE.toEmployeeDatabaseServiceAppDto(employee, role);
    }

    public EmployeeServiceAppDto toEmployeeServiceAppDto(EmployeeDatabaseServiceAppDto employeeDto) {
        return EmployeeRequestMapper.INSTANCE.toEmployeeServiceAppDto(employeeDto);
    }

}
