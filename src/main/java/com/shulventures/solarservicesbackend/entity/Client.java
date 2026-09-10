package com.shulventures.solarservicesbackend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // ==================== LEAD REFERENCE ====================

    /*
     * ID of the Lead from which this Client was created.
     *
     * This is intentionally NOT a @ManyToOne relationship.
     * The Lead will be deleted after successful conversion.
     */
    private Long inquiryId;


    // ==================== CUSTOMER DETAILS ====================

    private String custName;

    private String custPhone;

    private String custEmail;

    @Column(columnDefinition = "TEXT")
    private String custAddress;


    // ==================== SERVICE DETAILS ====================

    private String service;

    @Column(columnDefinition = "TEXT")
    private String serviceTermCondition;

    private BigDecimal totalAmount;
    private BigDecimal gstAmount = BigDecimal.ZERO;
    private BigDecimal finalAmount;
    private String warranty;

    @Column(columnDefinition = "TEXT")
    private String serviceCovered;

    private LocalDate serviceDate;


    // ==================== GST DETAILS ====================

    /*
     * false = Non-GST Client
     * true  = GST Client
     */
    private Boolean applyGst = false;

    /*
     * inclusive / exclusive
     */
    private String gstType = "exclusive";

    private String gstInvoiceNo;

    @Column(columnDefinition = "TEXT")
    private String billingAddress;

    @Column(columnDefinition = "TEXT")
    private String shippingAddress;


    // ==================== OTHER DETAILS ====================

    /*
     * For now documents can be stored as text.
     * We can improve document handling later.
     */
    @Column(columnDefinition = "TEXT")
    private String documents;

    private String consumerNo;

    private String subdivision;

    private String technicalName;


    // ==================== CLIENT SOURCE ====================

    /*
     * ADMIN or VENDOR
     *
     * For Lead -> Client conversion:
     * addedBy = "ADMIN"
     */
    private String addedBy = "ADMIN";

    /*
     * Vendor ID will be used when Vendor Management
     * is implemented.
     */
    private Long vendorId;


    // ==================== TIMESTAMPS ====================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    // ==================== LIFECYCLE ====================

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (applyGst == null) {
            applyGst = false;
        }

        if (gstType == null || gstType.isBlank()) {
            gstType = "exclusive";
        }

        if (gstAmount == null) {
            gstAmount = BigDecimal.ZERO;
        }

        if (addedBy == null || addedBy.isBlank()) {
            addedBy = "ADMIN";
        }
    }


    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    // ==================== GETTERS & SETTERS ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceTermCondition() {
        return serviceTermCondition;
    }

    public void setServiceTermCondition(String serviceTermCondition) {
        this.serviceTermCondition = serviceTermCondition;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getServiceCovered() {
        return serviceCovered;
    }

    public void setServiceCovered(String serviceCovered) {
        this.serviceCovered = serviceCovered;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Boolean getApplyGst() {
        return applyGst;
    }

    public void setApplyGst(Boolean applyGst) {
        this.applyGst = applyGst;
    }

    public String getGstType() {
        return gstType;
    }

    public void setGstType(String gstType) {
        this.gstType = gstType;
    }

    public BigDecimal getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(BigDecimal gstAmount) {
        this.gstAmount = gstAmount;
    }

    public String getGstInvoiceNo() {
        return gstInvoiceNo;
    }

    public void setGstInvoiceNo(String gstInvoiceNo) {
        this.gstInvoiceNo = gstInvoiceNo;
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

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }

    public String getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(String technicalName) {
        this.technicalName = technicalName;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
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
