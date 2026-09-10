package com.shulventures.solarservicesbackend.controller;


import com.shulventures.solarservicesbackend.entity.Lead;
import com.shulventures.solarservicesbackend.service.LeadService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@CrossOrigin(origins = "http://localhost:5173")
public class LeadController {

    private final LeadService leadService;




    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }


    // ================= CREATE LEAD =================

    @PostMapping
    public ResponseEntity<Lead> createLead(
            @RequestBody Lead lead
    ) {

        Lead savedLead = leadService.createLead(lead);

        return new ResponseEntity<>(
                savedLead,
                HttpStatus.CREATED
        );
    }


    // ================= GET ALL LEADS =================

    @GetMapping
    public ResponseEntity<List<Lead>> getAllLeads() {

        List<Lead> leads = leadService.getAllLeads();

        return ResponseEntity.ok(leads);
    }

    // ================= GET QUOTATION LEADS ===================

    @GetMapping("/quotations")
    public ResponseEntity<List<Lead>> getQuotationLeads() {
        List<Lead> quotations = leadService.getQuotationLeads();
        return ResponseEntity.ok(quotations);
    }

    // ================= GET LEADS BY STATUS =================

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Lead>> getLeadsByStatus(
            @PathVariable String status
    ) {
        List<Lead> leads =
                leadService.getLeadsByStatus(status);
        return ResponseEntity.ok(leads);
    }

    // ================= GET LEAD BY ID =================

    @GetMapping("/{id}")
    public ResponseEntity<Lead> getLeadById(
            @PathVariable Long id
    ) {

        return leadService.getLeadById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }


    // ================= UPDATE LEAD =================

    @PutMapping("/{id}")
    public ResponseEntity<Lead> updateLead(
            @PathVariable Long id,
            @RequestBody Lead lead
    ) {

        Lead updatedLead =
                leadService.updateLead(id, lead);

        return ResponseEntity.ok(updatedLead);
    }


    // ================= DELETE LEAD =================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLead(
            @PathVariable Long id
    ) {

        leadService.deleteLead(id);

        return ResponseEntity.noContent().build();
    }

    //=============== Action Button Api's =============================

    //======= Add Schedule API For Action Button > Schedule=================
    @PostMapping("/{id}/schedule")
    public ResponseEntity<Lead> scheduleLead(
            @PathVariable Long id,
            @RequestBody Lead lead
    ) {

        Lead scheduledLead =
                leadService.scheduleLead(id, lead);

        return ResponseEntity.ok(scheduledLead);
    }

    // ================= RE-FOLLOWUP API =================

    @PostMapping("/{id}/refollowup")
    public ResponseEntity<Lead> reFollowUpLead(
            @PathVariable Long id,
            @RequestBody Lead followUpData
    ) {

        Lead updatedLead = leadService.reFollowUpLead(id, followUpData);

        return ResponseEntity.ok(updatedLead);
    }

    // ================= VISIT API =================

    @PostMapping("/{id}/visit")
    public ResponseEntity<Lead> visitLead(
            @PathVariable Long id,
            @RequestBody Lead visitData
    ) {
        Lead updatedLead = leadService.visitLead(id, visitData);

        return ResponseEntity.ok(updatedLead);
    }

    // ================= SERVICE API =================

    @PostMapping("/{id}/service")
    public ResponseEntity<Lead> serviceLead(
            @PathVariable Long id,
            @RequestBody Lead serviceData
    ) {

        Lead updatedLead =
                leadService.serviceLead(id, serviceData);

        return ResponseEntity.ok(updatedLead);
    }

    // ==================== QUOTATION API =================

    @PostMapping("/{id}/quotation")
    public ResponseEntity<Lead> quotationLead(
            @PathVariable Long id,
            @RequestBody Lead quotationData
    ) {
        Lead updatedLead = leadService.quotationLead(id, quotationData);

        return ResponseEntity.ok(updatedLead);
    }
}