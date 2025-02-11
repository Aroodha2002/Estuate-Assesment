package com.dating.service;

import com.dating.dto.MatchedUsers;
import com.dating.model.User;
import com.dating.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DatingService {

    @Autowired
    private UserRepository userRepository;

    public List<MatchedUsers> findTopMatches(Long userId, int limit) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<User> oppositeGenders = userRepository.findByGenderNot(user.getGender());
        List<MatchedUsers> matches = new ArrayList<>();

        for (User oppositeGender : oppositeGenders) {
            if (!oppositeGender.getId().equals(userId)) {
                int matchScore = calculateMatchScore(user, oppositeGender);
                int commonInterests = calculateCommonInterests(user.getInterests(), oppositeGender.getInterests());
                
                matches.add(new MatchedUsers(
                    oppositeGender.getId(),
                    oppositeGender.getName(),
                    oppositeGender.getAge(),
                    matchScore,
                    commonInterests
                ));
            }
        }

        matches.sort(Comparator.comparingInt(MatchedUsers::getMatchScore).reversed());
        return matches.subList(0, Math.min(limit, matches.size()));
    }

    private int calculateMatchScore(User user1, User user2) {
        int score = 0;
        
       
        if (!user1.getGender().equals(user2.getGender())) {
            score += 1000;
        }

       
        int ageDiff = Math.abs(user1.getAge() - user2.getAge());
        score += (100 - ageDiff) * 10;

       
        int commonInterests = calculateCommonInterests(user1.getInterests(), user2.getInterests());
        score += commonInterests;

        return score;
    }

    private int calculateCommonInterests(String interests1, String interests2) {
        if (interests1 == null || interests2 == null) {
            return 0;
        }
        
        Set<String> set1 = new HashSet<>(Arrays.asList(interests1.split(",")));
        Set<String> set2 = new HashSet<>(Arrays.asList(interests2.split(",")));
        
        set1.retainAll(set2);
        return set1.size();
    }
} 