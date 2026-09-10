package com.shulventures.solarservicesbackend.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.shulventures.solarservicesbackend.entity.Quotation;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;

@Service
public class QuotationPdfService {

    private final TemplateEngine templateEngine;
    private final QuotationService quotationService;

    public QuotationPdfService(
            TemplateEngine templateEngine,
            QuotationService quotationService
    ) {
        this.templateEngine = templateEngine;
        this.quotationService = quotationService;
    }

    public byte[] generateQuotationPdf(Long quotationId) {

        // Get quotation from database
        Quotation quotation =
                quotationService.getQuotationById(quotationId);

        // Create Thymeleaf context
        Context context = new Context();

        context.setVariable("quotation", quotation);

        // Convert HTML template into HTML string
        String html = templateEngine.process(
                "quotation/quotation",
                context
        );

        // Convert HTML into PDF
        try (ByteArrayOutputStream outputStream =
                     new ByteArrayOutputStream()) {

            PdfRendererBuilder builder =
                    new PdfRendererBuilder();

            builder.useFastMode();

            builder.withHtmlContent(
                    html,
                    null
            );

            builder.toStream(outputStream);

            builder.run();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate quotation PDF",
                    e
            );
        }
    }
}