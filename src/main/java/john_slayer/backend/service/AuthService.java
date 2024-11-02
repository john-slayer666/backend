package john_slayer.backend.service;

import john_slayer.backend.DTO.UserDTO;
import john_slayer.backend.entity.User;
import john_slayer.backend.omponent.JwtTokenProvider;
import john_slayer.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public ResponseEntity<?> register(UserDTO userDTO) {
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be null or empty");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        String hashPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPasswordHash(hashPassword);
        user.setFullName(userDTO.getFullName());
        user.setAvatarUrl(userDTO.getAvatarUrl());

        userRepository.save(user);

        return ResponseEntity.ok("Registration successful");
    }

    public ResponseEntity<?> login(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (passwordEncoder.matches(userDTO.getPassword(), user.getPasswordHash())) {
            //! Генерация токена
            String token = jwtTokenProvider.generateToken(user.getId());

            //! Возвращаем токен в ответе
            Map <String, String> response = new HashMap <>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect login or password");
        }
    }

    public UserDTO getCurrentUser(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

}


