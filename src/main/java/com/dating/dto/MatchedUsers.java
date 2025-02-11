package com.dating.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class MatchedUsers {
    private Long userId;
    private String name;
    private int age;
    private int matchScore;
    private int commonInterests;
} 