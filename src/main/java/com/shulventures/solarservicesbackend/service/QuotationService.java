package com.shulventures.solarservicesbackend.service;

import com.shulventures.solarservicesbackend.entity.Lead;
import com.shulventures.solarservicesbackend.entity.Quotation;
import com.shulventures.solarservicesbackend.entity.QuotationItem;
import com.shulventures.solarservicesbackend.repository.LeadRepository;
import com.shulventures.solarservicesbackend.repository.QuotationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final LeadRepository leadRepository;

    public QuotationService(
            QuotationRepository quotationRepository,
            LeadRepository leadRepository
    ) {
        this.quotationRepository = quotationRepository;
        this.leadRepository = leadRepository;
    }


    // ================= Method to Creat Quotation =================
    public Quotation createQuotation(Long leadId, Quotation quotation) {

        // Firstly Find existing lead
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Lead not found with id: " + leadId
                        )
                );

        // Connect quotation with lead
        quotation.setLead(lead);

        // Connect every item with quotation
        if (quotation.getItems() != null) {

            for (QuotationItem item : quotation.getItems()) {

                item.setQuotation(quotation);
            }
        }


        // Update lead quotation status
        lead.setQuotationDate(quotation.getQuotationDate());
        lead.setQuotationStatus("DRAFT");
        lead.setStatus("QUOTATION");

        leadRepository.save(lead);

        // Save quotation + items
        return quotationRepository.save(quotation);
    }

    // ================= GET all Quotations =================
    public List<Quotation> getAllQuotations() {
        return quotationRepository.findAll();
    }


    // ================= GET Quotation by ID =================
    public Quotation getQuotationById(Long id) {

        return quotationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Quotation not found with id: " + id
                        )
                );
    }


    // ================= Get Quotaion by Lead =================
    public List<Quotation> getQuotationsByLead(Long leadId) {

        return quotationRepository.findByLeadId(leadId);
    }
}