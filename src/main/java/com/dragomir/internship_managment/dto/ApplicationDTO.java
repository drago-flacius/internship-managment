package com.dragomir.internship_managment.dto;

import com.dragomir.internship_managment.domain.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDTO {
    private Long id;
    private Long studentId;
    private ApplicationStatus status;
    private String appliedAt;
    private String coverLetter;
    private String studentName;
    private Long internshipId;
    private String internshipTitle;
    private String internshipCompanyName;
    private String internshipDescription;
    private String internshipLocation;
    private Integer internshipDuration;
    private Double internshipSalary;
    private Boolean internshipPaid;
}
