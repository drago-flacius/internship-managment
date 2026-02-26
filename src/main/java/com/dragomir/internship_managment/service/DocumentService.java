package com.dragomir.internship_managment.service;

import com.dragomir.internship_managment.domain.DocumentType;
import com.dragomir.internship_managment.domain.Student;
import com.dragomir.internship_managment.domain.StudentDocument;
import com.dragomir.internship_managment.repository.StudentDocumentRepository;
import com.dragomir.internship_managment.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final StudentRepository studentRepository;
    private final StudentDocumentRepository documentRepository;

    @Value("${app.cv.upload-dir}")
    private String uploadDir;

    @Transactional
    public Map<String, Object> uploadCV(Long studentId, MultipartFile file) throws IOException {
        Student student = getStudentById(studentId);
        return uploadDocument(student, file, DocumentType.CV);
    }

    @Transactional
    public Map<String, Object> uploadDiary(Long studentId, MultipartFile file) throws IOException {
        Student student = getStudentById(studentId);
        return uploadDocument(student, file, DocumentType.DIARY);
    }

    public Resource downloadCV(Long studentId) throws IOException {
        Student student = getStudentById(studentId);
        return loadDocument(student, DocumentType.CV);
    }

    public Resource downloadDiary(Long studentId) throws IOException {
        Student student = getStudentById(studentId);
        return loadDocument(student, DocumentType.DIARY);
    }

    @Transactional
    public void deleteCV(Long studentId) throws IOException {
        Student student = getStudentById(studentId);
        deleteDocument(student, DocumentType.CV);
    }

    @Transactional
    public void deleteDiary(Long studentId) throws IOException {
        Student student = getStudentById(studentId);
        deleteDocument(student, DocumentType.DIARY);
    }

  

    private Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
    }

    private Map<String, Object> uploadDocument(Student student, MultipartFile file, DocumentType type) throws IOException {
        if (!"application/pdf".equals(file.getContentType())) {
            throw new RuntimeException("Only PDF files are allowed");
        }

        Path folder = Paths.get(uploadDir, type.name().toLowerCase());
        Files.createDirectories(folder);

        String safeIndex = student.getIndexNumber().replaceAll("[^a-zA-Z0-9_-]", "_");

        String filename = "student-" + safeIndex + ".pdf";
        Path targetPath = folder.resolve(filename);

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        StudentDocument document = documentRepository.findByStudentAndType(student, type)
                .orElse(new StudentDocument());

        document.setStudent(student);
        document.setType(type);
        document.setFileName(file.getOriginalFilename());
        document.setContentType(file.getContentType());
        document.setFilePath(type.name().toLowerCase() + "/" + filename);
        document.setUploadedAt(LocalDateTime.now());

        documentRepository.save(document);

        Map<String, Object> info = new HashMap<>();
        info.put("fileName", file.getOriginalFilename());
        info.put("uploadedAt", document.getUploadedAt());
        info.put("type", type.name());

        return info;
    }

    private Resource loadDocument(Student student, DocumentType type) throws IOException {
        StudentDocument document = documentRepository.findByStudentAndType(student, type)
                .orElseThrow(() -> new RuntimeException(type + " not found"));

        Path filePath = Paths.get(uploadDir, document.getFilePath());
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Could not read file: " + filePath);
        }

        return resource;
    }

    private void deleteDocument(Student student, DocumentType type) throws IOException {
        StudentDocument document = documentRepository.findByStudentAndType(student, type)
                .orElse(null);

        if (document == null) return;

        Path filePath = Paths.get(uploadDir, document.getFilePath());
        Files.deleteIfExists(filePath);

        documentRepository.delete(document);
    }
}