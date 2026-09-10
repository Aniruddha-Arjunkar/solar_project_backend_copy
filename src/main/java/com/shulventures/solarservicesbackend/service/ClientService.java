package com.shulventures.solarservicesbackend.service;

import com.shulventures.solarservicesbackend.entity.Client;
import com.shulventures.solarservicesbackend.entity.Lead;
import com.shulventures.solarservicesbackend.repository.ClientRepository;
import com.shulventures.solarservicesbackend.repository.LeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final LeadRepository leadRepository;


    public ClientService(ClientRepository clientRepository,
                         LeadRepository leadRepository) {
        this.clientRepository = clientRepository;
        this.leadRepository = leadRepository;
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

        BigDecimal baseAmount = client.getTotalAmount();

        if (baseAmount == null) {
            baseAmount = BigDecimal.ZERO;
            client.setTotalAmount(baseAmount);
        }

        if (Boolean.TRUE.equals(client.getApplyGst())) {

            // 18% GST
            BigDecimal gstAmount = baseAmount
                    .multiply(new BigDecimal("0.18"));

            client.setGstAmount(gstAmount);

            // Base + GST
            client.setFinalAmount(
                    baseAmount.add(gstAmount)
            );

        } else {

            // Non-GST Client
            client.setGstAmount(BigDecimal.ZERO);

            client.setFinalAmount(baseAmount);
        }

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


        //====================================================
        // GST CALCULATION
        //
        // Example:
        //
        // Total Amount = 250000
        //
        // GST 18%:
        // 250000 × 0.18 = 45000
        //
        // Final Amount:
        // 250000 + 45000 = 295000
        //
        // IMPORTANT:
        // GST amount and final amount are calculated
        // by backend. We do NOT trust frontend values.
        //====================================================

        if (Boolean.TRUE.equals(applyGst)) {

            BigDecimal gstAmount =
                    baseAmount.multiply(
                            new BigDecimal("0.18")
                    );

            BigDecimal finalAmount =
                    baseAmount.add(gstAmount);


            existingClient.setGstAmount(
                    gstAmount
            );

            existingClient.setFinalAmount(
                    finalAmount
            );

        } else {

            //================================================
            // GST NOT APPLIED
            //================================================

            existingClient.setGstAmount(
                    BigDecimal.ZERO
            );

            existingClient.setFinalAmount(
                    baseAmount
            );
        }


        //====================================================
        // ADDRESS INFORMATION
        //====================================================

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

        //================================================
        // GST CALCULATION
        //
        // Example:
        //
        // Total Amount = 100000
        // GST 18%      = 18000
        // Final Amount = 118000
        //================================================
        BigDecimal baseAmount = client.getTotalAmount();

        if (baseAmount == null) {
            baseAmount = BigDecimal.ZERO;
            client.setTotalAmount(baseAmount);
        }
        if (Boolean.TRUE.equals(client.getApplyGst())) {
            BigDecimal gstAmount =
                    baseAmount.multiply(new BigDecimal("0.18"));
            client.setGstAmount(gstAmount);
            client.setFinalAmount(
                    baseAmount.add(gstAmount)
            );
        } else {
            client.setGstAmount(BigDecimal.ZERO);
            client.setFinalAmount(baseAmount);
        }

        // SAVE CLIENT
        Client savedClient = clientRepository.save(client);

        // DELETE ORIGINAL LEAD
        leadRepository.delete(lead);

        // RETURN CREATED CLIENT
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

        if (Boolean.TRUE.equals(client.getApplyGst())) {

            // 18% GST
            BigDecimal gstAmount =
                    baseAmount.multiply(
                            new BigDecimal("0.18")
                    );

            client.setGstAmount(gstAmount);

            // Base Amount + GST
            client.setFinalAmount(
                    baseAmount.add(gstAmount)
            );

        } else {

            // =================================================
            // NON-GST CLIENT
            // =================================================

            client.setGstAmount(
                    BigDecimal.ZERO
            );

            client.setFinalAmount(
                    baseAmount
            );
        }

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
