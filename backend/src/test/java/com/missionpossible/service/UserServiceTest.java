package com.missionpossible.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.missionpossible.entity.User;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void shouldCreateUserWithHashedPassword() {
        String rawPassword = "testPassword123";

        User savedUser = userService.createUser(
                "testEmail@test.com",
                rawPassword,
                "Esai",
                "Hernandez");

        entityManager.flush();
        entityManager.clear();

        User retrievedUser = userService.findById(savedUser.getId()).orElseThrow();

        assertNotNull(retrievedUser.getId());
        assertNotNull(retrievedUser.getCreatedAt());
        assertNotNull(retrievedUser.getUpdatedAt());
        assertEquals("testEmail@test.com", retrievedUser.getEmail());
        assertEquals("Esai", retrievedUser.getFirstName());
        assertEquals("Hernandez", retrievedUser.getLastName());

        assertNotEquals(rawPassword, retrievedUser.getPasswordHash());
        assertTrue(passwordEncoder.matches(rawPassword, retrievedUser.getPasswordHash()));
    }

    @Test 
    public void shouldRejectDuplicateEmail() {
        userService.createUser(
                "testEmail@test.com",
                "password123",
                "Esai",
                "Hernandez");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(
                        "testEmail@test.com",
                        "password123",
                        "Another",
                        "User"));

        assertEquals("Email is already registered", exception.getMessage());
    }

    @Test 
    public void shouldFindUserByEmail() {
        User savedUser = userService.createUser(
                "testEmail@test.com",
                "password123",
                "Esai",
                "Hernandez");

        entityManager.flush();
        entityManager.clear();

        User retrievedUser = userService.findByEmail("testEmail@test.com").orElseThrow();

        assertEquals(savedUser.getId(), retrievedUser.getId());
    }

}
