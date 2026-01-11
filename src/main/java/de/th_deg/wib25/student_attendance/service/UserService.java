package de.th_deg.wib25.student_attendance.service;

import de.th_deg.wib25.student_attendance.entity.User;
import de.th_deg.wib25.student_attendance.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, String password, String firstName, String lastName, String role, Long matriculationNumber) {
        // Check if user already exists
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Validate email domain for professors
        if ("PROFESSOR".equals(role) && !email.endsWith("@th-deg.de")) {
            throw new IllegalArgumentException("Professors must use @th-deg.de email address");
        }

        // Create new user
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        user.setMatriculationNumber(matriculationNumber);

        return userRepository.save(user);
    }
}
