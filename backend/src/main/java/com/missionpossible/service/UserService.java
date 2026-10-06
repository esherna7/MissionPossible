package com.missionpossible.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.missionpossible.entity.User;
import com.missionpossible.exception.DuplicateEmailException;
import com.missionpossible.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(String email, String rawPassword, String firstName, String lastName) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException();
        }

        User userToSave = new User();
        userToSave.setEmail(email);
        userToSave.setPasswordHash(passwordEncoder.encode(rawPassword));
        userToSave.setFirstName(firstName);
        userToSave.setLastName(lastName);

        return userRepository.save(userToSave);
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
