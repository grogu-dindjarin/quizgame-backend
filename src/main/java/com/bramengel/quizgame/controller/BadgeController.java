package com.bramengel.quizgame.controller;

import com.bramengel.quizgame.dto.BadgeResponse;
import com.bramengel.quizgame.service.BadgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    private final BadgeService badgeService;

    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @GetMapping
    public ResponseEntity<List<BadgeResponse>> findAll() {
        return ResponseEntity.ok(badgeService.findAll());
    }

    @GetMapping("/me")
    public ResponseEntity<List<BadgeResponse>> getMyBadges(Principal principal) {
        return ResponseEntity.ok(badgeService.findEarnedByUserEmail(principal.getName()));
    }
}
