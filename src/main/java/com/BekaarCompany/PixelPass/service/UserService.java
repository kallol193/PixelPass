package com.BekaarCompany.PixelPass.service;

import com.BekaarCompany.PixelPass.entity.User;
import com.BekaarCompany.PixelPass.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        String role = user.getRole();
        if (!role.startsWith("ROLE_")){
            role = "ROLE_"+ role;
        }user.setRole(role);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Find all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Find user by ID
    public Optional<User> getUserById(ObjectId id) {
        return userRepository.findById(id);
    }

    // Find user by username
    @Cacheable(value = "user",key = "#username")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));
    }

    // Update user details
    @Transactional
    @Cacheable(value = "existingUser", key ="#id")
    public User updateUser(ObjectId id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        existingUser.setUsername(updatedUser.getUsername());
//        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
//        // Add more fields to update as necessary

        return userRepository.save(existingUser);
    }

    // Delete user by ID
    @Transactional
    @Cacheable(value = "user",key = "#id")
    public void deleteUser(ObjectId id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        userRepository.delete(user);

    }


    @Scheduled(fixedRate = 3600000)//eleminates every cache stored after 1 hour or 36 lac miliseconds
    @CacheEvict(value = {"user","existingUser"},allEntries = true)
    public void clearAllCaches(){
        throw new RuntimeException("cache all cleared");
    }
}

