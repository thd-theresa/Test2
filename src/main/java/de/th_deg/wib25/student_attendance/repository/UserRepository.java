package de.th_deg.wib25.student_attendance.repository;

import de.th_deg.wib25.student_attendance.entity.User;
import org.springframework.data.jpa. repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Finde User per Email
    Optional<User> findByEmail(String email);
    // Finde alle Studenten
    List<User> findByRole(String role);
    // Finde Student per Matrikelnummer
    Optional<User> findByMatriculationNumber(Long matriculationNumber);
    // Prüfe ob Email existiert
    boolean existsByEmail(String email);
}