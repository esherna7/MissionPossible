package com.missionpossible.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.missionpossible.entity.User;
import com.missionpossible.entity.Household;
import com.missionpossible.entity.HouseholdMember;
import com.missionpossible.entity.HouseholdRole;
import com.missionpossible.repository.HouseholdMemberRepository;
import com.missionpossible.repository.HouseholdRepository;

@Service
@Transactional
public class HouseholdService {

    private final HouseholdRepository householdRepository;

    private final HouseholdMemberRepository householdMemberRepository;

    public HouseholdService(HouseholdRepository householdRepository,
            HouseholdMemberRepository householdMemberRepository) {
        this.householdRepository = householdRepository;
        this.householdMemberRepository = householdMemberRepository;
    }

    public Household createHousehold(String name, User creator) {
        Household household = new Household(name, creator);
        Household savedHousehold = householdRepository.save(household);
        String displayName = creator.getFirstName() + " " + creator.getLastName();
        HouseholdMember membership = new HouseholdMember(savedHousehold, creator, displayName, HouseholdRole.ADMIN);
        householdMemberRepository.save(membership);
        return savedHousehold;
    }

}
