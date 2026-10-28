package com.missionpossible.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.missionpossible.entity.Household;
import com.missionpossible.entity.HouseholdMember;
import com.missionpossible.entity.HouseholdRole;
import com.missionpossible.entity.User;
import com.missionpossible.repository.HouseholdMemberRepository;
import com.missionpossible.repository.HouseholdRepository;
import com.missionpossible.repository.UserRepository;

@SpringBootTest
public class HouseholdServiceTest {

    @Autowired
    private HouseholdService householdService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseholdRepository householdRepository;

    @Autowired
    private HouseholdMemberRepository householdMemberRepository;

    @Test
    @Transactional
    void shouldCreateHouseholdWithAdminMembership() {
        User user = new User();

        user.setEmail("household-service-test@example.com");
        user.setPasswordHash("test-hash");
        user.setFirstName("Test");
        user.setLastName("Admin");

        User savedUser = userRepository.save(user);

        Household savedHousehold = householdService.createHousehold(
                "Test Family",
                savedUser);

        assertNotNull(savedHousehold.getId());
        assertEquals("Test Family", savedHousehold.getName());
        assertEquals(
                savedUser.getId(),
                savedHousehold.getCreatedByUser().getId());

        Optional<HouseholdMember> membership = householdMemberRepository.findByHousehold_IdAndUser_Id(
                savedHousehold.getId(),
                savedUser.getId());

        assertTrue(membership.isPresent());

        HouseholdMember savedMembership = membership.get();
        assertEquals(HouseholdRole.ADMIN, savedMembership.getRole());
        assertEquals("Test Admin", savedMembership.getDisplayName());
        assertTrue(savedMembership.isActive());
    }
}
