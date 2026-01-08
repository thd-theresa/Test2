
package de.th_deg.wib25.student_attendance.controller;

import de.th_deg.wib25.student_attendance.entity.Course;
import de.th_deg.wib25.student_attendance.entity.User;
import de.th_deg.wib25.student_attendance.repository.CourseRepository;
import de.th_deg.wib25.student_attendance.repository.UserRepository;
import de.th_deg.wib25.student_attendance.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseService courseService;

    public CourseController(CourseRepository courseRepository,
                            UserRepository userRepository,
                            CourseService courseService) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.courseService = courseService;
    }

    // ... dein bestehendes listCourses() ...

    @GetMapping("/{id}")
    public String courseDetail(@PathVariable Long id,
                               @RequestParam(value = "q", required = false) String q,
                               Model model) {
        Course course = courseRepository.findById(id).orElse(null);
        model.addAttribute("courseId", id);
        model.addAttribute("courseName", course != null ? course.getName() : "Test Kurs");

        List<User> students = (course != null) ? course.getStudents() : List.of();
        model.addAttribute("students", students);
        model.addAttribute("studentCount", students.size());

        // Kandidaten: alle STUDENT-User
        List<User> candidates = userRepository.findByRole("STUDENT");
        // Optionale Textsuche
        if (q != null && !q.isBlank()) {
            String qq = q.toLowerCase();
            candidates = candidates.stream()
                    .filter(u -> (u.getFirstName() + " " + u.getLastName()).toLowerCase().contains(qq)
                            || u.getEmail().toLowerCase().contains(qq))
                    .toList();
        }
        // Bereits zugeordnete entfernen
        if (course != null) {
            candidates = candidates.stream()
                    .filter(u -> !course.getStudents().contains(u))
                    .toList();
        }
        model.addAttribute("candidateStudents", candidates);
        model.addAttribute("query", q);

        return "course-detail";
    }

    @PostMapping("/{id}/students/add")
    public String addStudent(@PathVariable Long id, @RequestParam Long studentId) {
        courseService.addStudent(id, studentId);
        return "redirect:/courses/" + id;
    }

    @PostMapping("/{id}/students/remove")
    public String removeStudent(@PathVariable Long id, @RequestParam Long studentId) {
        courseService.removeStudent(id, studentId);
        return "redirect:/courses/" + id;
    }

    public record CourseDto(Long id, String name, String term) {}
}
