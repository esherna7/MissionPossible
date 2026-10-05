package com.missionpossible.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.missionpossible.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void shouldSaveAndRetrieveUser() {
        User testUser = new User();
        testUser.setEmail("testEmail@test.com");
        testUser.setPasswordHash("test");
        testUser.setFirstName("Esai");
        testUser.setLastName("Hernandez");

        User savedUser = userRepository.saveAndFlush(testUser);

        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getCreatedAt());
        assertNotNull(savedUser.getUpdatedAt());

        entityManager.clear();

        User retrievedUser = userRepository.findById(savedUser.getId()).orElseThrow();

        assertEquals(savedUser.getEmail(), retrievedUser.getEmail());
        assertEquals(savedUser.getFirstName(), retrievedUser.getFirstName());
        assertEquals(savedUser.getLastName(), retrievedUser.getLastName());
    }
}
