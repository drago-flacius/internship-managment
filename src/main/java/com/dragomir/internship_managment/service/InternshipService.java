package com.dragomir.internship_managment.service;

import com.dragomir.internship_managment.domain.Company;
import com.dragomir.internship_managment.domain.Internship;
import com.dragomir.internship_managment.domain.InternshipStatus;
import com.dragomir.internship_managment.dto.InternshipDTO;
import com.dragomir.internship_managment.dto.PendingInternshipDTO;
import com.dragomir.internship_managment.repository.ApplicationRepository;
import com.dragomir.internship_managment.repository.CompanyRepository;
import com.dragomir.internship_managment.repository.InternshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.List;

@Service
public class InternshipService {

    private final InternshipRepository internshipRepository;
    private final CompanyRepository companyRepository;
    @Autowired
    public InternshipService(InternshipRepository internshipRepository, CompanyRepository companyRepository) {
        this.internshipRepository = internshipRepository;
        this.companyRepository = companyRepository;

    }

    public Internship createOfferByUser(Internship offer, Authentication auth) {
        String email = auth.getName(); // get email from JWT
        Company company = companyRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        offer.setCompany(company);
        return internshipRepository.save(offer);
    }

    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }

    public Page<Internship> getFilteredInternships(
            int page, int limit,
            String search, String location, String company,
            Integer weeksDuration, Boolean isPaid, boolean isStudent) {

        Pageable pageable = PageRequest.of(page - 1, limit);

        // Ako je STUDENT, filtriraj samo APPROVED
        InternshipStatus status = isStudent ? InternshipStatus.APPROVED : null;

        return internshipRepository.getFilteredInternships(
                search,
                location,
                company,
                weeksDuration,
                isPaid,
                status,
                pageable
        );
    }

    public Internship getInternshipById(Long id) {
        return internshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Internship with id " + id + " not found"));
    }


    public Object getInternshipsByCompany(Authentication auth) {
        String email = auth.getName();
        Company company = companyRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        List<Internship> internships = internshipRepository.findByCompany(company);

        return internships.stream().map(internship ->  new InternshipDTO(
                internship.getId(),
                internship.getTitle(),
                internship.getDescription(),
                internship.getApplicationsCount()
        ));



    }

    public Internship approve(Long id, Authentication auth) {
        return null;
    }

    public Internship reject(Long id, Authentication auth) {
        return null;
    }

    public List<PendingInternshipDTO> getPendingForFaculty() {
        return internshipRepository
                .findByStatus(InternshipStatus.PENDING)
                .stream()
                .map(i -> new PendingInternshipDTO(
                        i.getId(),
                        i.getTitle(),
                        i.getCompany().getCompanyName(),
                        i.getCategory(),
                        i.getDurationWeeks(),
                        i.getAvailablePositions()
                ))
                .toList();
    }

    public Internship decideInternshipPlacement(Long id, String action, Authentication auth) {
        Internship internship = internshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        if (internship.getStatus() != InternshipStatus.PENDING) {
            throw new RuntimeException("Internship was already approved/rejected");
        }

        if ("approve".equalsIgnoreCase(action)) {
            internship.setStatus(InternshipStatus.APPROVED);
        } else if ("reject".equalsIgnoreCase(action)) {
            internship.setStatus(InternshipStatus.REJECTED);
        } else {
            throw new IllegalArgumentException("Invalid action: must be 'approve' or 'reject'");
        }

        return internshipRepository.save(internship);
    }
}