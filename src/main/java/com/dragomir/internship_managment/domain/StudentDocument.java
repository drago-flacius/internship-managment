package com.dragomir.internship_managment.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class StudentDocument {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Student student;
    @Enumerated(EnumType.STRING)
    private DocumentType type;
    private String fileName;
    private String contentType;
    private String filePath;
    private LocalDateTime uploadedAt;
}