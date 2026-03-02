package com.dragomir.internship_managment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"applications"})
@ToString(callSuper = true, exclude = {"applications"})
@Entity
@Table(name = "students")
public class Student extends User {

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "index_number", unique = true, nullable = false, length = 50)
    private String indexNumber;


    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "study_year", length = 50)
    private String studyYear;

    @Column(length = 500)
    private String bio;

    @Column(name = "gpa")

    private Double gpa;

    @Column(name = "skills", length = 1000)
    private String skills; //







    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();


    public void addApplication(Application application) {
        applications.add(application);
        application.setStudent(this);
    }

    public void removeApplication(Application application) {
        applications.remove(application);
        application.setStudent(null);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}