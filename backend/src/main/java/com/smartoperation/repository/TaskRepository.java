package com.smartoperation.repository;
import com.smartoperation.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByDepartmentId(Long deptId);
    List<Task> findByAssigneeId(Long userId);
}
