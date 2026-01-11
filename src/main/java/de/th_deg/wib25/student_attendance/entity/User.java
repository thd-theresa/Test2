package de.th_deg.wib25.student_attendance.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id //Primary Key
    @GeneratedValue(strategy = GenerationType. IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String role; //Student, Dozent oder Admin

    @Column(nullable = true)
    private Long matriculationNumber;

    // Konstruktor
    public User() {}

    public User(String email, String password, String firstName, String lastName, String role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    // Getters und Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public Long getMatriculationNumber() {
        return matriculationNumber;
    }
    public void setMatriculationNumber(Long matriculationNumber) {
        this.matriculationNumber = matriculationNumber;
    }


    //Hilfsmethoden
    public boolean isProfessor() {
        return "PROFESSOR".equals(this.role);
    }
    public boolean isStudent() {
        return "STUDENT".equals(this.role);
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
