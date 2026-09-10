package com.shulventures.solarservicesbackend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;


    // =====================================================
    // ITEM INFORMATION
    // =====================================================

    @Column(nullable = false)
    private String itemName;

    private String hsn;

    private BigDecimal qty = BigDecimal.ONE;

    private BigDecimal serviceCharge;

    private BigDecimal rate;

    private BigDecimal baseAmount;


    // =====================================================
    // CGST
    // =====================================================

    private BigDecimal cgstPer;

    private BigDecimal cgstAmt;


    // =====================================================
    // SGST
    // =====================================================

    private BigDecimal sgstPer;

    private BigDecimal sgstAmt;


    // =====================================================
    // ITEM TOTAL
    // =====================================================

    private BigDecimal itemTotal;


    // =====================================================
    // GETTERS & SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }


    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }


    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }


    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }


    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }


    public BigDecimal getCgstPer() {
        return cgstPer;
    }

    public void setCgstPer(BigDecimal cgstPer) {
        this.cgstPer = cgstPer;
    }


    public BigDecimal getCgstAmt() {
        return cgstAmt;
    }

    public void setCgstAmt(BigDecimal cgstAmt) {
        this.cgstAmt = cgstAmt;
    }


    public BigDecimal getSgstPer() {
        return sgstPer;
    }

    public void setSgstPer(BigDecimal sgstPer) {
        this.sgstPer = sgstPer;
    }


    public BigDecimal getSgstAmt() {
        return sgstAmt;
    }

    public void setSgstAmt(BigDecimal sgstAmt) {
        this.sgstAmt = sgstAmt;
    }


    public BigDecimal getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(BigDecimal itemTotal) {
        this.itemTotal = itemTotal;
    }
}
