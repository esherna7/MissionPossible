package com.missionpossible.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createUser() throws Exception {
        mockMvc.perform(post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email": "testEmail@test.com",
                            "password": "password",
                            "firstName": "test",
                            "lastName": "user"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("testEmail@test.com"))
                .andExpect(jsonPath("$.firstName").value("test"))
                .andExpect(jsonPath("$.lastName").value("user"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }

    @Test
    void createUser_duplicateEmail_returnsConflict() throws Exception {

        String requestBody = """
                {
                    "email": "duplicate@test.com",
                    "password": "password",
                    "firstName": "test",
                    "lastName": "user"
                }
                """;

        // First request creates the user
        mockMvc.perform(post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());

        // Second request uses the same email
        mockMvc.perform(post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    public void createUser_emptyEmail_returnsBadRequest() throws Exception {
        String requestBody = """
                {
                    "email": "",
                    "password": "password",
                    "firstName": "test",
                    "lastName": "user"
                }
                """;

        mockMvc.perform(post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUser_shortPassword_returnsBadRequest() throws Exception {
        String requestBody = """
                {
                "email": "testEmail@test2.com"
                "password": "123",
                "firstName": "test",
                "lastName": "user"
                }
                """;

        mockMvc.perform(post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

}
