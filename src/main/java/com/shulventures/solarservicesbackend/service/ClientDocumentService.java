package com.shulventures.solarservicesbackend.service;

import com.shulventures.solarservicesbackend.entity.Client;
import com.shulventures.solarservicesbackend.entity.ClientDocument;
import com.shulventures.solarservicesbackend.repository.ClientDocumentRepository;
import com.shulventures.solarservicesbackend.repository.ClientRepository;
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
public class ClientDocumentService {

    private final ClientDocumentRepository clientDocumentRepository;
    private final ClientRepository clientRepository;


    //====================================================
    // UPLOAD DIRECTORY
    //====================================================

    private final Path uploadDirectory;

   //Contructor Dependency Injection
    public ClientDocumentService(
            ClientDocumentRepository clientDocumentRepository,
            ClientRepository clientRepository,
            @Value("${file.upload-dir}") String uploadDir
    ) {

        this.clientDocumentRepository = clientDocumentRepository;

        this.clientRepository = clientRepository;

        this.uploadDirectory = Paths.get(uploadDir)
                               .toAbsolutePath()
                               .normalize();

        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException error) {

            throw new RuntimeException(
                    "Could not create document upload directory.",
                    error
            );
        }
    }


    // UPLOAD DOCUMENT

    public ClientDocument uploadDocument(
            Long clientId,
            MultipartFile file
    ) {

        //================================================
        // VALIDATE CLIENT
        //================================================

        Client client =
                clientRepository.findById(clientId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Client not found with id: "
                                                + clientId
                                )
                        );


        //================================================
        // VALIDATE FILE
        //================================================

        if (file == null || file.isEmpty()) {

            throw new RuntimeException(
                    "Please select a document."
            );
        }


        //================================================
        // ORIGINAL FILE NAME
        //================================================

        String originalFileName =
                file.getOriginalFilename();

        if (originalFileName == null ||
                originalFileName.isBlank()) {

            throw new RuntimeException(
                    "Invalid file name."
            );
        }


        //================================================
        // FILE EXTENSION
        //================================================

        String extension = "";

        int dotIndex =
                originalFileName.lastIndexOf(".");

        if (dotIndex >= 0) {

            extension =
                    originalFileName.substring(dotIndex)
                            .toLowerCase();
        }


        //================================================
        // ALLOWED FILE TYPES
        //================================================

        if (!extension.equals(".pdf") &&
                !extension.equals(".jpg") &&
                !extension.equals(".jpeg") &&
                !extension.equals(".png")) {

            throw new RuntimeException(
                    "Only PDF, JPG, JPEG and PNG files are allowed."
            );
        }


        //================================================
        // GENERATE UNIQUE FILE NAME
        //================================================

        String storedFileName =
                UUID.randomUUID()
                        + extension;


        //================================================
        // FILE PATH
        //================================================

        Path targetPath =
                uploadDirectory
                        .resolve(storedFileName)
                        .normalize();


        //================================================
        // SECURITY CHECK
        //================================================

        if (!targetPath.startsWith(uploadDirectory)) {

            throw new RuntimeException(
                    "Invalid file path."
            );
        }


        //================================================
        // SAVE FILE
        //================================================

        try {

            Files.copy(
                    file.getInputStream(),
                    targetPath
            );

        } catch (IOException error) {

            throw new RuntimeException(
                    "Failed to save document.",
                    error
            );
        }


        //================================================
        // SAVE DOCUMENT INFORMATION
        //================================================

        ClientDocument document =
                new ClientDocument();

        document.setClient(client);

        document.setFileName(
                originalFileName
        );

        document.setStoredFileName(
                storedFileName
        );

//        document.setFileType(
//                file.getContentType()
//        );


        // DETERMINE FILE TYPE

        String fileType = file.getContentType();

        if (fileType == null ||
                fileType.equals("application/octet-stream")) {

            if (extension.equals(".pdf")) {

                fileType = "application/pdf";

            } else if (extension.equals(".jpg") ||
                    extension.equals(".jpeg")) {

                fileType = "image/jpeg";

            } else if (extension.equals(".png")) {

                fileType = "image/png";
            }
        }

        document.setFileType(fileType);

        document.setFileSize(
                file.getSize()
        );

        document.setFilePath(
                targetPath.toString()
        );


        return clientDocumentRepository.save(
                document
        );
    }


    //====================================================
    // GET DOCUMENTS BY CLIENT
    //====================================================

    public List<ClientDocument> getDocumentsByClient(
            Long clientId
    ) {

        return clientDocumentRepository
                .findByClientId(clientId);
    }


    //====================================================
    // GET DOCUMENT FILE
    //====================================================

    public Resource getDocumentFile(Long documentId) {

        ClientDocument document =
                clientDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Document not found with id: "
                                                + documentId
                                )
                        );

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
                        "Document file does not exist."
                );
            }

            return resource;

        } catch (MalformedURLException error) {

            throw new RuntimeException(
                    "Unable to load document.",
                    error
            );
        }
    }


    //====================================================
    // GET DOCUMENT
    //====================================================

    public ClientDocument getDocument(
            Long documentId
    ) {

        return clientDocumentRepository
                .findById(documentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Document not found with id: "
                                        + documentId
                        )
                );
    }


    //====================================================
    // DELETE DOCUMENT
    //====================================================

    public void deleteDocument(Long documentId) {

        ClientDocument document =
                clientDocumentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Document not found with id: "
                                                + documentId
                                )
                        );


        //================================================
        // DELETE PHYSICAL FILE
        //================================================

        try {

            Path filePath =
                    Paths.get(
                            document.getFilePath()
                    ).normalize();

            Files.deleteIfExists(filePath);

        } catch (IOException error) {

            throw new RuntimeException(
                    "Failed to delete document file.",
                    error
            );
        }


        //================================================
        // DELETE DATABASE RECORD
        //================================================

        clientDocumentRepository.delete(
                document
        );
    }
}
