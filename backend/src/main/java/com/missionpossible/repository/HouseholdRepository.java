package com.missionpossible.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import com.missionpossible.entity.Household;

public interface HouseholdRepository extends JpaRepository<Household, UUID> {

}
