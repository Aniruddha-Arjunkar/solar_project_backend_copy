package com.shulventures.solarservicesbackend.service;

import com.shulventures.solarservicesbackend.entity.Client;
import com.shulventures.solarservicesbackend.entity.Payment;
import com.shulventures.solarservicesbackend.repository.ClientRepository;
import com.shulventures.solarservicesbackend.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ClientRepository clientRepository;


    public PaymentService(
            PaymentRepository paymentRepository,
            ClientRepository clientRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.clientRepository = clientRepository;
    }


    // ============================================================
    // CREATE PAYMENT
    // ============================================================

    @Transactional
    public Payment createPayment(
            Long clientId,
            Payment payment
    ) {

        // --------------------------------------------------------
        // FIND CLIENT
        // --------------------------------------------------------

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Client not found with id: " + clientId
                        )
                );


        // --------------------------------------------------------
        // GET CLIENT PAYABLE AMOUNT
        // --------------------------------------------------------
        /*
         * finalAmount includes GST when GST is applied.
         *
         * Example:
         *
         * totalAmount = 100000
         * gstAmount   = 18000
         * finalAmount = 118000
         *
         * Therefore Accounts uses finalAmount.
         *
         * If finalAmount is not available, we fall back
         * to totalAmount.
         */

        BigDecimal payableAmount =
                client.getFinalAmount();

        if (payableAmount == null) {
            payableAmount =
                    client.getTotalAmount();
        }

        if (payableAmount == null) {
            payableAmount =
                    BigDecimal.ZERO;
        }


        // --------------------------------------------------------
        // CURRENT PAYMENT AMOUNT
        // --------------------------------------------------------

        BigDecimal paidAmount =
                payment.getPaidAmount();

        if (paidAmount == null) {
            paidAmount =
                    BigDecimal.ZERO;
        }


        // Payment must be greater than zero.

        if (paidAmount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Payment amount must be greater than zero."
            );
        }


        // --------------------------------------------------------
        // GET PREVIOUS PAYMENTS
        // --------------------------------------------------------

        List<Payment> previousPayments =
                paymentRepository.findByClientId(clientId);


        BigDecimal previousPaid =
                previousPayments.stream()
                        .map(Payment::getPaidAmount)
                        .filter(amount -> amount != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );


        // --------------------------------------------------------
        // CALCULATE CURRENT DUE
        // --------------------------------------------------------

        BigDecimal totalPaidAfterPayment =
                previousPaid.add(paidAmount);


        BigDecimal dueAmount =
                payableAmount.subtract(
                        totalPaidAfterPayment
                );


        // --------------------------------------------------------
        // PREVENT OVER PAYMENT
        // --------------------------------------------------------

        if (dueAmount.compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Payment amount exceeds the remaining due amount."
            );
        }


        // --------------------------------------------------------
        // SET PAYMENT INFORMATION
        // --------------------------------------------------------

        payment.setClient(client);

        /*
         * Store the payable amount at the time of payment.
         */
        payment.setTotalAmount(
                payableAmount
        );

        payment.setPaidAmount(
                paidAmount
        );

        payment.setDueAmount(
                dueAmount
        );


        // --------------------------------------------------------
        // PAYMENT DATE
        // --------------------------------------------------------

        if (payment.getPaymentDate() == null) {

            payment.setPaymentDate(
                    LocalDate.now()
            );
        }


        // --------------------------------------------------------
        // VENDOR INFORMATION
        // --------------------------------------------------------
        /*
         * If the client was created by a vendor,
         * automatically copy the vendor ID.
         */

        payment.setVendorId(
                client.getVendorId()
        );


        // --------------------------------------------------------
        // SAVE PAYMENT
        // --------------------------------------------------------

        return paymentRepository.save(payment);
    }


    // ============================================================
    // GET ALL PAYMENTS
    // ============================================================

    public List<Payment> getAllPayments() {

        return paymentRepository.findAll();
    }


    // ============================================================
    // GET PAYMENTS BY CLIENT
    // ============================================================

    public List<Payment> getPaymentsByClient(
            Long clientId
    ) {

        if (!clientRepository.existsById(clientId)) {

            throw new RuntimeException(
                    "Client not found with id: " + clientId
            );
        }

        return paymentRepository.findByClientId(
                clientId
        );
    }


    // ============================================================
    // GET PAYMENT BY ID
    // ============================================================

    public Optional<Payment> getPaymentById(
            Long id
    ) {

        return paymentRepository.findById(id);
    }


    // ============================================================
    // GET PAYMENTS BY DATE RANGE
    // ============================================================

    public List<Payment> getPaymentsByDateRange(
            LocalDate startDate,
            LocalDate endDate
    ) {

        return paymentRepository
                .findByPaymentDateBetween(
                        startDate,
                        endDate
                );
    }


    // ============================================================
    // UPDATE PAYMENT
    // ============================================================

    @Transactional
    public Payment updatePayment(
            Long paymentId,
            Payment updatedPayment
    ) {

        // --------------------------------------------------------
        // FIND EXISTING PAYMENT
        // --------------------------------------------------------

        Payment existingPayment =
                paymentRepository.findById(paymentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found with id: "
                                                + paymentId
                                )
                        );


        // --------------------------------------------------------
        // GET CLIENT
        // --------------------------------------------------------

        Client client =
                existingPayment.getClient();


        if (client == null) {

            throw new RuntimeException(
                    "Client associated with payment was not found."
            );
        }


        Long clientId =
                client.getId();


        // --------------------------------------------------------
        // GET CLIENT PAYABLE AMOUNT
        // --------------------------------------------------------

        BigDecimal payableAmount =
                client.getFinalAmount();

        if (payableAmount == null) {

            payableAmount =
                    client.getTotalAmount();
        }

        if (payableAmount == null) {

            payableAmount =
                    BigDecimal.ZERO;
        }


        // --------------------------------------------------------
        // NEW PAYMENT AMOUNT
        // --------------------------------------------------------

        BigDecimal newPaidAmount =
                updatedPayment.getPaidAmount();

        if (newPaidAmount == null) {

            newPaidAmount =
                    BigDecimal.ZERO;
        }


        if (newPaidAmount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Payment amount must be greater than zero."
            );
        }


        // --------------------------------------------------------
        // GET OTHER PAYMENTS
        // --------------------------------------------------------

        List<Payment> payments =
                paymentRepository.findByClientId(
                        clientId
                );


        BigDecimal otherPaymentsTotal =
                payments.stream()
                        .filter(payment ->
                                !payment.getId()
                                        .equals(paymentId)
                        )
                        .map(Payment::getPaidAmount)
                        .filter(amount -> amount != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );


        // --------------------------------------------------------
        // CALCULATE NEW DUE
        // --------------------------------------------------------

        BigDecimal totalPaidAfterUpdate =
                otherPaymentsTotal.add(
                        newPaidAmount
                );


        BigDecimal dueAmount =
                payableAmount.subtract(
                        totalPaidAfterUpdate
                );


        // --------------------------------------------------------
        // PREVENT OVER PAYMENT
        // --------------------------------------------------------

        if (dueAmount.compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Payment amount exceeds the remaining due amount."
            );
        }


        // --------------------------------------------------------
        // UPDATE PAYMENT
        // --------------------------------------------------------

        existingPayment.setTotalAmount(
                payableAmount
        );

        existingPayment.setPaidAmount(
                newPaidAmount
        );

        existingPayment.setDueAmount(
                dueAmount
        );

        existingPayment.setPaymentDate(
                updatedPayment.getPaymentDate()
        );

        existingPayment.setDueDate(
                updatedPayment.getDueDate()
        );

        existingPayment.setPaymentGateway(
                updatedPayment.getPaymentGateway()
        );


        // --------------------------------------------------------
        // KEEP VENDOR INFORMATION FROM CLIENT
        // --------------------------------------------------------

        existingPayment.setVendorId(
                client.getVendorId()
        );


        return paymentRepository.save(
                existingPayment
        );
    }


    // ============================================================
    // DELETE PAYMENT
    // ============================================================

    public void deletePayment(Long id) {

        if (!paymentRepository.existsById(id)) {

            throw new RuntimeException(
                    "Payment not found with id: " + id
            );
        }

        paymentRepository.deleteById(id);
    }
}
