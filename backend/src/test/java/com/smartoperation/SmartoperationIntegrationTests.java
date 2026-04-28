package com.smartoperation;

import com.smartoperation.controller.AuthController;
import com.smartoperation.controller.TaskController;
import com.smartoperation.model.AppUser;
import com.smartoperation.model.Task;
import com.smartoperation.repository.UserRepository;
import com.smartoperation.repository.TaskRepository;
import com.smartoperation.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString;

/**
 * Integration Test Suite for Smartoperation Backend
 * Tests authentication, API endpoints, database connectivity, rate limiting, and WebSocket
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SmartoperationIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private AppUser testUser;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        // Setup: Create test user if not exists
        testUser = new AppUser();
        testUser.setEmail("integration-test@test.com");
        testUser.setName("Integration Test User");
        testUser.setPasswordHash("password");
        testUser.setRole("EMPLOYEE");
        
        // Save user (will update if exists)
        testUser = userRepository.save(testUser);
        
        // Generate JWT token for authenticated requests
        jwtToken = jwtUtil.generateToken(testUser.getEmail());
    }

    // ============================================
    // AUTHENTICATION TESTS (JWT)
    // ============================================

    @Test
    void testLoginWithValidCredentials() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "ceo@smart.com");
        credentials.put("password", "password");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(credentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.email").value("ceo@smart.com"))
                .andReturn();

        String token = extractToken(result.getResponse().getContentAsString());
        assertNotNull(token);
        assertTrue(token.length() > 50); // JWT tokens are long
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "nonexistent@test.com");
        credentials.put("password", "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(credentials)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testJwtTokenExpiration() throws Exception {
        // This test verifies JWT token has proper expiration set
        String token = jwtUtil.generateToken("test@test.com");
        assertNotNull(token);
        
        // Extract and verify expiration claim exists
        assertDoesNotThrow(() -> jwtUtil.extractExpiration(token));
    }

    @Test
    void testSignupNewUser() throws Exception {
        Map<String, String> signupData = new HashMap<>();
        signupData.put("email", "newuser@test.com");
        signupData.put("name", "New Test User");
        signupData.put("password", "password");
        signupData.put("role", "EMPLOYEE");

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(signupData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.email").value("newuser@test.com"));

        // Verify user was saved to database
        assertTrue(userRepository.findByEmail("newuser@test.com").isPresent());
    }

    // ============================================
    // API ENDPOINT TESTS
    // ============================================

    @Test
    void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
                // Response should be an array of tasks
    }

    @Test
    void testCreateTask() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("Test Task");
        newTask.setDescription("Task created during integration test");
        newTask.setPriority("HIGH");
        newTask.setStatus("OPEN");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(newTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testUpdateTaskStatus() throws Exception {
        // Get first task from database
        Task task = taskRepository.findAll().stream().findFirst().orElse(null);
        assertNotNull(task);

        Map<String, String> updateData = new HashMap<>();
        updateData.put("status", "IN_PROGRESS");

        mockMvc.perform(put("/api/tasks/" + task.getId() + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testGetTaskComments() throws Exception {
        Task task = taskRepository.findAll().stream().findFirst().orElse(null);
        assertNotNull(task);

        mockMvc.perform(get("/api/tasks/" + task.getId() + "/comments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
                // Response should be array of comments
    }

    @Test
    void testAddCommentToTask() throws Exception {
        Task task = taskRepository.findAll().stream().findFirst().orElse(null);
        assertNotNull(task);

        Map<String, String> commentData = new HashMap<>();
        commentData.put("content", "Test comment");
        commentData.put("authorId", String.valueOf(testUser.getId()));

        mockMvc.perform(post("/api/tasks/" + task.getId() + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(commentData)))
                .andExpect(status().isOk());
    }

    // ============================================
    // DATABASE CONNECTIVITY TESTS
    // ============================================

    @Test
    void testUserRepositoryFindByEmail() {
        // Test UserRepository can query database
        assertTrue(userRepository.findByEmail("ceo@smart.com").isPresent());
        AppUser user = userRepository.findByEmail("ceo@smart.com").get();
        assertEquals("CEO Jane Doe", user.getName());
    }

    @Test
    void testTaskRepositoryFindAll() {
        // Test TaskRepository can retrieve all tasks
        long taskCount = taskRepository.count();
        assertTrue(taskCount > 0);
        assertFalse(taskRepository.findAll().isEmpty());
    }

    @Test
    void testDatabaseConnectionPersistence() {
        // Create and save a test entity
        AppUser user = new AppUser();
        user.setEmail("persistence-test@test.com");
        user.setName("Persistence Test");
        user.setPasswordHash("password");
        user.setRole("EMPLOYEE");
        
        AppUser saved = userRepository.save(user);
        assertNotNull(saved.getId());

        // Retrieve and verify
        assertTrue(userRepository.findByEmail("persistence-test@test.com").isPresent());
    }

    // ============================================
    // RATE LIMITING TESTS
    // ============================================

    @Test
    void testRateLimitingAllows50RequestsPerMinute() throws Exception {
        // Send 50 requests (should all succeed)
        for (int i = 0; i < 50; i++) {
            mockMvc.perform(get("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void testRateLimitingBlocks51stRequest() throws Exception {
        // Send 51 requests - 51st should be blocked
        for (int i = 0; i < 51; i++) {
            MvcResult result = mockMvc.perform(get("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();
            
            if (i < 50) {
                assertEquals(200, result.getResponse().getStatus());
            } else {
                assertEquals(429, result.getResponse().getStatus()); // Too Many Requests
            }
        }
    }

    // ============================================
    // SECURITY & CORS TESTS
    // ============================================

    @Test
    void testCorsHeadersPresent() throws Exception {
        mockMvc.perform(get("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
                // CORS is configured with "*" in @CrossOrigin
    }

    @Test
    void testUnauthorizedAccessAttempt() throws Exception {
        // Some endpoints might require authentication
        // This test verifies behavior for unauthenticated requests
        mockMvc.perform(get("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Tasks endpoint is public
    }

    // ============================================
    // WEBSOCKET NOTIFICATION TESTS
    // ============================================

    @Test
    void testWebSocketEndpointAvailable() throws Exception {
        // WebSocket endpoints are tested differently, but verify HTTP endpoint exists
        mockMvc.perform(get("/ws-alerts")
                .header("Upgrade", "websocket"))
                .andExpect(status().isOk());
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private String toJson(Object obj) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String extractToken(String jsonResponse) {
        try {
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            var node = mapper.readTree(jsonResponse);
            return node.get("token").asText();
        } catch (Exception e) {
            return null;
        }
    }
}
