package com.shulventures.solarservicesbackend.service;


import com.shulventures.solarservicesbackend.entity.Lead;
import com.shulventures.solarservicesbackend.repository.LeadRepository;
import com.shulventures.solarservicesbackend.repository.QuotationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LeadService {

    private final LeadRepository leadRepository;

    //inject Quotation Repository to Delete Lead via Quotation
    private final QuotationRepository quotationRepository;

    public LeadService(LeadRepository leadRepository,
                       QuotationRepository quotationRepository) {
        this.leadRepository = leadRepository;
        this.quotationRepository = quotationRepository;
    }


    // ================= CREATE =================
    public Lead createLead(Lead lead) {

        if (lead.getStatus() == null || lead.getStatus().isBlank()) {
            lead.setStatus("NEW");
        }

        return leadRepository.save(lead);
    }



    // ================= GET ALL =================

    public List<Lead> getAllLeads() {
        return leadRepository.findAll();
    }


    // ================= GET BY ID =================

    public Optional<Lead> getLeadById(Long id) {
        return leadRepository.findById(id);
    }

    // ================= GET LEADS BY STATUS =================

    public List<Lead> getLeadsByStatus(String status) {
        return leadRepository.findByStatus(status);
    }

    // ================= UPDATE =================

    public Lead updateLead(Long id, Lead updatedLead) {
        Lead existingLead = leadRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Lead not found with id: " + id)
                );

        existingLead.setName(updatedLead.getName());
        existingLead.setContact(updatedLead.getContact());
        existingLead.setEmail(updatedLead.getEmail());
        existingLead.setAddress(updatedLead.getAddress());
        existingLead.setMessage(updatedLead.getMessage());
        existingLead.setInquiryDate(updatedLead.getInquiryDate());
        existingLead.setStatus(updatedLead.getStatus());

        existingLead.setFollowUpDate(updatedLead.getFollowUpDate());

        existingLead.setVisitDate(updatedLead.getVisitDate());
        existingLead.setVisitTime(updatedLead.getVisitTime());

        existingLead.setServiceDate(updatedLead.getServiceDate());
        existingLead.setServiceTime(updatedLead.getServiceTime());
        existingLead.setServiceType(updatedLead.getServiceType());
        existingLead.setServiceRequirement(
                updatedLead.getServiceRequirement()
        );

        existingLead.setScheduleDate(updatedLead.getScheduleDate());
        existingLead.setScheduleTime(updatedLead.getScheduleTime());
        existingLead.setScheduleType(updatedLead.getScheduleType());

        existingLead.setRemarks(updatedLead.getRemarks());


        return leadRepository.save(existingLead);
    }


    // ================= DELETE LEAD via Quotaion =================

    @Transactional
    public void deleteLead(Long id) {

        // CHECK LEAD EXISTS
        if (!leadRepository.existsById(id)) {
            throw new RuntimeException(
                    "Lead not found with id: " + id
            );
        }

        // First DELETE QUOTATIONS OF THIS LEAD
        quotationRepository.deleteByLeadId(id);


        // Then DELETE LEAD
        leadRepository.deleteById(id);
    }

//=== Action Buttons (Existing Leads is updating Instead of Creating New Lead) ============================================

//================= Add First Action Schedule ===================================
    public Lead scheduleLead(Long id, Lead scheduleData) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Lead not found with id: " + id
                        )
                );
        lead.setScheduleDate(scheduleData.getScheduleDate());
        lead.setScheduleTime(scheduleData.getScheduleTime());
        lead.setScheduleType(scheduleData.getScheduleType());
        lead.setRemarks(scheduleData.getRemarks());
        lead.setStatus("SCHEDULED");

        return leadRepository.save(lead);
    }

    //============== Create FollowUp of Existing Lead =================

    public Lead reFollowUpLead(Long id, Lead followUpData) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Lead not found with id: " + id
                        )
                );
        lead.setFollowUpDate(followUpData.getFollowUpDate());

        lead.setRemarks(followUpData.getRemarks());

        lead.setStatus("FOLLOW_UP");

        return leadRepository.save(lead);
    }

    // ================= CREATE VISIT OF EXISTING LEAD =================

    public Lead visitLead(Long id, Lead visitData) {

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Lead not found with id: " + id)
                );

        lead.setVisitDate(visitData.getVisitDate());
        lead.setVisitTime(visitData.getVisitTime());
        lead.setRemarks(visitData.getRemarks());
        lead.setStatus("VISIT");

        return leadRepository.save(lead);
    }

    // ================= CREATE SERVICE OF EXISTING LEAD =================

    public Lead serviceLead(Long id, Lead serviceData) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Lead not found with id: " + id
                        )
                );

        lead.setServiceDate(serviceData.getServiceDate());
        lead.setServiceTime(serviceData.getServiceTime());
        lead.setServiceType(serviceData.getServiceType());
        lead.setServiceRequirement(serviceData.getServiceRequirement());
        lead.setRemarks(serviceData.getRemarks());
        lead.setStatus("SERVICE");
        return leadRepository.save(lead);
    }

    // ========================== QUOTATION ==============================================

    public Lead quotationLead(Long id, Lead quotationData) {

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Lead not found with id: " + id));

        lead.setQuotationDate(quotationData.getQuotationDate());
        lead.setRemarks(quotationData.getRemarks());
//        lead.setStatus("QUOTATION");

        return leadRepository.save(lead);
    }

    // ================= GET QUOTATION LEADS =================

    public List<Lead> getQuotationLeads() {
        return leadRepository.findByQuotationStatusIsNotNull();
    }

   // public List<Lead> getQuotationLeads() {
   //     return leadRepository.findByStatus("QUOTATION");
   // }

}
