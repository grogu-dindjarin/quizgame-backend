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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    // --- register ---

    @Test
    void register_withValidRequest_returnsAuthResponse() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("bram@example.com");
        request.setPassword("secret123");
        request.setDisplayName("Bram");

        when(userRepository.existsByEmail("bram@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenAnswer(inv -> {
            User saved = inv.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        ArgumentCaptor<UserProfile> profileCaptor = ArgumentCaptor.forClass(UserProfile.class);
        when(userProfileRepository.save(profileCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

        when(jwtUtil.generateToken("bram@example.com", "USER")).thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("bram@example.com", response.getEmail());
        assertEquals("USER", response.getRole());
        assertEquals("Bram", response.getDisplayName());
        assertEquals(1L, response.getUserId());

        User savedUser = userCaptor.getValue();
        assertEquals("bram@example.com", savedUser.getEmail());
        assertEquals("hashed", savedUser.getPassword());
        assertEquals(Role.USER, savedUser.getRole());

        UserProfile savedProfile = profileCaptor.getValue();
        assertEquals("Bram", savedProfile.getDisplayName());
    }

    @Test
    void register_withExistingEmail_throwsBadRequestException() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");
        request.setPassword("secret123");
        request.setDisplayName("Existing");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> authService.register(request)
        );

        assertEquals("E-mailadres is al in gebruik", exception.getMessage());
        verify(userRepository, never()).save(any());
        verify(userProfileRepository, never()).save(any());
    }

    // --- authenticate ---

    @Test
    void authenticate_withValidCredentials_returnsAuthResponse() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("bram@example.com");
        request.setPassword("secret123");

        User user = new User("bram@example.com", "hashed", Role.USER);
        user.setId(1L);

        UserProfile profile = new UserProfile(user, "Bram");

        when(userRepository.findByEmail("bram@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret123", "hashed")).thenReturn(true);
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(jwtUtil.generateToken("bram@example.com", "USER")).thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("bram@example.com", response.getEmail());
        assertEquals("USER", response.getRole());
        assertEquals("Bram", response.getDisplayName());
        assertEquals(1L, response.getUserId());
    }

    @Test
    void authenticate_withUnknownEmail_throwsBadRequestException() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@example.com");
        request.setPassword("secret123");

        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> authService.authenticate(request)
        );

        assertEquals("Ongeldig e-mailadres of wachtwoord", exception.getMessage());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void authenticate_withWrongPassword_throwsBadRequestException() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("bram@example.com");
        request.setPassword("wrongpassword");

        User user = new User("bram@example.com", "hashed", Role.USER);
        user.setId(1L);

        when(userRepository.findByEmail("bram@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "hashed")).thenReturn(false);

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> authService.authenticate(request)
        );

        assertEquals("Ongeldig e-mailadres of wachtwoord", exception.getMessage());
        verify(userProfileRepository, never()).findByUserId(anyLong());
    }

    @Test
    void authenticate_withMissingProfile_throwsRecordNotFoundException() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("bram@example.com");
        request.setPassword("secret123");

        User user = new User("bram@example.com", "hashed", Role.USER);
        user.setId(1L);

        when(userRepository.findByEmail("bram@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret123", "hashed")).thenReturn(true);
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(
                RecordNotFoundException.class,
                () -> authService.authenticate(request)
        );

        assertEquals("Gebruikersprofiel niet gevonden voor gebruiker: 1", exception.getMessage());
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }
}
