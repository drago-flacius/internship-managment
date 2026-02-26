package com.dragomir.internship_managment.controller;

import com.dragomir.internship_managment.dto.ApiResponse;
import com.dragomir.internship_managment.service.CompanyService;
import com.dragomir.internship_managment.service.StudentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/companies")
@CrossOrigin(origins = "http://localhost:3000")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<ApiResponse> getAllCompanies() {

        var companies = companyService.getAllCompanies();
        return ResponseEntity.ok(new ApiResponse(true, "Companies retrieved successfully", companies ));
    }

    @GetMapping("/{id}/internships")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<ApiResponse> getComaniesInterships(@PathVariable Long id) {

        var internships = companyService.getInternshipsByCompany(id);
        return ResponseEntity.ok(new ApiResponse(true, "Internship retrieved successfully", internships));
    }


}
