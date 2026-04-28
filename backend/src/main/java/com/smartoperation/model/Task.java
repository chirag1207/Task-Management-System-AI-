package com.smartoperation.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@Entity
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition="TEXT")
    private String description;
    private String status;
    private String priority;
    @Column(columnDefinition="TEXT")
    private String aiAnalysis;
    @ManyToOne
    private AppUser assignee;
    @ManyToOne
    private Department department;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime dueDate;
}
