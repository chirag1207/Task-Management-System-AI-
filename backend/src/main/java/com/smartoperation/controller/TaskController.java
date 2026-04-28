package com.smartoperation.controller;
import com.smartoperation.model.Task;
import com.smartoperation.model.TaskComment;
import com.smartoperation.repository.TaskRepository;
import com.smartoperation.repository.TaskCommentRepository;
import com.smartoperation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin("*")
public class TaskController {
    @Autowired private TaskRepository taskRepository;
    @Autowired private TaskCommentRepository commentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SimpMessagingTemplate messagingTemplate;
    
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        if(task.getDueDate() == null) {
            task.setDueDate(LocalDateTime.now().plusDays(3)); // Default 3 days
        }
        Task saved = taskRepository.save(task);
        messagingTemplate.convertAndSend("/topic/alerts", "{\"message\":\"New Task Created: " + saved.getTitle() + "\"}");
        return saved;
    }

    @PutMapping("/{id}/status")
    public Task updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Task t = taskRepository.findById(id).orElseThrow();
        t.setStatus(body.get("status"));
        Task saved = taskRepository.save(t);
        messagingTemplate.convertAndSend("/topic/alerts", "{\"message\":\"Task Updated: " + saved.getTitle() + "\"}");
        return saved;
    }

    @GetMapping("/{id}/comments")
    public List<TaskComment> getComments(@PathVariable Long id) {
        return commentRepository.findByTaskIdOrderByCreatedAtDesc(id);
    }

    @PostMapping("/{id}/comments")
    public TaskComment addComment(@PathVariable Long id, @RequestBody Map<String, String> body) {
        TaskComment tc = new TaskComment();
        tc.setTask(taskRepository.findById(id).orElseThrow());
        tc.setAuthor(userRepository.findById(Long.parseLong(body.get("authorId"))).orElseThrow());
        tc.setContent(body.get("content"));
        return commentRepository.save(tc);
    }
}