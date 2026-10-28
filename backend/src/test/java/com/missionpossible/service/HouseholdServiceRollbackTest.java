package com.missionpossible.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.missionpossible.entity.HouseholdMember;
import com.missionpossible.entity.User;
import com.missionpossible.repository.HouseholdMemberRepository;
import com.missionpossible.repository.HouseholdRepository;
import com.missionpossible.repository.UserRepository;

@SpringBootTest
public class HouseholdServiceRollbackTest {

    @Autowired
    private HouseholdService householdService;

    @MockitoBean
    private HouseholdMemberRepository householdMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseholdRepository householdRepository;

    @Test
    void shouldRollbackHouseholdWhenMembershipCreationFails() {

        when(householdMemberRepository.save(any(HouseholdMember.class)))
                .thenThrow(new RuntimeException("Membership save failed"));

        User user = new User();
        user.setEmail("household-rollback-test@example.com");
        user.setPasswordHash("test-hash");
        user.setFirstName("Rollback");
        user.setLastName("Tester");

        User savedUser = userRepository.save(user);

        try {
            long householdCountBefore = householdRepository.count();

            assertThrows(RuntimeException.class, () -> {
                householdService.createHousehold("Rollback Family", savedUser);
            });

            long householdCountAfter = householdRepository.count();

            assertEquals(householdCountBefore, householdCountAfter);

        } finally {
            userRepository.deleteById(savedUser.getId());
        }
    }
}
