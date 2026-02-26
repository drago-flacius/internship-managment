package com.dragomir.internship_managment.repository;
import com.dragomir.internship_managment.domain.Company;
import com.dragomir.internship_managment.domain.InternshipStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.dragomir.internship_managment.domain.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InternshipRepository extends JpaRepository<Internship, Long> {

    @Query("SELECT i FROM Internship i " +
            "WHERE (:search IS NULL OR :search = '' OR LOWER(i.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:location IS NULL OR :location = '' OR LOWER(i.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:company IS NULL OR :company = '' OR LOWER(i.company.companyName) LIKE LOWER(CONCAT('%', :company, '%'))) " +
            "AND (:weeksDuration IS NULL OR i.durationWeeks >= :weeksDuration) " +
            "AND (:isPaid IS NULL OR i.isPaid = :isPaid) " +
            "AND (:status IS NULL OR i.status = :status)")
    Page<Internship> getFilteredInternships(
            @Param("search") String search,
            @Param("location") String location,
            @Param("company") String company,
            @Param("weeksDuration") Integer weeksDuration,
            @Param("isPaid") Boolean isPaid,
            @Param("status") InternshipStatus status,   // <-- change here
            Pageable pageable
    );

    List<Internship> findByCompany(Company company);

    List<Internship> findByStatus(InternshipStatus status);

}
