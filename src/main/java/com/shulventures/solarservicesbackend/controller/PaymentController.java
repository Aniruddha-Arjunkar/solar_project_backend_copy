package com.shulventures.solarservicesbackend.controller;

import com.shulventures.solarservicesbackend.entity.Payment;
import com.shulventures.solarservicesbackend.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {


    private final PaymentService paymentService;


    public PaymentController(
            PaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }


    // ============================================================
    // CREATE PAYMENT FOR CLIENT
    // ============================================================

    @PostMapping("/client/{clientId}")
    public ResponseEntity<Payment> createPayment(
            @PathVariable Long clientId,
            @RequestBody Payment payment
    ) {

        Payment savedPayment =
                paymentService.createPayment(
                        clientId,
                        payment
                );

        return new ResponseEntity<>(
                savedPayment,
                HttpStatus.CREATED
        );
    }


    // ============================================================
    // GET ALL PAYMENTS
    // ============================================================

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }


    // ============================================================
    // GET PAYMENTS BY CLIENT
    // ============================================================

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Payment>> getPaymentsByClient(
            @PathVariable Long clientId
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByClient(
                        clientId
                )
        );
    }


    // ============================================================
    // GET PAYMENTS BY DATE RANGE
    // ============================================================

    @GetMapping("/history")
    public ResponseEntity<List<Payment>> getPaymentsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByDateRange(
                        startDate,
                        endDate
                )
        );
    }


    // ============================================================
    // GET PAYMENT BY ID
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(
            @PathVariable Long id
    ) {

        return paymentService
                .getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity
                                .notFound()
                                .build()
                );
    }


    // ============================================================
    // UPDATE PAYMENT
    // ============================================================

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(
            @PathVariable Long id,
            @RequestBody Payment payment
    ) {

        Payment updatedPayment =
                paymentService.updatePayment(
                        id,
                        payment
                );

        return ResponseEntity.ok(
                updatedPayment
        );
    }


    // ============================================================
    // DELETE PAYMENT
    // ============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(
            @PathVariable Long id
    ) {

        paymentService.deletePayment(id);

        return ResponseEntity.noContent().build();
    }
}
