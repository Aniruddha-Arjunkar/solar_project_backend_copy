package com.shulventures.solarservicesbackend.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.shulventures.solarservicesbackend.entity.Invoice;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;

/**
 * ============================================================
 * INVOICE PDF SERVICE
 * ============================================================
 *
 * Responsible for:
 *
 * 1. Getting invoice data
 * 2. Passing invoice data to Thymeleaf
 * 3. Converting HTML into PDF
 *
 * ============================================================
 */

@Service
public class InvoicePdfService {

    private final TemplateEngine templateEngine;
    private final InvoiceService invoiceService;

    public InvoicePdfService(
            TemplateEngine templateEngine,
            InvoiceService invoiceService
    ) {
        this.templateEngine = templateEngine;
        this.invoiceService = invoiceService;
    }

    // ============================================================
    // GENERATE INVOICE PDF
    // ============================================================

    public byte[] generateInvoicePdf(Long invoiceId) {

        // --------------------------------------------------------
        // GET INVOICE FROM DATABASE
        // --------------------------------------------------------

        Invoice invoice =
                invoiceService.getInvoiceEntityById(invoiceId);

        // --------------------------------------------------------
        // CREATE THYMELEAF CONTEXT
        // --------------------------------------------------------

        Context context = new Context();

        context.setVariable(
                "invoice",
                invoice
        );

        // --------------------------------------------------------
        // PROCESS HTML TEMPLATE
        // --------------------------------------------------------

        String html =
                templateEngine.process(
                        "quotation/invoice/invoice",
                        context
                );

        // --------------------------------------------------------
        // CONVERT HTML TO PDF
        // --------------------------------------------------------

        try (
                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()
        ) {

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
                    "Failed to generate invoice PDF",
                    e
            );
        }
    }
}