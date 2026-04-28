package com.smartoperation.repository;
import com.smartoperation.model.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
    List<TaskComment> findByTaskIdOrderByCreatedAtDesc(Long taskId);
}
