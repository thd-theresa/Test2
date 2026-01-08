
package de.th_deg.wib25.student_attendance.controller;

import org.springframework.stereotype. Controller;
import org.springframework. ui.Model;
import org. springframework.web.bind.annotation. GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @GetMapping
    public String listCourses(Model model) {
        List<CourseDto> courses = List.of(
                new CourseDto(1L, "Software Engineering", "WS 25/26"),
                new CourseDto(2L, "Datenbanken", "WS 25/26")
        );
        model.addAttribute("courses", courses);
        return "courses"; // -> templates/courses. html
    }

    @GetMapping("/detail")
    public String courseDetail(@RequestParam Long id, @RequestParam String name, Model model) {
        model.addAttribute("courseId", id);
        model.addAttribute("courseName", name);
        return "course-detail"; // -> templates/course-detail.html
    }

    public record CourseDto(Long id, String name, String term) {}
}