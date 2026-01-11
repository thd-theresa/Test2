package de.th_deg.wib25.student_attendance.config;

import de.th_deg.wib25.student_attendance.entity.User;
import de.th_deg.wib25.student_attendance.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if any users exist in the database
        if (userRepository.count() == 0) {
            // Generate a secure random password
            String randomPassword = generateSecurePassword(16);
            
            // Create the admin/lecturer account
            User admin = new User();
            admin.setEmail("admin@th-deg.de");
            admin.setPassword(passwordEncoder.encode(randomPassword));
            admin.setFirstName("Admin");
            admin.setLastName("Dozent");
            admin.setRole("ADMIN");
            admin.setMatriculationNumber(0L);
            
            userRepository.save(admin);
            
            // Log the password to console (only on first startup)
            System.out.println("=".repeat(80));
            System.out.println("INITIAL ADMIN ACCOUNT CREATED");
            System.out.println("=".repeat(80));
            System.out.println("Email: admin@th-deg.de");
            System.out.println("Password: " + randomPassword);
            System.out.println("=".repeat(80));
            System.out.println("IMPORTANT: Please save this password securely. It will not be shown again!");
            System.out.println("=".repeat(80));
        }
    }

    private String generateSecurePassword(int length) {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-_=+[]{}|;:,.<>?";
        String allChars = upperCase + lowerCase + digits + specialChars;
        
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        
        // Ensure at least one character from each category
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));
        
        // Fill the rest randomly
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        // Shuffle the password characters
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }
}
