package com.dating.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dating.dto.MatchedUsers;
import com.dating.model.User;
import com.dating.repository.UserRepository;
import com.dating.service.DatingService;

@RestController
@RequestMapping("/dating")
public class DatingController {

    @Autowired
    private DatingService datingService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating user: " + e.getMessage());
        }
    }

    @GetMapping("/matches/{userId}")
    public ResponseEntity<List<MatchedUsers>> getMatches(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "2") int limit) {
        return ResponseEntity.ok(datingService.findTopMatches(userId, limit));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.notFound().build();
            }
            userRepository.deleteById(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting user: " + e.getMessage());
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll()
    {
    	List<User> allUsers=userRepository.findAll();
    	return ResponseEntity.ok(allUsers);
    }
} 