
package de.th_deg.wib25.student_attendance.service;

import de.th_deg.wib25.student_attendance.entity.Course;
import de.th_deg.wib25.student_attendance.entity.User;
import de.th_deg.wib25.student_attendance.repository.CourseRepository;
import de.th_deg.wib25.student_attendance.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + studentId));

        if (!student.isStudent()) {
            throw new IllegalStateException("User is not a STUDENT");
        }

        course.addStudent(student);
        // Bei ManyToMany reicht Persist des Owning-Seite; hier Kurs ist owning Seite via @JoinTable
        courseRepository.save(course);
    }

    @Transactional
    public void removeStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + studentId));

        course.removeStudent(student);
        courseRepository.save(course);
    }
}
