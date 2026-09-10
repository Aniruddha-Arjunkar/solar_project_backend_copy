package com.shulventures.solarservicesbackend.controller;

import com.shulventures.solarservicesbackend.entity.Client;
import com.shulventures.solarservicesbackend.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:5173")
public class ClientController {

    private final ClientService clientService;


    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    // ==================== CREATE CLIENT ====================

    @PostMapping
    public ResponseEntity<Client> createClient(
            @RequestBody Client client
    ) {

        Client savedClient = clientService.createClient(client);

        return new ResponseEntity<>(
                savedClient,
                HttpStatus.CREATED
        );
    }


    // ==================== GET ALL CLIENTS ====================

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {

        return ResponseEntity.ok(
                clientService.getAllClients()
        );
    }


    // ==================== GET NON-GST CLIENTS ====================

    @GetMapping("/non-gst")
    public ResponseEntity<List<Client>> getNonGstClients() {

        return ResponseEntity.ok(
                clientService.getNonGstClients()
        );
    }


    // ==================== GET GST CLIENTS ====================

    @GetMapping("/gst")
    public ResponseEntity<List<Client>> getGstClients() {

        return ResponseEntity.ok(
                clientService.getGstClients()
        );
    }


    // ==================== GET ADMIN CLIENTS ====================

    @GetMapping("/admin")
    public ResponseEntity<List<Client>> getAdminClients() {

        return ResponseEntity.ok(
                clientService.getAdminClients()
        );
    }


    // ==================== GET VENDOR CLIENTS ====================

    @GetMapping("/vendor")
    public ResponseEntity<List<Client>> getVendorClients() {

        return ResponseEntity.ok(
                clientService.getVendorClients()
        );
    }

    // ============================================================
    // GET CLIENTS BY VENDOR
     // ============================================================
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<Client>> getClientsByVendorId(
            @PathVariable Long vendorId
    ) {
        return ResponseEntity.ok(
                clientService.getClientsByVendorId(vendorId)
        );
    }

    // ==================== GET CLIENT BY ID ====================

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(
            @PathVariable Long id
    ) {

        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }


    // ==================== UPDATE CLIENT ====================

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(
            @PathVariable Long id,
            @RequestBody Client client
    ) {

        return ResponseEntity.ok(
                clientService.updateClient(id, client)
        );
    }


    // ==================== DELETE CLIENT ====================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @PathVariable Long id
    ) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }


   //========= Convert Lead into Client ============

    @PostMapping("/convert-from-lead/{leadId}")
    public ResponseEntity<Client> convertLeadToClient(
            @PathVariable Long leadId,
            @RequestBody Client clientData
    ) {
        Client client =
                clientService.convertLeadToClient(
                        leadId,
                        clientData
                );
        return ResponseEntity.ok(client);
    }

    // ==================== CREATE CLIENT FROM VENDOR ====================
    @PostMapping("/vendor/{vendorId}")
    public ResponseEntity<Client> createVendorClient(
            @PathVariable Long vendorId,
            @RequestBody Client client
    ) {

        Client savedClient =
                clientService.createVendorClient(
                        vendorId,
                        client
                );

        return new ResponseEntity<>(
                savedClient,
                HttpStatus.CREATED
        );
    }
}