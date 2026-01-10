package de.th_deg.wib25.student_attendance.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String semester;

    @Column(length = 1000)
    private String description;

    // Professor, der den Kurs leitet
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = true) //hier auf True geändert, um auch Kurse testweise ohne Prof anzulegen
    private User professor;

    // Studenten im Kurs (Many-to-Many)
    @ManyToMany
    @JoinTable(
            name = "course_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<User> students = new ArrayList<>();

    // Termine des Kurses
    @OneToMany(mappedBy = "course", cascade = CascadeType. ALL, orphanRemoval = true)
    private List<CourseSession> sessions = new ArrayList<>();

    // Constructors
    public Course() {}

    public Course(String name, String semester, User professor) {
        this.name = name;
        this.semester = semester;
        this.professor = professor;
    }

    // Getters & Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getProfessor() {
        return professor;
    }

    public void setProfessor(User professor) {
        this.professor = professor;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public List<CourseSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<CourseSession> sessions) {
        this.sessions = sessions;
    }

    // Helper Methods
    public void addStudent(User student) {
        if (!students.contains(student)) {
            students.add(student);
        }
    }

    public void removeStudent(User student) {
        students.remove(student);
    }

    public int getStudentCount() {
        return students.size();
    }
}
