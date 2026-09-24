package com.shulventures.solarservicesbackend.service;

import com.shulventures.solarservicesbackend.entity.Employee;
import com.shulventures.solarservicesbackend.entity.EmployeeDocument;
import com.shulventures.solarservicesbackend.repository.EmployeeDocumentRepository;
import com.shulventures.solarservicesbackend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeDocumentService {

    private final EmployeeDocumentRepository employeeDocumentRepository;
    private final EmployeeRepository employeeRepository;


    // =============== UPLOAD DIRECTORY ============================

    private final Path uploadDirectory;

    public EmployeeDocumentService(
            EmployeeDocumentRepository employeeDocumentRepository,
            EmployeeRepository employeeRepository,
            @Value("${employee.file.upload-dir}")
            String uploadDir
    ) {

        this.employeeDocumentRepository =
                employeeDocumentRepository;

        this.employeeRepository =
                employeeRepository;

        this.uploadDirectory =
                Paths.get(uploadDir)
                        .toAbsolutePath()
                        .normalize();


        try {

            Files.createDirectories(
                    uploadDirectory
            );

        } catch (IOException error) {

            throw new RuntimeException(
                    "Could not create employee document upload directory.",
                    error
            );
        }
    }


    // ============================================================
    // UPLOAD DOCUMENT
    // ============================================================

    public EmployeeDocument uploadDocument(
            Long employeeId,
            MultipartFile file
    ) {

        // --------------------------------------------------------
        // VALIDATE EMPLOYEE
        // --------------------------------------------------------

        Employee employee =
                employeeRepository.findById(
                        employeeId
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found with id: "
                                        + employeeId
                        )
                );


        // --------------------------------------------------------
        // VALIDATE FILE
        // --------------------------------------------------------

        if (file == null ||
                file.isEmpty()) {

            throw new RuntimeException(
                    "Please select a document."
            );
        }


        // --------------------------------------------------------
        // ORIGINAL FILE NAME
        // --------------------------------------------------------

        String originalFileName =
                file.getOriginalFilename();


        if (originalFileName == null ||
                originalFileName.isBlank()) {

            throw new RuntimeException(
                    "Invalid file name."
            );
        }


        // --------------------------------------------------------
        // FILE EXTENSION
        // --------------------------------------------------------

        String extension = "";

        int dotIndex =
                originalFileName.lastIndexOf(".");


        if (dotIndex >= 0) {

            extension =
                    originalFileName
                            .substring(dotIndex)
                            .toLowerCase();
        }


        // --------------------------------------------------------
        // ALLOWED FILE TYPES
        // --------------------------------------------------------

        if (!extension.equals(".pdf") &&
                !extension.equals(".jpg") &&
                !extension.equals(".jpeg") &&
                !extension.equals(".png")) {

            throw new RuntimeException(
                    "Only PDF, JPG, JPEG and PNG files are allowed."
            );
        }


        // --------------------------------------------------------
        // UNIQUE STORED FILE NAME
        // --------------------------------------------------------

        String storedFileName =
                UUID.randomUUID()
                        + extension;


        // --------------------------------------------------------
        // FILE PATH
        // --------------------------------------------------------

        Path targetPath =
                uploadDirectory
                        .resolve(storedFileName)
                        .normalize();


        // --------------------------------------------------------
        // SECURITY CHECK
        // --------------------------------------------------------

        if (!targetPath.startsWith(
                uploadDirectory
        )) {

            throw new RuntimeException(
                    "Invalid file path."
            );
        }


        // --------------------------------------------------------
        // SAVE PHYSICAL FILE
        // --------------------------------------------------------

        try {

            Files.copy(
                    file.getInputStream(),
                    targetPath
            );

        } catch (IOException error) {

            throw new RuntimeException(
                    "Failed to save employee document.",
                    error
            );
        }


        // --------------------------------------------------------
        // CREATE DATABASE RECORD
        // --------------------------------------------------------

        EmployeeDocument document =
                new EmployeeDocument();


        document.setEmployee(
                employee
        );


        document.setFileName(
                originalFileName
        );


        document.setStoredFileName(
                storedFileName
        );


        // --------------------------------------------------------
        // DETERMINE MIME TYPE
        // --------------------------------------------------------

        String fileType =
                file.getContentType();


        if (fileType == null ||
                fileType.equals(
                        "application/octet-stream"
                )) {

            if (extension.equals(".pdf")) {

                fileType =
                        "application/pdf";

            } else if (
                    extension.equals(".jpg") ||
                            extension.equals(".jpeg")
            ) {

                fileType =
                        "image/jpeg";

            } else if (
                    extension.equals(".png")
            ) {

                fileType =
                        "image/png";
            }
        }


        document.setFileType(
                fileType
        );


        document.setFileSize(
                file.getSize()
        );


        document.setFilePath(
                targetPath.toString()
        );


        return employeeDocumentRepository
                .save(document);
    }


    // ============================================================
    // GET EMPLOYEE DOCUMENTS
    // ============================================================

    public List<EmployeeDocument>
    getDocumentsByEmployee(
            Long employeeId
    ) {

        if (!employeeRepository.existsById(
                employeeId
        )) {

            throw new RuntimeException(
                    "Employee not found with id: "
                            + employeeId
            );
        }


        return employeeDocumentRepository
                .findByEmployeeId(
                        employeeId
                );
    }


    // ============================================================
    // GET DOCUMENT
    // ============================================================

    public EmployeeDocument getDocument(
            Long documentId
    ) {

        return employeeDocumentRepository
                .findById(documentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee document not found with id: "
                                        + documentId
                        )
                );
    }


    // ============================================================
    // GET DOCUMENT FILE
    // ============================================================

    public Resource getDocumentFile(
            Long documentId
    ) {

        EmployeeDocument document =
                getDocument(documentId);


        try {

            Path filePath =
                    Paths.get(
                            document.getFilePath()
                    ).normalize();


            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );


            if (!resource.exists()) {

                throw new RuntimeException(
                        "Employee document file does not exist."
                );
            }


            return resource;

        } catch (
                MalformedURLException error
        ) {

            throw new RuntimeException(
                    "Unable to load employee document.",
                    error
            );
        }
    }


    // ============================================================
    // DELETE DOCUMENT
    // ============================================================

    public void deleteDocument(
            Long documentId
    ) {

        EmployeeDocument document =
                getDocument(documentId);


        // --------------------------------------------------------
        // DELETE PHYSICAL FILE
        // --------------------------------------------------------

        try {

            Path filePath =
                    Paths.get(
                            document.getFilePath()
                    ).normalize();


            Files.deleteIfExists(
                    filePath
            );

        } catch (IOException error) {

            throw new RuntimeException(
                    "Failed to delete employee document file.",
                    error
            );
        }


        // --------------------------------------------------------
        // DELETE DATABASE RECORD
        // --------------------------------------------------------

        employeeDocumentRepository.delete(
                document
        );
    }
}
