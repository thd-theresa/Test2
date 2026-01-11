package de.th_deg.wib25.student_attendance.config;

import de.th_deg.wib25.student_attendance.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private static final String ADMIN_EMAIL = "admin@student-attendance.de";
    private static final String ROLE_DOZENT = "DOZENT";
    private static final int PASSWORD_LENGTH = 16;

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        if (!userService.adminExists()) {
            createDefaultAdmin();
        }
    }

    private void createDefaultAdmin() {
        String randomPassword = generateSecurePassword();
        
        try {
            userService.createUser(
                    ADMIN_EMAIL,
                    randomPassword,
                    "System",
                    "Administrator",
                    ROLE_DOZENT
            );

            logger.info("===============================================");
            logger.info("DEFAULT ADMIN ACCOUNT CREATED");
            logger.info("===============================================");
            logger.info("Email: {}", ADMIN_EMAIL);
            logger.info("Password: {}", randomPassword);
            logger.info("===============================================");
            logger.info("BITTE NOTIEREN SIE DIESES PASSWORT!");
            logger.info("Es wird nur beim ersten Start angezeigt.");
            logger.info("===============================================");
            
        } catch (Exception e) {
            logger.error("Failed to create default admin account", e);
        }
    }

    private String generateSecurePassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[PASSWORD_LENGTH];
        random.nextBytes(bytes);
        
        // Base64-Kodierung für ein lesbares und sicheres Passwort
        String password = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        
        // Auf gewünschte Länge kürzen
        return password.substring(0, Math.min(password.length(), PASSWORD_LENGTH));
    }
}
