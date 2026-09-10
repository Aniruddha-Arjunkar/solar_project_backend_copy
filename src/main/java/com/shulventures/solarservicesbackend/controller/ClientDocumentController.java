package com.shulventures.solarservicesbackend.controller;


import com.shulventures.solarservicesbackend.entity.ClientDocument;
import com.shulventures.solarservicesbackend.service.ClientDocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/client-documents")
@CrossOrigin(origins = "http://localhost:5173")
public class ClientDocumentController {

    private final ClientDocumentService clientDocumentService;

    public ClientDocumentController(
            ClientDocumentService clientDocumentService
    ) {
        this.clientDocumentService =
                clientDocumentService;
    }


    //====================================================
    // UPLOAD DOCUMENT
    //====================================================

    @PostMapping(
            value = "/client/{clientId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ClientDocument> uploadDocument(

            @PathVariable Long clientId,

            @RequestParam("file")
            MultipartFile file

    ) {

        ClientDocument document =
                clientDocumentService.uploadDocument(
                        clientId,
                        file
                );

        return ResponseEntity.ok(document);
    }


    //====================================================
    // GET CLIENT DOCUMENTS
    //====================================================

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ClientDocument>>
    getClientDocuments(

            @PathVariable Long clientId

    ) {

        return ResponseEntity.ok(
                clientDocumentService
                        .getDocumentsByClient(clientId)
        );
    }


    //====================================================
// VIEW / DOWNLOAD DOCUMENT
//====================================================

    @GetMapping("/{documentId}/view")
    public ResponseEntity<Resource> viewDocument(
            @PathVariable Long documentId
    ) {

        ClientDocument document =
                clientDocumentService
                        .getDocument(documentId);

        Resource resource =
                clientDocumentService
                        .getDocumentFile(documentId);


        //================================================
        // DETERMINE MEDIA TYPE
        //================================================

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


        //================================================
        // FIX GENERIC FILE TYPE
        //================================================

        if (MediaType.APPLICATION_OCTET_STREAM.equals(mediaType)) {

            String fileName =
                    document.getFileName().toLowerCase();

            if (fileName.endsWith(".pdf")) {

                mediaType =
                        MediaType.APPLICATION_PDF;

            } else if (fileName.endsWith(".jpg") ||
                    fileName.endsWith(".jpeg")) {

                mediaType =
                        MediaType.IMAGE_JPEG;

            } else if (fileName.endsWith(".png")) {

                mediaType =
                        MediaType.IMAGE_PNG;
            }
        }


        //================================================
        // RETURN FILE
        //================================================

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


    //====================================================
    // DELETE DOCUMENT
    //====================================================

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(

            @PathVariable Long documentId

    ) {

        clientDocumentService.deleteDocument(
                documentId
        );

        return ResponseEntity.noContent().build();
    }
}
