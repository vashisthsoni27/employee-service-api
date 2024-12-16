package com.employee.service.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeServiceAppDto {
    private Integer id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("role_id")
    private Integer roleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public EmployeeServiceAppDto() {}

    public String toString() {
        return "EmployeeServiceAppDto(id=" + id + ", firstName=" + firstName + ", surname=" + surname + ", roleId=" + roleId + ")";
    }
}
