package com.shulventures.solarservicesbackend.service;

import com.shulventures.solarservicesbackend.service.VendorService;
import com.shulventures.solarservicesbackend.entity.Client;
import com.shulventures.solarservicesbackend.entity.Lead;
import com.shulventures.solarservicesbackend.repository.ClientRepository;
import com.shulventures.solarservicesbackend.repository.LeadRepository;
import com.shulventures.solarservicesbackend.repository.QuotationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final LeadRepository leadRepository;
    private final QuotationRepository quotationRepository;
    private final VendorService vendorService;


    public ClientService(ClientRepository clientRepository,
                         LeadRepository leadRepository,
                         QuotationRepository quotationRepository,
                         VendorService vendorService) {
        this.clientRepository = clientRepository;
        this.leadRepository = leadRepository;
        this.quotationRepository = quotationRepository;
        this.vendorService = vendorService;
    }

    // =======  GST CALCULATION ===============================

    private void calculateGstAmounts(Client client) {

        // --------------------------------------------------------
        // DEFAULT GST VALUES
        // --------------------------------------------------------

        if (client.getApplyGst() == null) {
            client.setApplyGst(false);
        }

        if (client.getGstType() == null ||
                client.getGstType().isBlank()) {

            client.setGstType("exclusive");
        }


        // --------------------------------------------------------
        // TOTAL AMOUNT
        // --------------------------------------------------------

        BigDecimal totalAmount =
                client.getTotalAmount();

        if (totalAmount == null) {

            totalAmount = BigDecimal.ZERO;

            client.setTotalAmount(totalAmount);
        }


        // --------------------------------------------------------
        // NON-GST CLIENT
        // --------------------------------------------------------

        if (!Boolean.TRUE.equals(client.getApplyGst())) {

            client.setGstAmount(
                    BigDecimal.ZERO
            );

            client.setFinalAmount(
                    totalAmount
            );

            return;
        }


        // --------------------------------------------------------
        // 18% GST
        // --------------------------------------------------------

        BigDecimal gstRate =
                new BigDecimal("0.18");


        // ========================================================
        // EXCLUSIVE GST
        // Total Amount does NOT contain GST
        // ========================================================

        if ("exclusive".equalsIgnoreCase(
                client.getGstType()
        )) {

            BigDecimal gstAmount =
                    totalAmount.multiply(gstRate);

            client.setGstAmount(
                    gstAmount.setScale(
                            2,
                            RoundingMode.HALF_UP
                    )
            );

            client.setFinalAmount(
                    totalAmount
                            .add(gstAmount)
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            )
            );

            return;
        }


        // ========================================================
        // INCLUSIVE GST
        // Total Amount already contains GST
        // ========================================================

        if ("inclusive".equalsIgnoreCase(
                client.getGstType()
        )) {

            BigDecimal gstAmount =
                    totalAmount
                            .multiply(gstRate)
                            .divide(
                                    new BigDecimal("1.18"),
                                    2,
                                    RoundingMode.HALF_UP
                            );

            client.setGstAmount(
                    gstAmount
            );

            client.setFinalAmount(
                    totalAmount.setScale(
                            2,
                            RoundingMode.HALF_UP
                    )
            );

            return;
        }


        // ========================================================
        // UNKNOWN GST TYPE
        // Keep current behavior as EXCLUSIVE
        // ========================================================

        BigDecimal gstAmount =
                totalAmount.multiply(gstRate);

        client.setGstAmount(
                gstAmount.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        client.setFinalAmount(
                totalAmount
                        .add(gstAmount)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        )
        );
    }

    // ==================== CREATE CLIENT ====================

    public Client createClient(Client client) {

        if (client.getApplyGst() == null) {
            client.setApplyGst(false);
        }

        if (client.getAddedBy() == null || client.getAddedBy().isBlank()) {
            client.setAddedBy("ADMIN");
        }

        // ==================== GST CALCULATION ====================
        calculateGstAmounts(client);

        return clientRepository.save(client);
    }


    // ==================== GET ALL CLIENTS ====================

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }


    // ==================== GET CLIENT BY ID ====================

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }


    // ==================== GET NON-GST CLIENTS ====================

    public List<Client> getNonGstClients() {
        return clientRepository.findByApplyGst(false);
    }


    // ==================== GET GST CLIENTS ====================

    public List<Client> getGstClients() {
        return clientRepository.findByApplyGst(true);
    }


    // ==================== GET ADMIN CLIENTS ====================

    public List<Client> getAdminClients() {
        return clientRepository.findByAddedBy("ADMIN");
    }


    // ==================== GET VENDOR CLIENTS ====================

    public List<Client> getVendorClients() {
        return clientRepository.findByAddedBy("VENDOR");
    }



    // ==================== UPDATE CLIENT ====================

    public Client updateClient(Long id, Client updatedClient) {

        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Client not found with id: " + id
                        )
                );



        // CUSTOMER INFORMATION

        existingClient.setCustName(
                updatedClient.getCustName()
        );

        existingClient.setCustPhone(
                updatedClient.getCustPhone()
        );

        existingClient.setCustEmail(
                updatedClient.getCustEmail()
        );

        existingClient.setCustAddress(
                updatedClient.getCustAddress()
        );


        //====================================================
        // SERVICE INFORMATION
        //====================================================

        existingClient.setService(
                updatedClient.getService()
        );

        existingClient.setServiceTermCondition(
                updatedClient.getServiceTermCondition()
        );

        existingClient.setWarranty(
                updatedClient.getWarranty()
        );

        existingClient.setServiceCovered(
                updatedClient.getServiceCovered()
        );

        existingClient.setServiceDate(
                updatedClient.getServiceDate()
        );


        //====================================================
        // PAYMENT INFORMATION
        //====================================================

        BigDecimal baseAmount =
                updatedClient.getTotalAmount();


        // If amount is not provided, use 0
        if (baseAmount == null) {

            baseAmount = BigDecimal.ZERO;
        }


        existingClient.setTotalAmount(
                baseAmount
        );


        //====================================================
        // GST INFORMATION
        //====================================================

        Boolean applyGst =
                updatedClient.getApplyGst();


        // Prevent null GST value
        if (applyGst == null) {
            applyGst = false;
        }


        existingClient.setApplyGst(
                applyGst
        );

        existingClient.setGstType(
                updatedClient.getGstType()
        );

        existingClient.setGstInvoiceNo(
                updatedClient.getGstInvoiceNo()
        );


        // GST CALCULATION
        calculateGstAmounts(existingClient);


        // ADDRESS INFORMATION
        existingClient.setBillingAddress(
                updatedClient.getBillingAddress()
        );

        existingClient.setShippingAddress(
                updatedClient.getShippingAddress()
        );


        //====================================================
        // ADDITIONAL INFORMATION
        //====================================================

        existingClient.setDocuments(
                updatedClient.getDocuments()
        );

        existingClient.setConsumerNo(
                updatedClient.getConsumerNo()
        );

        existingClient.setSubdivision(
                updatedClient.getSubdivision()
        );

        existingClient.setTechnicalName(
                updatedClient.getTechnicalName()
        );


        //====================================================
        // SOURCE INFORMATION
        //====================================================

//        existingClient.setAddedBy(
//                updatedClient.getAddedBy()
//        );
//
//        existingClient.setVendorId(
//                updatedClient.getVendorId()
//        );


        // SAVE UPDATED CLIENT
        return clientRepository.save(
                existingClient
        );
    }

//    public Client updateClient(Long id, Client updatedClient) {
//
//        Client existingClient = clientRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException(
//                                "Client not found with id: " + id
//                        )
//                );
//
//        existingClient.setCustName(updatedClient.getCustName());
//        existingClient.setCustPhone(updatedClient.getCustPhone());
//        existingClient.setCustEmail(updatedClient.getCustEmail());
//        existingClient.setCustAddress(updatedClient.getCustAddress());
//
//        existingClient.setService(updatedClient.getService());
//        existingClient.setServiceTermCondition(
//                updatedClient.getServiceTermCondition()
//        );
//        existingClient.setTotalAmount(updatedClient.getTotalAmount());
//        existingClient.setWarranty(updatedClient.getWarranty());
//        existingClient.setServiceCovered(
//                updatedClient.getServiceCovered()
//        );
//        existingClient.setServiceDate(updatedClient.getServiceDate());
//
//        existingClient.setApplyGst(updatedClient.getApplyGst());
//        existingClient.setGstType(updatedClient.getGstType());
//        existingClient.setGstAmount(updatedClient.getGstAmount());
//        existingClient.setGstInvoiceNo(updatedClient.getGstInvoiceNo());
//
//        existingClient.setBillingAddress(
//                updatedClient.getBillingAddress()
//        );
//
//        existingClient.setShippingAddress(
//                updatedClient.getShippingAddress()
//        );
//
//        existingClient.setDocuments(updatedClient.getDocuments());
//
//        existingClient.setConsumerNo(updatedClient.getConsumerNo());
//        existingClient.setSubdivision(updatedClient.getSubdivision());
//        existingClient.setTechnicalName(updatedClient.getTechnicalName());
//
//        existingClient.setAddedBy(updatedClient.getAddedBy());
//        existingClient.setVendorId(updatedClient.getVendorId());
//
//        return clientRepository.save(existingClient);
//    }


    // ==================== DELETE CLIENT ====================

    public void deleteClient(Long id) {

        if (!clientRepository.existsById(id)) {
            throw new RuntimeException(
                    "Client not found with id: " + id
            );
        }
        clientRepository.deleteById(id);
    }


    // CONVERT SCHEDULED LEAD INTO CLIENT

    @Transactional
    public Client convertLeadToClient(Long leadId, Client clientData) {

        // Find the lead
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() ->
                        new RuntimeException("Lead not found with id: " + leadId)
                );

        // VALIDATE LEAD STATUS

        if (!"SCHEDULED".equalsIgnoreCase(lead.getStatus())) {
            throw new RuntimeException(
                    "Only scheduled leads can be converted into clients."
            );
        }

        // COPY LEAD INFORMATION INTO CLIENT

        Client client = new Client();

        // Keep original Lead ID for reference
        // This is NOT a database foreign key.
        client.setInquiryId(lead.getId());

        // Basic customer information
        client.setCustName(lead.getName());
        client.setCustPhone(lead.getContact());
        client.setCustEmail(lead.getEmail());
        client.setCustAddress(lead.getAddress());

        // SERVICE INFORMATION
        client.setService(lead.getServiceType());
        client.setServiceDate(lead.getScheduleDate());

        // If service requirement exists, use it
        client.setServiceCovered(lead.getServiceRequirement());

        // COPY ADDITIONAL DATA FROM MAKE CLIENT FORM
        if (clientData.getService() != null &&
                !clientData.getService().isBlank()) {
            client.setService(clientData.getService());
        }
        if (clientData.getServiceTermCondition() != null) {
            client.setServiceTermCondition(
                    clientData.getServiceTermCondition()
            );
        }
        if (clientData.getWarranty() != null) {
            client.setWarranty(clientData.getWarranty());
        }
        if (clientData.getServiceCovered() != null) {
            client.setServiceCovered(
                    clientData.getServiceCovered()
            );
        }
        if (clientData.getServiceDate() != null) {
            client.setServiceDate(clientData.getServiceDate());
        }
        // AMOUNT
        client.setTotalAmount(clientData.getTotalAmount());
        // GST INFORMATION
        client.setApplyGst(clientData.getApplyGst());
        client.setGstType(clientData.getGstType());
        client.setGstInvoiceNo(clientData.getGstInvoiceNo());
        client.setBillingAddress(clientData.getBillingAddress());
        client.setShippingAddress(clientData.getShippingAddress());
        // OTHER CLIENT INFORMATION
        client.setDocuments(clientData.getDocuments());
        client.setConsumerNo(clientData.getConsumerNo());
        client.setSubdivision(clientData.getSubdivision());
        client.setTechnicalName(clientData.getTechnicalName());
        // THIS CLIENT WAS CREATED BY ADMIN

        client.setAddedBy("ADMIN");


        // GST CALCULATION
        calculateGstAmounts(client);

        // SAVE CLIENT
        Client savedClient = clientRepository.save(client);

        // DELETE QUOTATIONS FIRST
        quotationRepository.deleteByLeadId(leadId);

        // DELETE ORIGINAL LEAD
        leadRepository.delete(lead);

        // RETURN CREATED CLIENT
        return savedClient;
    }

    // ==================== CONVERT SCHEDULED LEAD INTO CLIENT VIA VENDOR ====================

    @Transactional
    public Client convertLeadToVendorClient(
            Long leadId,
            Long vendorId,
            Client clientData
    ) {


        // FIND LEAD
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Lead not found with id: " + leadId
                        )
                );



        // VALIDATE LEAD STATUS
        if (!"SCHEDULED".equalsIgnoreCase(lead.getStatus())) {

            throw new RuntimeException(
                    "Only scheduled leads can be converted into clients."
            );
        }


        // VALIDATE VENDOR
        vendorService.getVendorById(vendorId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vendor not found with id: " + vendorId
                        )
                );

        // CREATE CLIENT
        Client client = new Client();


        // LEAD REFERENCE
        client.setInquiryId(lead.getId());


        // CUSTOMER INFORMATION FROM LEAD
        client.setCustName(lead.getName());
        client.setCustPhone(lead.getContact());
        client.setCustEmail(lead.getEmail());
        client.setCustAddress(lead.getAddress());


        // SERVICE INFORMATION
        client.setService(lead.getServiceType());
        client.setServiceDate(lead.getScheduleDate());
        client.setServiceCovered(
                lead.getServiceRequirement()
        );


        // ADDITIONAL DATA FROM MAKE CLIENT FORM
        if (clientData.getService() != null &&
                !clientData.getService().isBlank()) {

            client.setService(
                    clientData.getService()
            );
        }

        if (clientData.getServiceTermCondition() != null) {

            client.setServiceTermCondition(
                    clientData.getServiceTermCondition()
            );
        }

        if (clientData.getWarranty() != null) {

            client.setWarranty(
                    clientData.getWarranty()
            );
        }

        if (clientData.getServiceCovered() != null) {

            client.setServiceCovered(
                    clientData.getServiceCovered()
            );
        }

        if (clientData.getServiceDate() != null) {

            client.setServiceDate(
                    clientData.getServiceDate()
            );
        }


        // AMOUNT
        client.setTotalAmount(
                clientData.getTotalAmount()
        );


        // GST INFORMATION
        client.setApplyGst(
                clientData.getApplyGst()
        );

        client.setGstType(
                clientData.getGstType()
        );

        client.setGstInvoiceNo(
                clientData.getGstInvoiceNo()
        );


        // ADDRESS INFORMATION
        client.setBillingAddress(
                clientData.getBillingAddress()
        );

        client.setShippingAddress(
                clientData.getShippingAddress()
        );


        // OTHER CLIENT INFORMATION
        client.setDocuments(
                clientData.getDocuments()
        );

        client.setConsumerNo(
                clientData.getConsumerNo()
        );

        client.setSubdivision(
                clientData.getSubdivision()
        );

        client.setTechnicalName(
                clientData.getTechnicalName()
        );



        // SOURCE INFORMATION
        client.setAddedBy("VENDOR");
        client.setVendorId(vendorId);



        // GST CALCULATION
        calculateGstAmounts(client);


        // SAVE CLIENT
        Client savedClient = clientRepository.save(client);


        // DELETE QUOTATIONS FIRST
        quotationRepository.deleteByLeadId(leadId);


        // DELETE ORIGINAL LEAD
        leadRepository.delete(lead);

        // RETURN CLIENT
        return savedClient;
    }

    // ==================== CREATE CLIENT FROM VENDOR ====================

    public Client createVendorClient(
            Long vendorId,
            Client client
    ) {

        // ====================================================
        // VENDOR INFORMATION
        // ====================================================

        client.setVendorId(vendorId);

        // This client was created by a vendor
        client.setAddedBy("VENDOR");

        // Vendor-created clients do not come from a lead
        client.setInquiryId(null);


        // ====================================================
        // GST DEFAULT
        // ====================================================

        if (client.getApplyGst() == null) {

            client.setApplyGst(false);
        }


        // ====================================================
        // TOTAL AMOUNT
        // ====================================================

        BigDecimal baseAmount =
                client.getTotalAmount();

        if (baseAmount == null) {
            baseAmount = BigDecimal.ZERO;
            client.setTotalAmount(baseAmount);
        }


        // ====================================================
        // GST CALCULATION
        // ====================================================

        calculateGstAmounts(client);

        // SAVE CLIENT
        return clientRepository.save(client);
    }

    // ============================================================
// GET CLIENTS BY VENDOR ID
// ============================================================

    public List<Client> getClientsByVendorId(Long vendorId) {

        return clientRepository.findByVendorId(vendorId);

    }
}
