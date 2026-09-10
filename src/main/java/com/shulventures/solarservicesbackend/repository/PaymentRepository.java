package com.shulventures.solarservicesbackend.repository;

import com.shulventures.solarservicesbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    // ============================================================
    // GET PAYMENTS OF ONE CLIENT
    // ============================================================

    List<Payment> findByClientId(Long clientId);


    // ============================================================
    // PAYMENT HISTORY BY DATE RANGE
    // ============================================================

    List<Payment> findByPaymentDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );


    // ============================================================
    // PAYMENTS OF CLIENT BY DATE RANGE
    // ============================================================

    List<Payment> findByClientIdAndPaymentDateBetween(
            Long clientId,
            LocalDate startDate,
            LocalDate endDate
    );


    // ============================================================
    // PAYMENTS BY VENDOR
    // ============================================================

    List<Payment> findByVendorId(Long vendorId);
}
