package com.missionpossible.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.missionpossible.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}
