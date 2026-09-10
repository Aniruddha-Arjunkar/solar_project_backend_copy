package com.shulventures.solarservicesbackend.repository;

import com.shulventures.solarservicesbackend.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // FIND INVOICES BY CLIENT
    List<Invoice> findByClientId(Long clientId);


    // FIND INVOICE BY INVOICE NUMBER
    Optional<Invoice> findByGstInvoiceNo(String gstInvoiceNo);


    // CHECK DUPLICATE INVOICE NUMBER
    boolean existsByGstInvoiceNo(String gstInvoiceNo);
}
