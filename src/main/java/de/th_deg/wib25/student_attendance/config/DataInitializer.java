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
    
    // Erster Admin mit zufälligem Passwort
    private static final String ADMIN_EMAIL = "admin@th-deg.de";
    
    // Zweiter Admin mit festem Passwort
    private static final String ADMIN2_EMAIL = "admin2@th-deg.de";
    private static final String ADMIN2_PASSWORD = "Admin123!";
    
    private static final String ADMIN_ROLE = "PROFESSOR";  // Role für Dozenten
    private static final int PASSWORD_LENGTH = 16; // Länge des zufälligen Passworts

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Verschlüsselt Passwörter sicher

    @PostConstruct
    public void init() {
        logger.info("=== DataInitializer wird gestartet ===");
        createAdminAccountIfNotExists(); // Admin 1: Zufälliges Passwort
        createSecondAdminAccountIfNotExists(); // Admin 2: Festes Passwort
        logger.info("=== DataInitializer abgeschlossen ===");
    }

    private void createAdminAccountIfNotExists() {
        // Prüfe ob Admin existiert
        var existingAdmin = userRepository.findByEmail(ADMIN_EMAIL);
        
        if (existingAdmin.isPresent()) {
            User admin = existingAdmin.get();
            
            if (!isBCryptEncoded(admin.getPassword())) {
                logger.warn("Admin-Account existiert, aber Passwort ist nicht BCrypt-kodiert.");
                logger.warn("Aktualisiere Admin-Account mit neuem Passwort...");
                
                // Generiere neues Passwort und aktualisiere bestehenden Account
                String randomPassword = generateSecurePassword();
                String encodedPassword = passwordEncoder.encode(randomPassword);
                admin.setPassword(encodedPassword);
                userRepository.save(admin);
                
                logger.warn("=".repeat(80));
                logger.warn("ADMIN-ACCOUNT AKTUALISIERT");
                logger.warn("=".repeat(80));
                logger.warn("Email: {}", ADMIN_EMAIL);
                logger.warn("Neues Passwort: {}", randomPassword);
                logger.warn("Rolle: {}", ADMIN_ROLE);
                logger.warn("=".repeat(80));
                logger.warn("BITTE NOTIEREN SIE DAS NEUE PASSWORT!");
                logger.warn("=".repeat(80));
                return;
            } else {
                logger.info("Admin-Account existiert bereits mit gültigem BCrypt-Passwort.");
                return;
            }
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

    private void createSecondAdminAccountIfNotExists() {
        // Prüfe ob zweiter Admin existiert
        var existingAdmin2 = userRepository.findByEmail(ADMIN2_EMAIL);
        
        if (existingAdmin2.isPresent()) {
            User admin2 = existingAdmin2.get();
            
            // Prüfe ob Passwort korrekt ist, aktualisiere wenn nötig
            if (!isBCryptEncoded(admin2.getPassword())) {
                logger.warn("Admin2-Account existiert, aber Passwort ist nicht BCrypt-kodiert.");
                logger.warn("Aktualisiere Admin2-Account mit festem Passwort...");
                
                String encodedPassword = passwordEncoder.encode(ADMIN2_PASSWORD);
                admin2.setPassword(encodedPassword);
                userRepository.save(admin2);
                
                logger.warn("=".repeat(80));
                logger.warn("ADMIN2-ACCOUNT AKTUALISIERT");
                logger.warn("=".repeat(80));
                logger.warn("Email: {}", ADMIN2_EMAIL);
                logger.warn("Passwort: {} (FESTES PASSWORT)", ADMIN2_PASSWORD);
                logger.warn("Rolle: {}", ADMIN_ROLE);
                logger.warn("=".repeat(80));
            } else {
                logger.info("Admin2-Account existiert bereits mit gültigem BCrypt-Passwort.");
            }
            return;
        }

        // Erstelle zweiten Admin mit festem Passwort
        String encodedPassword = passwordEncoder.encode(ADMIN2_PASSWORD);

        User admin2User = new User();
        admin2User.setEmail(ADMIN2_EMAIL);
        admin2User.setPassword(encodedPassword);
        admin2User.setFirstName("Admin");
        admin2User.setLastName("Zwei");
        admin2User.setRole(ADMIN_ROLE);
        admin2User.setMatriculationNumber(1L); // Unterschiedliche Matrikelnummer

        userRepository.save(admin2User);

        logger.warn("=".repeat(80));
        logger.warn("ZWEITER ADMIN-ACCOUNT ERSTELLT");
        logger.warn("=".repeat(80));
        logger.warn("Email: {}", ADMIN2_EMAIL);
        logger.warn("Passwort: {} (FESTES PASSWORT)", ADMIN2_PASSWORD);
        logger.warn("Rolle: {}", ADMIN_ROLE);
        logger.warn("=".repeat(80));
        logger.warn("DIESER ACCOUNT HAT EIN FESTES PASSWORT FÜR EINFACHE ANMELDUNG!");
        logger.warn("=".repeat(80));
    }

    /**
     * Prüft ob ein Passwort BCrypt-kodiert ist.
     * BCrypt-Hashes beginnen mit $2a$, $2b$, $2x$ oder $2y$
     */
    private boolean isBCryptEncoded(String password) {
        if (password == null) {
            return false;
        }
        return password.matches("^\\$2[abxy]\\$\\d+\\$.+");
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
