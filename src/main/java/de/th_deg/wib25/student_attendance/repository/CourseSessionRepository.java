package de.th_deg.wib25.student_attendance.repository;

import de.th_deg.wib25.student_attendance.entity.CourseSession;
import org.springframework.data.jpa.repository. JpaRepository;
import org. springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java. util.List;

@Repository
public interface CourseSessionRepository extends JpaRepository<CourseSession, Long> {
    // Finde alle Sessions eines Kurses
    List<CourseSession> findByCourseId(Long courseId);
    // Finde Sessions nach Datum
    List<CourseSession> findByDateBetween(LocalDateTime start, LocalDateTime end);
    // Finde Sessions nach Raum
    List<CourseSession> findByRoom(String room);
}