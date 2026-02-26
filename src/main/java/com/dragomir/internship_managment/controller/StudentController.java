package com.dragomir.internship_managment.controller;

import com.dragomir.internship_managment.domain.Application;
import com.dragomir.internship_managment.domain.Student;
import com.dragomir.internship_managment.dto.ApiResponse;
import com.dragomir.internship_managment.dto.ApplicationDTO;
import com.dragomir.internship_managment.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService userService) {
        this.studentService = userService;
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('FACULTY')")
    public ResponseEntity<ApiResponse> getStudents(Authentication authentication) {
        List<Student> students = studentService.getAllStudents();
        return  ResponseEntity.ok
                (new ApiResponse(true, "Students found", students));

    }

    @GetMapping("/{id}/applications")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<ApiResponse> getStudentApplications(@PathVariable Long id) {
        List<ApplicationDTO> applications = studentService.getStudentApplications(id);

        return ResponseEntity.ok(
                new ApiResponse(true, "Apps found", applications));

    }


    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getStudentProfile(Authentication authentication){
        String email = authentication.getName();
        Student student = studentService.getStudentByEmail(email);
        return  ResponseEntity.ok
                (new ApiResponse(true, "Student fount", student));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateStudentProfile(@RequestBody Student updatedStudent,
                                                            Authentication authentication) {
        studentService.updateStudent(updatedStudent);
        return ResponseEntity.ok(new ApiResponse(true, "Profile updated", updatedStudent));
    }




}
