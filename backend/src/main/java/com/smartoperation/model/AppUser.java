package com.smartoperation.model;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private String role; // OWNER, ASSISTANT, MANAGER, EMPLOYEE
    @ManyToOne
    private Department department;
    @ManyToOne
    private AppUser manager;
}
