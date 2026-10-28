package com.missionpossible.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import com.missionpossible.entity.HouseholdMember;

public interface HouseholdMemberRepository extends JpaRepository<HouseholdMember, UUID> {

    Optional<HouseholdMember> findByHousehold_IdAndUser_Id(
            UUID householdId,
            UUID userId);

}
