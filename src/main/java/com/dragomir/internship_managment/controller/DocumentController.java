package com.dragomir.internship_managment.controller;

import com.dragomir.internship_managment.dto.ApiResponse;
import com.dragomir.internship_managment.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    // STUDENT uploads THEIR diary
    @PostMapping("/{studentId}/diary")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse> uploadDiary(
            @PathVariable  Long studentId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        documentService.uploadDiary(studentId, file);
        ApiResponse response = new ApiResponse(true, "Dnevnik uspešno posavljen !");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{studentId}/diary")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Resource> downloadDiary(
            @PathVariable Long studentId
    ) throws IOException {
        Resource diary = documentService.downloadDiary(studentId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"internship-diary.pdf\"")
                .body(diary);
    }
    @PostMapping("/{studentId}/cv")
    public ResponseEntity<ApiResponse> uploadCV(@RequestParam("cv") MultipartFile file,
                                                @PathVariable   Long studentId) throws IOException {
        Map<String, Object> cvInfo =  documentService.uploadCV(studentId , file);
        return ResponseEntity.ok(new ApiResponse(true, "CV uploaded ", cvInfo));
    }

    @DeleteMapping("/{studentId}/cv")
    public ResponseEntity<ApiResponse> deleteCV(@PathVariable Long studentId) throws IOException {
        documentService.deleteCV(studentId);

        ApiResponse response = new ApiResponse(true, "CV uspešno obrisan!");
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{studentId}/cv")
    public ResponseEntity<Resource> downloadCV(@PathVariable Long studentId) throws IOException {
        Resource resource = documentService.downloadCV(studentId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/{studentId}/cv/meta")
    public ResponseEntity<ApiResponse> getCVMetadata(@PathVariable Long studentId) {
        Map<String, Object> metadata = documentService.getCVMetadata(studentId);

        if (metadata == null) {
            return ResponseEntity.ok(new ApiResponse(true, "No CV uploaded", null));
        }

        return ResponseEntity.ok(new ApiResponse(true, "CV metadata fetched", metadata));
    }
}