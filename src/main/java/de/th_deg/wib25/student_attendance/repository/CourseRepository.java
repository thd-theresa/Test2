package de.th_deg.wib25.student_attendance.repository;

import de. th_deg.wib25.student_attendance.entity.Course;
import org.springframework.data. jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Finde alle Kurse eines Professors
    List<Course> findByProfessorId(Long professorId);
    // Finde Kurse nach Semester
    List<Course> findBySemester(String semester);
    // Finde Kurs nach Name
    List<Course> findByNameContainingIgnoreCase(String name);
}