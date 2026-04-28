package com.smartoperation.dto;
import lombok.Data;
@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private String role; // OWNER, MANAGER, EMPLOYEE
    private Long departmentId;
}
