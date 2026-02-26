package com.dragomir.internship_managment.service;

import com.dragomir.internship_managment.domain.Company;
import com.dragomir.internship_managment.domain.Internship;
import com.dragomir.internship_managment.domain.InternshipStatus;
import com.dragomir.internship_managment.dto.BrowseInternshipDTO;
import com.dragomir.internship_managment.dto.InternshipDetailsDTO;
import com.dragomir.internship_managment.repository.CompanyRepository;
import com.dragomir.internship_managment.repository.InternshipRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class InternshipServiceTest {

    @Mock
    private  InternshipRepository internshipRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private InternshipService internshipService;



    @Test
    void shouldReturnInternshipDTOByInternshipId() {
        Long id = 1L;
        Internship internship = new Internship();
        Company company = new Company();
        company.setCompanyName("Company Name");
        internship.setCompany(company);
        when(internshipRepository.findById(id)).thenReturn(Optional.of(internship));

        Internship internshipDTO = internshipService.getInternshipById(id);

        assertNotNull(internshipDTO);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenInternshipNotFound() {
        Long id = 1L;

        when(internshipRepository.findById(id)).thenReturn(Optional.empty());


        assertThrows(RuntimeException.class, () -> internshipService.getInternshipById(id));


    }

    @Test
    void shouldReturnEmptyPageIfNoInternshipsFound() {
        int page= 2;
        int limit = 10;
        String search = "Java";
        String location = "Belgrade";
        String company = "Axians";
        Integer weeksDuration = 3;
        Boolean isPaid = false;
        boolean isStudent = true;
        Pageable pageable = PageRequest.of(page - 1, limit);
        InternshipStatus status = isStudent ? InternshipStatus.APPROVED : null;
        when(internshipRepository.getFilteredInternships(search, location, company, weeksDuration, isPaid, status, pageable)).thenReturn(Page.empty());

        Page<Internship> internships = internshipService.getFilteredInternships(page, limit, search, location, company, weeksDuration, isPaid, isStudent);

        assertNotNull(internships);
        assertTrue(internships.isEmpty());

    }

}