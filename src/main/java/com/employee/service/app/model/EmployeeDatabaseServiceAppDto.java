package com.employee.service.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

public class EmployeeDatabaseServiceAppDto {
    private Integer id;

    private String name;

    @JsonProperty("role_id")
    private Integer roleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public EmployeeDatabaseServiceAppDto() {}

    public String toString() {
        return "EmployeeDatabaseServiceAppDto(id=" + id + ", name=" + name + ", roleId=" + roleId + ")";
    }
}
