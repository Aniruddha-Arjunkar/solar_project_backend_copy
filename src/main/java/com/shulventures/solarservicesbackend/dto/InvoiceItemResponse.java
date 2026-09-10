package com.shulventures.solarservicesbackend.dto;

import java.math.BigDecimal;

public class InvoiceItemResponse {

    private Long id;
    private String itemName;
    private String hsn;
    private BigDecimal qty;
    private BigDecimal serviceCharge;
    private BigDecimal rate;
    private BigDecimal baseAmount;

    private BigDecimal cgstPer;
    private BigDecimal cgstAmt;
    private BigDecimal sgstPer;
    private BigDecimal sgstAmt;


    private BigDecimal itemTotal;


    public InvoiceItemResponse() {
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
