
package de.th_deg.wib25.student_attendance.controller;

import de.th_deg.wib25.student_attendance.entity.User;
import de.th_deg.wib25.student_attendance.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserRepository userRepository;

    public UserAdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", List.of("STUDENT", "PROFESSOR", "ADMIN"));
        return "admin/users"; // -> templates/admin/users.html
    }

    @PostMapping
    public String createUser(@ModelAttribute("newUser") User newUser, Model model) {
        if (userRepository.existsByEmail(newUser.getEmail())) {
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("roles", List.of("STUDENT", "PROFESSOR", "ADMIN"));
            model.addAttribute("error", "E-Mail existiert bereits.");
            return "admin/users";
        }
        // Hinweis: Du hast Security deaktiviert; falls du Passwörter hashen willst,
        // kannst du hier später BCrypt verwenden.
        if (!"STUDENT".equalsIgnoreCase(newUser.getRole()) && newUser.getMatriculationNumber() == null) {
            newUser.setMatriculationNumber(0L);
        }
        userRepository.save(newUser);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}
