package com.smartoperation;
import com.smartoperation.model.*;
import com.smartoperation.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;

@Configuration
public class DummyDataConfig {
    @Bean
    CommandLineRunner initData(UserRepository userRepo, DepartmentRepository deptRepo, TaskRepository taskRepo, TaskCommentRepository commentRepo) {
        return args -> {
            if(deptRepo.count() == 0) {
                Department d1 = new Department(); d1.setName("Engineering"); deptRepo.save(d1);
                Department d2 = new Department(); d2.setName("Marketing"); deptRepo.save(d2);
                Department d3 = new Department(); d3.setName("HR"); deptRepo.save(d3);
                Department d4 = new Department(); d4.setName("Finance"); deptRepo.save(d4);
                
                AppUser owner = new AppUser(); owner.setName("CEO Jane Doe"); owner.setEmail("ceo@smart.com"); owner.setRole("OWNER"); userRepo.save(owner);
                
                AppUser m1 = new AppUser(); m1.setName("Alice (Eng Mgr)"); m1.setEmail("alice@smart.com"); m1.setRole("MANAGER"); m1.setDepartment(d1); userRepo.save(m1);
                AppUser m2 = new AppUser(); m2.setName("Bob (Mkt Mgr)"); m2.setEmail("bob@smart.com"); m2.setRole("MANAGER"); m2.setDepartment(d2); userRepo.save(m2);
                AppUser m3 = new AppUser(); m3.setName("Charlie (HR Mgr)"); m3.setEmail("charlie@smart.com"); m3.setRole("MANAGER"); m3.setDepartment(d3); userRepo.save(m3);
                
                AppUser e1 = new AppUser(); e1.setName("Dev Dan"); e1.setEmail("dan@smart.com"); e1.setRole("EMPLOYEE"); e1.setDepartment(d1); e1.setManager(m1); userRepo.save(e1);
                AppUser e2 = new AppUser(); e2.setName("QA Eve"); e2.setEmail("eve@smart.com"); e2.setRole("EMPLOYEE"); e2.setDepartment(d1); e2.setManager(m1); userRepo.save(e2);
                AppUser e3 = new AppUser(); e3.setName("SEO Frank"); e3.setEmail("frank@smart.com"); e3.setRole("EMPLOYEE"); e3.setDepartment(d2); e3.setManager(m2); userRepo.save(e3);
                
                // Tasks
                Task t1 = new Task(); t1.setTitle("Migrate to AWS"); t1.setStatus("IN_PROGRESS"); t1.setPriority("HIGH"); t1.setAssignee(e1); t1.setDepartment(d1); t1.setDueDate(LocalDateTime.now().plusDays(2)); taskRepo.save(t1);
                Task t2 = new Task(); t2.setTitle("Q3 Advertising"); t2.setStatus("OPEN"); t2.setPriority("MEDIUM"); t2.setAssignee(e3); t2.setDepartment(d2); t2.setDueDate(LocalDateTime.now().minusDays(1)); taskRepo.save(t2); // Overdue
                Task t3 = new Task(); t3.setTitle("Hire New Devs"); t3.setStatus("OPEN"); t3.setPriority("HIGH"); t3.setAssignee(m3); t3.setDepartment(d3); t3.setDueDate(LocalDateTime.now().plusDays(10)); taskRepo.save(t3);
                Task t4 = new Task(); t4.setTitle("Audit Expenses"); t4.setStatus("COMPLETED"); t4.setPriority("LOW"); t4.setAssignee(owner); t4.setDepartment(d4); t4.setDueDate(LocalDateTime.now().minusDays(5)); taskRepo.save(t4);
                
                // Comments
                TaskComment tc1 = new TaskComment(); tc1.setTask(t1); tc1.setAuthor(m1); tc1.setContent("How is the DB migration going?"); commentRepo.save(tc1);
                TaskComment tc2 = new TaskComment(); tc2.setTask(t1); tc2.setAuthor(e1); tc2.setContent("Almost done, running final tests."); commentRepo.save(tc2);
            }
        };
    }
}
