package com.shulventures.solarservicesbackend.repository;

import com.shulventures.solarservicesbackend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // ==================== GST FILTER ====================
    List<Client> findByApplyGst(Boolean applyGst);


    // ==================== SOURCE FILTER ====================
    List<Client> findByAddedBy(String addedBy);


    // ==================== VENDOR CLIENTS ====================
    List<Client> findByVendorId(Long vendorId);
}
