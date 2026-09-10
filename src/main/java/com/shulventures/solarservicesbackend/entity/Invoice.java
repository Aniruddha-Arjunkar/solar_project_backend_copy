package com.shulventures.solarservicesbackend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String gstInvoiceNo;

    @Column(nullable = false)
    private LocalDate invoiceDate;

    @Column(nullable = false)
    private LocalDate dueDate;


    // =====================================================
    // CLIENT REFERENCE
    // =====================================================

    /*
     * Invoice belongs to an existing Client.
     *
     * Unlike Lead -> Client conversion, the Client will
     * NOT be deleted.
     *
     * Therefore we can safely keep a relationship here.
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;


    /*
     * Original Lead ID.
     *
     * This is only for historical/reference purposes.
     */

    private Long inquiryId;


    // =====================================================
    // CUSTOMER SNAPSHOT
    // =====================================================

    /*
     * We intentionally store customer information inside
     * the invoice as a snapshot.
     *
     * If the Client's address/name changes later,
     * the old invoice should still show the original
     * information.
     */

    private String custName;

    private String custPhone;

    private String custEmail;

    @Column(columnDefinition = "TEXT")
    private String billingAddress;

    @Column(columnDefinition = "TEXT")
    private String shippingAddress;


    // =====================================================
    // INVOICE ITEMS
    // =====================================================

    @OneToMany(
            mappedBy = "invoice",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<InvoiceItem> items = new ArrayList<>();


    // =====================================================
    // TOTALS
    // =====================================================

    private BigDecimal subtotal = BigDecimal.ZERO;

    private BigDecimal totalCgst = BigDecimal.ZERO;

    private BigDecimal totalSgst = BigDecimal.ZERO;

    private BigDecimal grandTotal = BigDecimal.ZERO;


    // =====================================================
    // NOTES
    // =====================================================

    @Column(columnDefinition = "TEXT")
    private String notes;


    // =====================================================
    // PDF
    // =====================================================

    /*
     * Path/name of generated invoice PDF.
     */

    private String pdfFile;


    // =====================================================
    // TIMESTAMPS
    // =====================================================

    private LocalDateTime createdAt;


    // =====================================================
    // LIFECYCLE
    // =====================================================

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();

        if (subtotal == null) {
            subtotal = BigDecimal.ZERO;
        }

        if (totalCgst == null) {
            totalCgst = BigDecimal.ZERO;
        }

        if (totalSgst == null) {
            totalSgst = BigDecimal.ZERO;
        }

        if (grandTotal == null) {
            grandTotal = BigDecimal.ZERO;
        }
    }


    // =====================================================
    // GETTERS & SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getGstInvoiceNo() {
        return gstInvoiceNo;
    }

    public void setGstInvoiceNo(String gstInvoiceNo) {
        this.gstInvoiceNo = gstInvoiceNo;
    }


    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }


    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public Long getInquiryId() {
        return inquiryId;
    }

    public void setInquiryId(Long inquiryId) {
        this.inquiryId = inquiryId;
    }


    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }


    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }


    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }


    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }


    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }


    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }


    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }


    public BigDecimal getTotalCgst() {
        return totalCgst;
    }

    public void setTotalCgst(BigDecimal totalCgst) {
        this.totalCgst = totalCgst;
    }


    public BigDecimal getTotalSgst() {
        return totalSgst;
    }

    public void setTotalSgst(BigDecimal totalSgst) {
        this.totalSgst = totalSgst;
    }


    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public String getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
