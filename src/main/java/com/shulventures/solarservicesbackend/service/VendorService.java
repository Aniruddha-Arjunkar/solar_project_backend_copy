package com.shulventures.solarservicesbackend.service;

import com.shulventures.solarservicesbackend.entity.Vendor;
import com.shulventures.solarservicesbackend.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }


    // ============================================================
    // CREATE VENDOR
    // ============================================================

    public Vendor createVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }


    // ============================================================
    // GET ALL VENDORS
    // ============================================================

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }


    // ============================================================
    // GET VENDOR BY ID
    // ============================================================

    public Optional<Vendor> getVendorById(Long id) {
        return vendorRepository.findById(id);
    }


    // ============================================================
    // UPDATE VENDOR
    // ============================================================

    public Vendor updateVendor(
            Long id,
            Vendor updatedVendor
    ) {
        Vendor existingVendor =
                vendorRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Vendor not found with id: " + id
                                )
                        );
        // ========================================================
        // UPDATE VENDOR INFORMATION
        // ========================================================

        existingVendor.setVendorName(
                updatedVendor.getVendorName()
        );
        existingVendor.setPhone(
                updatedVendor.getPhone()
        );
        existingVendor.setEmail(
                updatedVendor.getEmail()
        );
        existingVendor.setAddress(
                updatedVendor.getAddress()
        );
        existingVendor.setRemarks(
                updatedVendor.getRemarks()
        );
        return vendorRepository.save(
                existingVendor
        );
    }


    // ============================================================
    // DELETE VENDOR
    // ============================================================

    public void deleteVendor(Long id) {
        if (!vendorRepository.existsById(id)) {
            throw new RuntimeException(
                    "Vendor not found with id: " + id
            );
        }
        vendorRepository.deleteById(id);
    }
}
