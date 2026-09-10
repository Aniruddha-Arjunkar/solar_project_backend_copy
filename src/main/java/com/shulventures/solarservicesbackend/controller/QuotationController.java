package com.shulventures.solarservicesbackend.controller;

import com.shulventures.solarservicesbackend.entity.Quotation;
import com.shulventures.solarservicesbackend.service.QuotationPdfService;
import com.shulventures.solarservicesbackend.service.QuotationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import for Pdf
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/quotations")
@CrossOrigin(origins = "http://localhost:5173")
public class QuotationController {

    private final QuotationService quotationService;

    //inject Pdf Related Service Class
    private final QuotationPdfService quotationPdfService;

    public QuotationController(QuotationService quotationService,
                               QuotationPdfService quotationPdfService) {

        this.quotationService = quotationService;

        this.quotationPdfService = quotationPdfService;
    }


    // ================= Create Quotation =================
    @PostMapping("/lead/{leadId}")
    public ResponseEntity<Quotation> createQuotation(
            @PathVariable Long leadId,
            @RequestBody Quotation quotation
    ) {
        Quotation savedQuotation =
                quotationService.createQuotation(
                        leadId,
                        quotation
                );

        return new ResponseEntity<>(
                savedQuotation,
                HttpStatus.CREATED
        );
    }


    // ================= Get all Quotations =================
    @GetMapping
    public ResponseEntity<List<Quotation>> getAllQuotations() {

        return ResponseEntity.ok(
                quotationService.getAllQuotations()
        );
    }


    // ================= Get Quotation by ID =================
    @GetMapping("/{id}")
    public ResponseEntity<Quotation> getQuotationById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                quotationService.getQuotationById(id)
        );
    }


    // ================= GET Quotations by lead =================
    @GetMapping("/lead/{leadId}")
    public ResponseEntity<List<Quotation>> getQuotationsByLead(
            @PathVariable Long leadId
    ) {
        return ResponseEntity.ok(
                quotationService.getQuotationsByLead(leadId)
        );
    }

   // End point to get pdf
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generateQuotationPdf(
            @PathVariable Long id
    ) {

        byte[] pdf =
                quotationPdfService.generateQuotationPdf(id);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=quotation-" + id + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}