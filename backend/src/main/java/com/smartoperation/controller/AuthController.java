package com.smartoperation.controller;

import com.smartoperation.model.AppUser;
import com.smartoperation.repository.DepartmentRepository;
import com.smartoperation.repository.UserRepository;
import com.smartoperation.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        AppUser user = userRepository.findByEmail(credentials.get("email")).orElse(null);
        if (user != null && credentials.get("password").equals("password")) { // Mock password check
            String token = jwtUtil.generateToken(user.getEmail());
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> data) {
        AppUser user = new AppUser();
        user.setName(data.get("name"));
        user.setEmail(data.get("email"));
        user.setRole(data.get("role"));
        user.setPasswordHash("password"); // Mock hash
        
        if(data.containsKey("departmentId") && data.get("departmentId") != null && !data.get("departmentId").isEmpty()) {
            user.setDepartment(departmentRepository.findById(Long.parseLong(data.get("departmentId"))).orElse(null));
        }
        
        AppUser saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getEmail());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", saved);
        return ResponseEntity.ok(response);
    }
}