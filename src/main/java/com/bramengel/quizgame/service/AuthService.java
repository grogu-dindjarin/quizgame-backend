package com.bramengel.quizgame.service;

import com.bramengel.quizgame.dto.AuthResponse;
import com.bramengel.quizgame.dto.LoginRequest;
import com.bramengel.quizgame.dto.RegisterRequest;
import com.bramengel.quizgame.exception.BadRequestException;
import com.bramengel.quizgame.exception.RecordNotFoundException;
import com.bramengel.quizgame.model.Role;
import com.bramengel.quizgame.model.User;
import com.bramengel.quizgame.model.UserProfile;
import com.bramengel.quizgame.repository.UserProfileRepository;
import com.bramengel.quizgame.repository.UserRepository;
import com.bramengel.quizgame.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            UserProfileRepository userProfileRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("E-mailadres is al in gebruik");
        }

        User user = new User(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            Role.USER
        );
        userRepository.save(user);

        UserProfile profile = new UserProfile(user, request.getDisplayName());
        userProfileRepository.save(profile);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getEmail(), user.getRole().name(), profile.getDisplayName(), user.getId());
    }

    public AuthResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BadRequestException("Ongeldig e-mailadres of wachtwoord"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Ongeldig e-mailadres of wachtwoord");
        }

        UserProfile profile = userProfileRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RecordNotFoundException("Gebruikersprofiel niet gevonden voor gebruiker: " + user.getId()));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getEmail(), user.getRole().name(), profile.getDisplayName(), user.getId());
    }
}
