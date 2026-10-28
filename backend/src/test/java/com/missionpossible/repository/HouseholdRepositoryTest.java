package com.missionpossible.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.missionpossible.entity.Household;
import com.missionpossible.entity.User;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
public class HouseholdRepositoryTest {

    @Autowired
    private HouseholdRepository householdRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void shouldSaveAndFindHousehold() {
        User testUser = new User();
        testUser.setEmail("testEmail@test.com");
        testUser.setPasswordHash("test");
        testUser.setFirstName("Esai");
        testUser.setLastName("Hernandez");

        User savedUser = userRepository.saveAndFlush(testUser);

        entityManager.clear();

        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getCreatedAt());
        assertNotNull(savedUser.getUpdatedAt());

        Household household = new Household("Hernandez Family", savedUser);
        Household savedHousehold = householdRepository.saveAndFlush(household);

        UUID householdId = savedHousehold.getId();

        entityManager.clear();

        Household foundHousehold = householdRepository
                .findById(householdId)
                .orElseThrow();

        assertNotNull(foundHousehold.getId());
        assertEquals("Hernandez Family", foundHousehold.getName());
        assertEquals(
                savedUser.getId(),
                foundHousehold.getCreatedByUser().getId());
    }

}
