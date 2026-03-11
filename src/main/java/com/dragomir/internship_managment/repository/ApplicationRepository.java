package com.dragomir.internship_managment.repository;

import com.dragomir.internship_managment.domain.Application;
import com.dragomir.internship_managment.domain.ApplicationStatus;
import com.dragomir.internship_managment.domain.Internship;
import com.dragomir.internship_managment.domain.InternshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudent_Id(Long studentId);
    List<Application> findByInternshipId(Long internshipId);
    List<Application> findByStatus(ApplicationStatus status);
    @Query("SELECT a FROM Application a " +
            "JOIN FETCH a.student s " +
            "JOIN FETCH a.internship i " +
            "WHERE s.email = :email " +
            "AND a.status = 'ACCEPTED'") // Koristi 'APPROVED' ako ti je takav Enum
    Optional<Application> findAcceptedByStudentEmail(@Param("email") String email);
}

