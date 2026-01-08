
package de.th_deg.wib25.student_attendance.controller;

import de.th_deg.wib25.student_attendance.entity.Course;
import de.th_deg.wib25.student_attendance.entity.User;
import de.th_deg.wib25.student_attendance.repository.CourseRepository;
import de.th_deg.wib25.student_attendance.repository.UserRepository;
import de.th_deg.wib25.student_attendance.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * Controller für Kurslisten, Kursdetails und einfache Teilnehmerverwaltung.
 *
 * Endpunkte:
 *  - GET  /courses                -> Kursübersicht (Liste)
 *  - GET  /courses/{id}           -> Kursdetail mit Teilnehmerliste, Suche, Dropdown
 *  - POST /courses/{id}/students/add    -> Student zum Kurs hinzufügen
 *  - POST /courses/{id}/students/remove -> Student aus Kurs entfernen
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
     *
     * Unterstützt sowohl /courses als auch /courses/
     * Lädt Kurse aus der DB. Falls du noch keine Kursdaten hast, kannst du
     * temporär Dummy-Daten verwenden (siehe auskommentierten Block).
     */
    @GetMapping({"", "/"})
    public String listCourses(Model model) {
        // Variante: echte Kurse aus DB
        List<Course> courses = courseRepository.findAll().stream()
                .sorted(Comparator.comparing(Course::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
        model.addAttribute("courses", courses);
        return "courses"; // -> templates/courses.html

        /*
        // TEMP-Variante mit Dummy-Daten (falls DB noch leer ist):
        List<CourseDto> courses = List.of(
            new CourseDto(1L, "Software Engineering", "WS 25/26"),
            new CourseDto(2L, "Datenbanken", "WS 25/26")
        );
        model.addAttribute("courses", courses);
        return "courses";
        */
    }

    /**
     * Kursdetails + Teilnehmerverwaltung (Anzeige)
     *
     * Zeigt:
     *  - Kursname
     *  - Teilnehmerliste
     *  - Teilnehmerzahl
     *  - Suche nach Student-Usern (q)
     *  - Dropdown mit Kandidaten (alle STUDENT-User abzüglich bereits zugeordneter)
     */
    @GetMapping("/{id}")
    public String courseDetail(@PathVariable Long id,
                               @RequestParam(value = "q", required = false) String q,
                               Model model) {
        // Kurs laden (optional: Fehlerseite, hier vereinfachend "Test Kurs" wenn nicht gefunden)
        Course course = courseRepository.findById(id).orElse(null);

        model.addAttribute("courseId", id);
        model.addAttribute("courseName", course != null ? course.getName() : "Test Kurs");

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
     * Nur für die (auskommentierte) Dummy-Listen-Variante.
     */
    public record CourseDto(Long id, String name, String term) {}
}
