package com.smartoperation.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@Entity
public class TaskComment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Task task;
    @ManyToOne
    private AppUser author;
    @Column(columnDefinition="TEXT")
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();
}
