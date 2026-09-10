package com.shulventures.solarservicesbackend.service;

import com.shulventures.solarservicesbackend.entity.Client;
import com.shulventures.solarservicesbackend.entity.Invoice;
import com.shulventures.solarservicesbackend.entity.InvoiceItem;
import com.shulventures.solarservicesbackend.repository.ClientRepository;
import com.shulventures.solarservicesbackend.repository.InvoiceRepository;

//For DTO
import com.shulventures.solarservicesbackend.dto.InvoiceResponse;
import com.shulventures.solarservicesbackend.dto.InvoiceItemResponse;
import java.util.ArrayList;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * ============================================================
 * INVOICE SERVICE
 * ============================================================
 *
 * Responsible for:
 *
 * 1. Creating invoices
 * 2. Creating invoice items
 * 3. Calculating GST
 * 4. Calculating invoice totals
 * 5. Getting invoices
 * 6. Getting invoices for a specific client
 * 7. Getting invoice by ID
 *
 * PDF generation will be connected after the
 * basic invoice creation flow is confirmed.
 *
 * ============================================================
 */

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            ClientRepository clientRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.clientRepository = clientRepository;
    }


    // ============================================================
    // CREATE INVOICE
    // ============================================================

    @Transactional
    public Invoice createInvoice(
            Long clientId,
            Invoice invoice
    ) {

        // --------------------------------------------------------
        // FIND CLIENT
        // --------------------------------------------------------

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Client not found with id: " + clientId
                        )
                );


        // --------------------------------------------------------
        // ONLY GST CLIENT SHOULD GENERATE GST INVOICE
        // --------------------------------------------------------

        if (!Boolean.TRUE.equals(client.getApplyGst())) {

            throw new RuntimeException(
                    "Invoice can only be generated for a GST client."
            );
        }


        // --------------------------------------------------------
        // CHECK INVOICE NUMBER
        // --------------------------------------------------------

        if (invoice.getGstInvoiceNo() == null ||
                invoice.getGstInvoiceNo().isBlank()) {

            throw new RuntimeException(
                    "GST Invoice Number is required."
            );
        }


        // --------------------------------------------------------
        // CHECK DUPLICATE INVOICE NUMBER
        // --------------------------------------------------------

        if (invoiceRepository.existsByGstInvoiceNo(
                invoice.getGstInvoiceNo()
        )) {

            throw new RuntimeException(
                    "Invoice number already exists: "
                            + invoice.getGstInvoiceNo()
            );
        }


        // --------------------------------------------------------
        // REQUIRED DATES
        // --------------------------------------------------------

        if (invoice.getInvoiceDate() == null) {

            invoice.setInvoiceDate(LocalDate.now());
        }

        if (invoice.getDueDate() == null) {

            throw new RuntimeException(
                    "Due date is required."
            );
        }


        // ========================================================
        // CLIENT REFERENCE
        // ========================================================

        invoice.setClient(client);


        // ========================================================
        // CLIENT SNAPSHOT
        // ========================================================
        //
        // We copy client information into the invoice.
        //
        // This is important because the client may be edited
        // later, but the old invoice should remain unchanged.
        //
        // ========================================================

        invoice.setCustName(client.getCustName());

        invoice.setCustPhone(client.getCustPhone());

        invoice.setCustEmail(client.getCustEmail());

        invoice.setBillingAddress(
                client.getBillingAddress()
        );

        invoice.setShippingAddress(
                client.getShippingAddress()
        );

        invoice.setInquiryId(
                client.getInquiryId()
        );


        // ========================================================
        // ITEMS
        // ========================================================

        if (invoice.getItems() == null ||
                invoice.getItems().isEmpty()) {

            throw new RuntimeException(
                    "At least one invoice item is required."
            );
        }


        // ========================================================
        // CALCULATE TOTALS
        // ========================================================

        BigDecimal subtotal = BigDecimal.ZERO;

        BigDecimal totalCgst = BigDecimal.ZERO;

        BigDecimal totalSgst = BigDecimal.ZERO;


        // --------------------------------------------------------
        // PROCESS EVERY ITEM
        // --------------------------------------------------------

        for (InvoiceItem item : invoice.getItems()) {

            // Connect item to invoice
            item.setInvoice(invoice);


            // ----------------------------------------------------
            // DEFAULT QUANTITY
            // ----------------------------------------------------

            if (item.getQty() == null) {

                item.setQty(BigDecimal.ONE);
            }


            // ----------------------------------------------------
            // VALIDATE ITEM NAME
            // ----------------------------------------------------

            if (item.getItemName() == null ||
                    item.getItemName().isBlank()) {

                throw new RuntimeException(
                        "Item name is required."
                );
            }


            // ----------------------------------------------------
            // RATE
            // ----------------------------------------------------

            BigDecimal rate = item.getRate();

            if (rate == null) {

                rate = BigDecimal.ZERO;

                item.setRate(rate);
            }


            // ----------------------------------------------------
            // QUANTITY
            // ----------------------------------------------------

            BigDecimal quantity = item.getQty();


            // ----------------------------------------------------
            // BASE AMOUNT
            // ----------------------------------------------------

            BigDecimal baseAmount =
                    rate.multiply(quantity);


            item.setBaseAmount(
                    baseAmount.setScale(
                            2,
                            RoundingMode.HALF_UP
                    )
            );


            // ----------------------------------------------------
            // GST %
            // ----------------------------------------------------

            BigDecimal cgstPer = item.getCgstPer();

            BigDecimal sgstPer = item.getSgstPer();


            if (cgstPer == null) {

                cgstPer = BigDecimal.ZERO;

                item.setCgstPer(cgstPer);
            }

            if (sgstPer == null) {

                sgstPer = BigDecimal.ZERO;

                item.setSgstPer(sgstPer);
            }


            // ----------------------------------------------------
            // CGST AMOUNT
            // ----------------------------------------------------

            BigDecimal cgstAmount =
                    baseAmount
                            .multiply(cgstPer)
                            .divide(
                                    new BigDecimal("100"),
                                    2,
                                    RoundingMode.HALF_UP
                            );


            // ----------------------------------------------------
            // SGST AMOUNT
            // ----------------------------------------------------

            BigDecimal sgstAmount =
                    baseAmount
                            .multiply(sgstPer)
                            .divide(
                                    new BigDecimal("100"),
                                    2,
                                    RoundingMode.HALF_UP
                            );


            item.setCgstAmt(cgstAmount);

            item.setSgstAmt(sgstAmount);


            // ----------------------------------------------------
            // ITEM TOTAL
            // ----------------------------------------------------

            BigDecimal itemTotal =
                    baseAmount
                            .add(cgstAmount)
                            .add(sgstAmount);


            item.setItemTotal(
                    itemTotal.setScale(
                            2,
                            RoundingMode.HALF_UP
                    )
            );


            // ----------------------------------------------------
            // ADD TO INVOICE TOTALS
            // ----------------------------------------------------

            subtotal = subtotal.add(baseAmount);

            totalCgst = totalCgst.add(cgstAmount);

            totalSgst = totalSgst.add(sgstAmount);
        }


        // ========================================================
        // GRAND TOTAL
        // ========================================================

        BigDecimal grandTotal =
                subtotal
                        .add(totalCgst)
                        .add(totalSgst);


        // ========================================================
        // SET INVOICE TOTALS
        // ========================================================

        invoice.setSubtotal(
                subtotal.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        invoice.setTotalCgst(
                totalCgst.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        invoice.setTotalSgst(
                totalSgst.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        invoice.setGrandTotal(
                grandTotal.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );


        // ========================================================
        // SAVE INVOICE
        // ========================================================

        return invoiceRepository.save(invoice);
    }


    // ============================================================
    // GET ALL INVOICES
    // ============================================================
    public List<InvoiceResponse> getAllInvoices() {

        return invoiceRepository.findAll()
                .stream()
                .map(this::toInvoiceResponse)
                .toList();
    }
//    public List<Invoice> getAllInvoices() {
//
//        return invoiceRepository.findAll();
//    }




    // ============================================================
    // GET INVOICES BY CLIENT
    // ============================================================
    public List<InvoiceResponse> getInvoicesByClient(
            Long clientId
    ) {

        return invoiceRepository.findByClientId(clientId)
                .stream()
                .map(this::toInvoiceResponse)
                .toList();
    }


    // ============================================================
    // GET INVOICE BY ID
    // ============================================================

    public InvoiceResponse getInvoiceById(Long id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invoice not found with id: " + id
                        )
                );

        return toInvoiceResponse(invoice);
    }
//    public Invoice getInvoiceById(Long id) {
//
//        return invoiceRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException(
//                                "Invoice not found with id: " + id
//                        )
//                );
//    }


    // ============================================================
    // GET INVOICE BY NUMBER
    // ============================================================

    public InvoiceResponse getInvoiceByNumber(
            String gstInvoiceNo
    ) {

        Invoice invoice = invoiceRepository
                .findByGstInvoiceNo(gstInvoiceNo)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invoice not found with number: "
                                        + gstInvoiceNo
                        )
                );

        return toInvoiceResponse(invoice);
    }


    // ============================================================
// CONVERT INVOICE ENTITY TO RESPONSE DTO
// ============================================================

    public InvoiceResponse toInvoiceResponse(Invoice invoice) {

        InvoiceResponse response = new InvoiceResponse();

        // --------------------------------------------------------
        // INVOICE INFORMATION
        // --------------------------------------------------------

        response.setId(invoice.getId());
        response.setGstInvoiceNo(invoice.getGstInvoiceNo());
        response.setInvoiceDate(invoice.getInvoiceDate());
        response.setDueDate(invoice.getDueDate());

        // --------------------------------------------------------
        // CUSTOMER SNAPSHOT
        // --------------------------------------------------------

        response.setInquiryId(invoice.getInquiryId());
        response.setCustName(invoice.getCustName());
        response.setCustPhone(invoice.getCustPhone());
        response.setCustEmail(invoice.getCustEmail());
        response.setBillingAddress(invoice.getBillingAddress());
        response.setShippingAddress(invoice.getShippingAddress());

        // --------------------------------------------------------
        // TOTALS
        // --------------------------------------------------------

        response.setSubtotal(invoice.getSubtotal());
        response.setTotalCgst(invoice.getTotalCgst());
        response.setTotalSgst(invoice.getTotalSgst());
        response.setGrandTotal(invoice.getGrandTotal());

        // --------------------------------------------------------
        // OTHER INFORMATION
        // --------------------------------------------------------

        response.setNotes(invoice.getNotes());
        response.setPdfFile(invoice.getPdfFile());
        response.setCreatedAt(invoice.getCreatedAt());

        // --------------------------------------------------------
        // ITEMS
        // --------------------------------------------------------

        List<InvoiceItemResponse> itemResponses = new ArrayList<>();

        if (invoice.getItems() != null) {

            for (InvoiceItem item : invoice.getItems()) {

                InvoiceItemResponse itemResponse =
                        new InvoiceItemResponse();
                itemResponse.setId(item.getId());
                itemResponse.setItemName(item.getItemName());
                itemResponse.setHsn(item.getHsn());
                itemResponse.setQty(item.getQty());
                itemResponse.setServiceCharge(item.getServiceCharge());
                itemResponse.setRate(item.getRate());
                itemResponse.setBaseAmount(item.getBaseAmount());
                itemResponse.setCgstPer(item.getCgstPer());
                itemResponse.setCgstAmt(item.getCgstAmt());
                itemResponse.setSgstPer(item.getSgstPer());
                itemResponse.setSgstAmt(item.getSgstAmt());
                itemResponse.setItemTotal(item.getItemTotal());
                itemResponses.add(itemResponse);
            }
        }
        response.setItems(itemResponses);
        return response;
    }


    // GET INVOICE ENTITY BY ID
    public Invoice getInvoiceEntityById(Long id) {

        return invoiceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invoice not found with id: " + id
                        )
                );
    }
}