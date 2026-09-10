package com.shulventures.solarservicesbackend.controller;



import com.shulventures.solarservicesbackend.entity.Vendor;
import com.shulventures.solarservicesbackend.service.VendorService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "http://localhost:5173")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(
            VendorService vendorService
    ) {
        this.vendorService = vendorService;
    }


    // ============================================================
    // CREATE VENDOR
    // ============================================================

    @PostMapping
    public ResponseEntity<Vendor> createVendor(
            @RequestBody Vendor vendor
    ) {
        Vendor savedVendor =
                vendorService.createVendor(vendor);
        return new ResponseEntity<>(
                savedVendor,
                HttpStatus.CREATED
        );
    }


    // ============================================================
    // GET ALL VENDORS
    // ============================================================

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors() {
        return ResponseEntity.ok(
                vendorService.getAllVendors()
        );
    }


    // ============================================================
    // GET VENDOR BY ID
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(
            @PathVariable Long id
    ) {
        return vendorService
                .getVendorById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }


    // ============================================================
    // UPDATE VENDOR
    // ============================================================

    @PutMapping("/{id}")
    public ResponseEntity<Vendor> updateVendor(
            @PathVariable Long id,
            @RequestBody Vendor vendor
    ) {
        return ResponseEntity.ok(
                vendorService.updateVendor(
                        id,
                        vendor
                )
        );
    }


    // ============================================================
    // DELETE VENDOR
    // ============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(
            @PathVariable Long id
    ) {
        vendorService.deleteVendor(id);
        return ResponseEntity.noContent().build();
    }
}
