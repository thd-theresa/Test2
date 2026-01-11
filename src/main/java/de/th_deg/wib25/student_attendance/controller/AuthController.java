package de.th_deg.wib25.student_attendance.controller;

import de.th_deg.wib25.student_attendance.entity.User;
import de.th_deg.wib25.student_attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Ungültige E-Mail oder Passwort!");
        }
        if (logout != null) {
            model.addAttribute("success", "Sie wurden erfolgreich abgemeldet.");
        }
        
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String role,
            @RequestParam(required = false) Long matriculationNumber,
            Model model) {

        // Prüfe ob Email bereits existiert
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Diese E-Mail-Adresse ist bereits registriert!");
            return "register";
        }

        // Erstelle neuen User
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        
        // Matrikelnummer nur für Studenten
        if ("STUDENT".equals(role) && matriculationNumber != null) {
            user.setMatriculationNumber(matriculationNumber);
        } else {
            user.setMatriculationNumber(0L);
        }

        userRepository.save(user);

        model.addAttribute("success", "Registrierung erfolgreich! Sie können sich jetzt anmelden.");
        return "login";
    }
}
