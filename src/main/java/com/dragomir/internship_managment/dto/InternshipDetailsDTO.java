package com.dragomir.internship_managment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor

public class InternshipDetailsDTO {

    private Long id;
    private String title;
    private String description;

    private String company;

    private String location;
    private Integer durationWeeks;

    private Boolean isPaid;
    private Double salary;

    private String requirements;
    private String responsibilities;

    private LocalDate deadline;
}
