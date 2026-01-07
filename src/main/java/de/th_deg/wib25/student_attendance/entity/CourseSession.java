package de.th_deg.wib25.student_attendance.entity;

import jakarta. persistence.*;
import java.time. LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course_sessions")
public class CourseSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    private String room;

    private String type; // "VORLESUNG", "ÜBUNG", "PRAKTIKUM"

    // Anwesenheiten für diesen Termin
    @OneToMany(mappedBy = "session", cascade = CascadeType. ALL, orphanRemoval = true)
    private List<Attendance> attendances = new ArrayList<>();

    // Constructors
    public CourseSession() {}

    public CourseSession(Course course, LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime) {
        this.course = course;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    // Helper Methods
    public String getFormattedDateTime() {
        return date.toLocalDate() + " " + startTime.toLocalTime() + " - " + endTime. toLocalTime();
    }
}