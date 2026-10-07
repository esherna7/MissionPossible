package com.missionpossible.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.missionpossible.service.UserService;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    void login_withoutCsrf_returnsForbidden() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email": "test@test.com",
                            "password": "password123"
                        }
                        """))
                .andExpect(status().isForbidden());
    }

    @Test
    void login_withCsrf_invalidCredentials_returnsUnauthorized() throws Exception {
        mockMvc.perform(post("/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email": "nonexistent@test.com",
                            "password": "wrongPassword123"
                        }
                        """))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_validCredentials_returnsOk() throws Exception {

        userService.createUser(
                "login@test.com",
                "password123",
                "Test",
                "User");

        mockMvc.perform(post("/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email": "login@test.com",
                            "password": "password123"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void login_validCredentials_createsSession() throws Exception {

        userService.createUser(
                "session@test.com",
                "password123",
                "Test",
                "User");

        MvcResult result = mockMvc.perform(post("/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email": "session@test.com",
                            "password": "password123"
                        }
                        """))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        assertNotNull(session);

        SecurityContext context = (SecurityContext) session.getAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

        assertNotNull(context);
        assertTrue(context.getAuthentication().isAuthenticated());
        assertEquals(
                "session@test.com",
                context.getAuthentication().getName());

        mockMvc.perform(get("/users/session-check")
                .session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    void logout_validSession_invalidatesSession() throws Exception {

        // Create a test user
        userService.createUser(
                "logout@test.com",
                "password123",
                "Test",
                "User");

        // Log in
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email": "logout@test.com",
                            "password": "password123"
                        }
                        """))
                .andExpect(status().isOk())
                .andReturn();

        // Get the authenticated session
        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);

        assertNotNull(session);

        // Log out using the same session
        mockMvc.perform(post("/auth/logout")
                .session(session)
                .with(csrf()))
                .andExpect(status().isNoContent());

        // Verify the session was invalidated
        assertTrue(session.isInvalid());
    }
}