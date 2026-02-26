package com.dragomir.internship_managment.service;

import com.dragomir.internship_managment.controller.StudentController;
import com.dragomir.internship_managment.domain.Application;
import com.dragomir.internship_managment.domain.Student;
import com.dragomir.internship_managment.dto.ApplicationDTO;
import com.dragomir.internship_managment.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Value("${app.cv.upload-dir}")
    private String uploadDir;
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public void updateStudent(Student student) {
        studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<ApplicationDTO> getStudentApplications(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return student.getApplications().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ApplicationDTO mapToDTO(Application app) {
        ApplicationDTO dto = new ApplicationDTO();

        dto.setId(app.getId());
        dto.setStudentId(app.getStudent().getId());
        dto.setStatus(app.getStatus());
        dto.setAppliedAt(app.getAppliedAt().toString());
        dto.setCoverLetter(app.getCoverLetter());
        dto.setStudentName(app.getStudent().getFirstName() + " " + app.getStudent().getLastName());
        dto.setInternshipId(app.getInternship().getId());
        dto.setInternshipTitle(app.getInternship().getTitle());
        dto.setInternshipCompanyName(app.getInternship().getCompany().getCompanyName());
        dto.setInternshipDescription(app.getInternship().getDescription());
        dto.setInternshipLocation(app.getInternship().getLocation());
        dto.setInternshipDuration(app.getInternship().getDurationWeeks());
        dto.setInternshipSalary(app.getInternship().getSalary());
        dto.setInternshipPaid(app.getInternship().getIsPaid());

        return dto;
    }
}
