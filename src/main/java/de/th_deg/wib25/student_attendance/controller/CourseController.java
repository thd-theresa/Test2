
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

/**
 * Controller für Kurslisten, Kursdetails und Teilnehmerverwaltung.
 *
 * Endpunkte:
 *  - GET  /courses                     -> Kursübersicht (immer hartkodierte Demo-Kurse)
 *  - GET  /courses/{id}                -> Kursdetail mit Teilnehmerliste, Suche, Dropdown
 *  - POST /courses/{id}/students/add   -> Student zum Kurs hinzufügen
 *  - POST /courses/{id}/students/remove-> Student aus Kurs entfernen
 */
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

    /**
     * Kursübersicht
     * zeigt echte Kurse aus der Datenbank an
     */
    @GetMapping({"", "/"})
    public String listCourses(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "courses"; // -> templates/courses.html
    }

    /**
     * Formular zum Anlegen eines neuen Kurses
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());

        // Professoren für Dropdown laden
        List<User> professors = userRepository.findByRole("PROFESSOR");
        model.addAttribute("professors", professors);

        return "course-form"; // -> templates/course-form. html
    }

    /**
     * Kurs speichern (POST)
     */
    @PostMapping
    public String createCourse(@ModelAttribute Course course,
                               @RequestParam(required = false) Long professorId) {

        // Professor zuweisen (falls ausgewählt)
        if (professorId != null) {
            User professor = userRepository.findById(professorId)
                    . orElseThrow(() -> new RuntimeException("Professor nicht gefunden"));
            course.setProfessor(professor);
        }

        courseRepository.save(course);

        // Zurück zur Kursliste
        return "redirect:/courses";
    }


    /**
     * Kursdetails + Teilnehmerverwaltung (Anzeige)
     */
    @GetMapping("/{id}")
    public String courseDetail(@PathVariable Long id,
                               @RequestParam(value = "q", required = false) String q,
                               Model model) {
        // Kurs aus DB laden; wenn nicht vorhanden, setze nur Name auf "Test Kurs"
        Course course = courseRepository.findById(id).orElse(null);

        model.addAttribute("courseId", id);
        model.addAttribute("courseName", course != null ? course.getName() : "Test Kurs");
        model.addAttribute("description", course != null ? course.getDescription() : null);

        // Teilnehmerliste
        List<User> students = (course != null) ? course.getStudents() : List.of();
        model.addAttribute("students", students);
        model.addAttribute("studentCount", students.size());

        // Kandidatenbasis: alle STUDENT-User
        List<User> candidates = userRepository.findByRole("STUDENT");

        // Optionale Textsuche (Name oder E-Mail)
        if (q != null && !q.isBlank()) {
            String qq = q.toLowerCase();
            candidates = candidates.stream()
                    .filter(u -> ((u.getFirstName() != null ? u.getFirstName() : "") + " " +
                            (u.getLastName() != null ? u.getLastName() : "")).toLowerCase().contains(qq)
                            || (u.getEmail() != null && u.getEmail().toLowerCase().contains(qq)))
                    .toList();
        }

        // Bereits im Kurs befindliche entfernen
        if (course != null && course.getStudents() != null && !course.getStudents().isEmpty()) {
            candidates = candidates.stream()
                    .filter(u -> !course.getStudents().contains(u))
                    .toList();
        }

        model.addAttribute("candidateStudents", candidates);
        model.addAttribute("query", q);

        return "course-detail"; // -> templates/course-detail.html
    }

    /**
     * Teilnehmer hinzufügen (POST)
     */
    @PostMapping("/{id}/students/add")
    public String addStudent(@PathVariable Long id,
                             @RequestParam("studentId") Long studentId) {
        courseService.addStudent(id, studentId);
        return "redirect:/courses/" + id;
    }

    /**
     * Teilnehmer entfernen (POST)
     */
    @PostMapping("/{id}/students/remove")
    public String removeStudent(@PathVariable Long id,
                                @RequestParam("studentId") Long studentId) {
        courseService.removeStudent(id, studentId);
        return "redirect:/courses/" + id;
    }

    /**
     * Kursliste-DTO (hartkodierte Demoobjekte)
    public record CourseDto(Long id, String name, String term) {}
    */
}
