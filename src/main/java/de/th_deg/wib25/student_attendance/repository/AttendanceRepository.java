package de.th_deg.wib25.student_attendance.repository;

import de.th_deg.wib25.student_attendance.entity.Attendance;
import org.springframework.data. jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // Finde alle Anwesenheiten eines Students
    List<Attendance> findByStudentId(Long studentId);
    // Finde alle Anwesenheiten einer Session
    List<Attendance> findBySessionId(Long sessionId);
    // Finde Anwesenheit eines Students bei einer Session
    Optional<Attendance> findByStudentIdAndSessionId(Long studentId, Long sessionId);
    // Finde nach Status (PRESENT, LATE, ABSENT)
    List<Attendance> findByStatus(String status);
    // Zähle Anwesenheiten eines Students
    long countByStudentIdAndStatus(Long studentId, String status);
}