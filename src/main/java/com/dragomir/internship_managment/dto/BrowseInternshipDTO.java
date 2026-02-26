package com.dragomir.internship_managment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BrowseInternshipDTO {
    private Long id;
    private String companyName;
    private String location;
    private Integer duration;
    private LocalDate deadline;
    private String title;
    private String description;
    private int applicantsCount;
}
