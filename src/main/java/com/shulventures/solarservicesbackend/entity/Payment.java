package com.shulventures.solarservicesbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    // ============================================================
    // PRIMARY KEY
    // ============================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // ============================================================
    // CLIENT REFERENCE
    // ============================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "client_id",
            nullable = false
    )
    @JsonIgnore
    private Client client;


    // ============================================================
    // PAYMENT INFORMATION
    // ============================================================

    /*
     * Actual payable amount of the client.
     *
     * This will normally come from:
     *
     * Client.finalAmount
     */
    @Column(
            precision = 15,
            scale = 2
    )
    private BigDecimal totalAmount;


    /*
     * Amount paid in this particular payment transaction.
     */
    @Column(
            precision = 15,
            scale = 2,
            nullable = false
    )
    private BigDecimal paidAmount;


    /*
     * Remaining amount after this payment.
     *
     * Example:
     *
     * Total Amount = 300000
     * Previous Paid = 100000
     * Current Payment = 50000
     *
     * Due Amount = 150000
     */
    @Column(
            precision = 15,
            scale = 2
    )
    private BigDecimal dueAmount;


    // ============================================================
    // DATE INFORMATION
    // ============================================================

    /*
     * Date on which the payment was made.
     */
    private LocalDate paymentDate;


    /*
     * Next date on which payment is expected.
     */
    private LocalDate dueDate;


    // ============================================================
    // PAYMENT GATEWAY
    // ============================================================

    /*
     * Examples:
     *
     * Cash
     * Google Pay
     * PhonePe
     * Bank Transfer
     * Cheque
     * Other
     */
    private String paymentGateway;


    // ============================================================
    // VENDOR REFERENCE
    // ============================================================

    /*
     * If the client belongs to a vendor,
     * this stores the vendor ID.
     *
     * For Admin-created clients this can remain null.
     */
    private Long vendorId;


    // ============================================================
    // TIMESTAMPS
    // ============================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    // ============================================================
    // LIFECYCLE
    // ============================================================

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

    }


    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();

    }


    // ============================================================
    // GETTERS & SETTERS
    // ============================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }


    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }


    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }


    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }


    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }


    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }


    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}