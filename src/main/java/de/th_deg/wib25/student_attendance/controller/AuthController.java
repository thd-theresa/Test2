package de.th_deg.wib25.student_attendance.controller;

import de.th_deg.wib25.student_attendance.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        if (error != null) {
            model.addAttribute("error", "Ungültige E-Mail oder Passwort");
        }
        if (logout != null) {
            model.addAttribute("success", "Sie wurden erfolgreich abgemeldet");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(@RequestParam(value = "error", required = false) String error,
                              Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("email") String email,
                          @RequestParam("password") String password,
                          @RequestParam("firstName") String firstName,
                          @RequestParam("lastName") String lastName,
                          @RequestParam("role") String role,
                          @RequestParam(value = "matriculationNumber", required = false) Long matriculationNumber,
                          Model model) {
        try {
            // Validate password length
            if (password.length() < 6) {
                model.addAttribute("error", "Passwort muss mindestens 6 Zeichen lang sein");
                return "register";
            }

            // Validate professor email domain
            if ("PROFESSOR".equals(role) && !email.endsWith("@th-deg.de")) {
                model.addAttribute("error", "Dozenten müssen eine @th-deg.de E-Mail-Adresse verwenden");
                return "register";
            }

            // Validate student has matriculation number
            if ("STUDENT".equals(role) && matriculationNumber == null) {
                model.addAttribute("error", "Studenten müssen eine Matrikelnummer angeben");
                return "register";
            }

            userService.registerUser(email, password, firstName, lastName, role, matriculationNumber);
            return "redirect:/login?success=registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
