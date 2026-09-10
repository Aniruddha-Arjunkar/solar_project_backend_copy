//Quotation form have Multiple Info Sections

package com.shulventures.solarservicesbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quotations")
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= QUOTATION INFO =================

    private String quotationNo;

    private LocalDate quotationDate;


    // ================= CLIENT DETAILS =================

    private String clientName;

    private String clientPhone;

    @Column(columnDefinition = "TEXT")
    private String clientAddress;

    private String phaseType;

    @Column(columnDefinition = "TEXT")
    private String subject;


    // ================= SUMMARY OF PROPOSAL =================

    private BigDecimal pvPlantSize;

    private Boolean gstIncluded;

    private String systemType;

    private BigDecimal powerGenerationMonth;

    private BigDecimal powerGenerationYear;

    private BigDecimal totalSystemCost;

    private BigDecimal minAnnualSaving;

    private BigDecimal investmentRecoveryYears;


    // ================= COMMERCIAL =================

    private BigDecimal supplyInstallation;

    private BigDecimal commercialTotal;

    private BigDecimal actualProjectCost;

    private BigDecimal gstAmount;

    private BigDecimal govtSubsidy;


    // ================= STATUS =================

    private String status;


    // ================= LEAD RELATION =================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    private Lead lead;


    // ================= QUOTATION ITEMS =================

    @OneToMany(
            mappedBy = "quotation",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<QuotationItem> items = new ArrayList<>();


    // ================= TIMESTAMPS =================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    // ================= PRE PERSIST =================

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null || status.isBlank()) {
            status = "DRAFT";
        }
    }


    // ================= PRE UPDATE =================

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }


    // ================= GETTERS AND SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getQuotationNo() {
        return quotationNo;
    }

    public void setQuotationNo(String quotationNo) {
        this.quotationNo = quotationNo;
    }


    public LocalDate getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(LocalDate quotationDate) {
        this.quotationDate = quotationDate;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }


    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }


    public String getPhaseType() {
        return phaseType;
    }

    public void setPhaseType(String phaseType) {
        this.phaseType = phaseType;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public BigDecimal getPvPlantSize() {
        return pvPlantSize;
    }

    public void setPvPlantSize(BigDecimal pvPlantSize) {
        this.pvPlantSize = pvPlantSize;
    }


    public Boolean getGstIncluded() {
        return gstIncluded;
    }

    public void setGstIncluded(Boolean gstIncluded) {
        this.gstIncluded = gstIncluded;
    }


    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }


    public BigDecimal getPowerGenerationMonth() {
        return powerGenerationMonth;
    }

    public void setPowerGenerationMonth(BigDecimal powerGenerationMonth) {
        this.powerGenerationMonth = powerGenerationMonth;
    }


    public BigDecimal getPowerGenerationYear() {
        return powerGenerationYear;
    }

    public void setPowerGenerationYear(BigDecimal powerGenerationYear) {
        this.powerGenerationYear = powerGenerationYear;
    }


    public BigDecimal getTotalSystemCost() {
        return totalSystemCost;
    }

    public void setTotalSystemCost(BigDecimal totalSystemCost) {
        this.totalSystemCost = totalSystemCost;
    }


    public BigDecimal getMinAnnualSaving() {
        return minAnnualSaving;
    }

    public void setMinAnnualSaving(BigDecimal minAnnualSaving) {
        this.minAnnualSaving = minAnnualSaving;
    }


    public BigDecimal getInvestmentRecoveryYears() {
        return investmentRecoveryYears;
    }

    public void setInvestmentRecoveryYears(BigDecimal investmentRecoveryYears) {
        this.investmentRecoveryYears = investmentRecoveryYears;
    }


    public BigDecimal getSupplyInstallation() {
        return supplyInstallation;
    }

    public void setSupplyInstallation(BigDecimal supplyInstallation) {
        this.supplyInstallation = supplyInstallation;
    }


    public BigDecimal getCommercialTotal() {
        return commercialTotal;
    }

    public void setCommercialTotal(BigDecimal commercialTotal) {
        this.commercialTotal = commercialTotal;
    }


    public BigDecimal getActualProjectCost() {
        return actualProjectCost;
    }

    public void setActualProjectCost(BigDecimal actualProjectCost) {
        this.actualProjectCost = actualProjectCost;
    }


    public BigDecimal getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(BigDecimal gstAmount) {
        this.gstAmount = gstAmount;
    }


    public BigDecimal getGovtSubsidy() {
        return govtSubsidy;
    }

    public void setGovtSubsidy(BigDecimal govtSubsidy) {
        this.govtSubsidy = govtSubsidy;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }


    public List<QuotationItem> getItems() {
        return items;
    }

    public void setItems(List<QuotationItem> items) {
        this.items = items;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}