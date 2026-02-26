package com.dragomir.internship_managment.repository;



import com.dragomir.internship_managment.domain.Student;
import com.dragomir.internship_managment.domain.StudentDocument;
import com.dragomir.internship_managment.domain.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentDocumentRepository extends JpaRepository<StudentDocument, Long> {
    Optional<StudentDocument> findByStudentAndType(Student student, DocumentType type);
}
