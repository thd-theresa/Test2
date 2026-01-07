package de.th_deg.wib25.student_attendance.entity;

import jakarta. persistence.*;
import java.time. LocalDateTime;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private CourseSession session;

    @Column(nullable = false)
    private String status; // "PRESENT", "LATE", "ABSENT", "EXCUSED"

    private Integer delayMinutes; // null wenn nicht verspätet

    @Column(length = 500)
    private String excuse; // Ausrede/Begründung

    @Column(nullable = false)
    private LocalDateTime timestamp; // Wann wurde eingetragen

    // Constructors
    public Attendance() {}

    public Attendance(User student, CourseSession session, String status) {
        this.student = student;
        this.session = session;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public CourseSession getSession() {
        return session;
    }

    public void setSession(CourseSession session) {
        this.session = session;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDelayMinutes() {
        return delayMinutes;
    }

    public void setDelayMinutes(Integer delayMinutes) {
        this.delayMinutes = delayMinutes;
    }

    public String getExcuse() {
        return excuse;
    }

    public void setExcuse(String excuse) {
        this.excuse = excuse;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Helper Methods
    public boolean isPresent() {
        return "PRESENT".equals(this.status);
    }

    public boolean isLate() {
        return "LATE". equals(this.status);
    }

    public boolean isAbsent() {
        return "ABSENT".equals(this. status);
    }

    public boolean isExcused() {
        return "EXCUSED".equals(this.status);
    }
}