package com.employee.service.app.mapper;

import com.employee.service.app.model.EmployeeDatabaseServiceAppDto;
import com.employee.service.app.model.EmployeeServiceAppDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeeRequestMapper {

    EmployeeRequestMapper INSTANCE = Mappers.getMapper(EmployeeRequestMapper.class);

    @Mapping(target = "name", expression = "java(combineName(employeeServiceAppRequest.getFirstName(), employeeServiceAppRequest.getSurname()))")
    @Mapping(target = "roleId", source = "role", qualifiedByName = "mapRoleHeader")
    EmployeeDatabaseServiceAppDto toEmployeeDatabaseServiceAppDto(EmployeeServiceAppDto employeeServiceAppRequest, String role);

    @Mapping(target = "firstName", expression = "java(splitFirstName(employeeDatabaseServiceAppDto.getName()))")
    @Mapping(target = "surname", expression = "java(splitSurname(employeeDatabaseServiceAppDto.getName()))")
    @Mapping(source = "roleId", target = "roleId")
    EmployeeServiceAppDto toEmployeeServiceAppDto(EmployeeDatabaseServiceAppDto employeeDatabaseServiceAppDto);

    default String splitSurname(String name) {
        String[] parts = name.split(" ", 2);
        return parts.length > 1 ? parts[1] : "";
    }

    default String splitFirstName(String name) {
        String[] parts = name.split(" ", 2);
        return parts.length > 0 ? parts[0] : "";
    }

    @Named("mapRoleHeader")
    @NotNull(message = "Role cannot be null")
    @Size(min = 3, max = 20, message = "Role must be between 3 and 20 characters")
    default Integer mapRoleHeader(String role) {
        try {
            switch (role) {
                case "ADMIN":
                    return 1;
                case "USER":
                    return 2;
                case "MANAGER":
                    return 3;
            }

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid role header value: " + role);
        }
        throw new IllegalArgumentException("Invalid role header value: " + role);
    }

    default String combineName(String firstName, String surname) {
        return firstName + " " + surname;
    }
}

