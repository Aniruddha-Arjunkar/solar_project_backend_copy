package com.shulventures.solarservicesbackend.controller;

import com.shulventures.solarservicesbackend.entity.EmployeeDocument;
import com.shulventures.solarservicesbackend.service.EmployeeDocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/employee-documents")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeDocumentController {

    private final EmployeeDocumentService employeeDocumentService;


    public EmployeeDocumentController(EmployeeDocumentService employeeDocumentService) {
        this.employeeDocumentService =
                employeeDocumentService;
    }


    // =================== UPLOAD DOCUMENT =========================

    @PostMapping(
            value = "/employee/{employeeId}",
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<EmployeeDocument>
    uploadDocument(
            @PathVariable Long employeeId,
            @RequestParam("file")
            MultipartFile file
    ) {

        EmployeeDocument document =
                employeeDocumentService.uploadDocument(
                        employeeId,
                        file
                );


        return ResponseEntity.ok(
                document
        );
    }


    // ================== GET EMPLOYEE DOCUMENTS ================================

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeDocument>>
    getEmployeeDocuments(@PathVariable Long employeeId) {
        return ResponseEntity.ok(
                employeeDocumentService
                        .getDocumentsByEmployee(
                                employeeId
                        )
        );
    }


    // ============================ VIEW DOCUMENT ==========================

    @GetMapping("/{documentId}/view")
    public ResponseEntity<Resource>
    viewDocument(@PathVariable Long documentId) {
        EmployeeDocument document =
                employeeDocumentService
                        .getDocument(
                                documentId
                        );

        Resource resource =
                employeeDocumentService
                        .getDocumentFile(
                                documentId
                        );


        MediaType mediaType;

        try {
            mediaType =
                    MediaType.parseMediaType(
                            document.getFileType()
                    );

        } catch (Exception error) {

            mediaType =
                    MediaType.APPLICATION_OCTET_STREAM;
        }


        if (
                MediaType.APPLICATION_OCTET_STREAM
                        .equals(mediaType)
        ) {

            String fileName =
                    document
                            .getFileName()
                            .toLowerCase();


            if (fileName.endsWith(".pdf")) {

                mediaType =
                        MediaType.APPLICATION_PDF;

            } else if (
                    fileName.endsWith(".jpg") ||
                            fileName.endsWith(".jpeg")
            ) {

                mediaType =
                        MediaType.IMAGE_JPEG;

            } else if (
                    fileName.endsWith(".png")
            ) {
                mediaType = MediaType.IMAGE_PNG;
            }
        }


        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" +
                                document.getFileName() +
                                "\""
                )
                .body(resource);
    }


    // ========================= DELETE DOCUMENT ====================

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void>
    deleteDocument(@PathVariable Long documentId) {
        employeeDocumentService
                .deleteDocument(
                        documentId
                );
        return ResponseEntity
                .noContent()
                .build();
    }
}
