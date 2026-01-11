package de.th_deg.wib25.student_attendance.config;

import de.th_deg.wib25.student_attendance.entity.User;
import de.th_deg.wib25.student_attendance.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private static final String ADMIN_EMAIL = "admin@th-deg.de"; //Feste Admin-Email
    private static final String ADMIN_ROLE = "PROFESSOR";  // Role für Dozenten
    private static final int PASSWORD_LENGTH = 16; // Länge des zufälligen Passworts

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    } // Verschlüsselt Passwörter sicher

    @PostConstruct
    public void init() {
        createAdminAccountIfNotExists(); // erstellt einen Admin falls noch keiner existiert wird automatisch ausgeführt
    }

    private void createAdminAccountIfNotExists() {
        if (userRepository.existsByEmail(ADMIN_EMAIL)) {
            logger.info("Admin-Account existiert bereits. Keine Initialisierung erforderlich.");
            return; // Prüft ob Admin schon existiert und beendet die Methode, falls ein Admin schon existiert
        }

        String randomPassword = generateSecurePassword(); // erzeugt zufälliges Passwort
        String encodedPassword = passwordEncoder.encode(randomPassword); //Verschlüsselt Passwort

        User adminUser = new User(); // Erstellt neues User Objekt
        adminUser.setEmail(ADMIN_EMAIL);
        adminUser.setPassword(encodedPassword);
        adminUser.setFirstName("System");
        adminUser.setLastName("Administrator");
        adminUser.setRole(ADMIN_ROLE);
        adminUser.setMatriculationNumber(0L); // Admin hat keine Matrikelnummer

        userRepository.save(adminUser); // Speichere in Datenbank

        logger.warn("=".repeat(80));
        logger.warn("ADMIN-ACCOUNT ERSTELLT - ERSTSTART");
        logger.warn("=".repeat(80));
        logger.warn("Email: {}", ADMIN_EMAIL);
        logger.warn("Passwort: {}", randomPassword); // gibt generiertes Passwort aus
        logger.warn("Rolle: {}", ADMIN_ROLE);
        logger.warn("=".repeat(80));
        logger.warn("BITTE NOTIEREN SIE DAS PASSWORT - ES WIRD NICHT ERNEUT ANGEZEIGT!");
        logger.warn("=".repeat(80));
    }

    private String generateSecurePassword() {
        SecureRandom random = new SecureRandom(); // Zufallsgenerator
        byte[] passwordBytes = new byte[PASSWORD_LENGTH];
        random.nextBytes(passwordBytes);

        String base64Password = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(passwordBytes);

        if (base64Password.length() >= PASSWORD_LENGTH) {
            return base64Password.substring(0, PASSWORD_LENGTH); //Kürzt Passwort auf gewünschte Länge
        } else {
            return base64Password + Base64.getUrlEncoder() // Falls string zu kurz ist
                    .withoutPadding()
                    .encodeToString(random.generateSeed(PASSWORD_LENGTH))
                    .substring(0, PASSWORD_LENGTH - base64Password.length());
        }
    }
}
