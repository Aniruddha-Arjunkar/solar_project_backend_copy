package com.shulventures.solarservicesbackend.service;

import com.shulventures.solarservicesbackend.entity.Employee;
import com.shulventures.solarservicesbackend.entity.Salary;
import com.shulventures.solarservicesbackend.repository.EmployeeRepository;
import com.shulventures.solarservicesbackend.repository.SalaryRepository;
import org.springframework.stereotype.Service;

import com.shulventures.solarservicesbackend.dto.EmployeeAdvanceSummaryResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SalaryService {

    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;

    public SalaryService(
            SalaryRepository salaryRepository,
            EmployeeRepository employeeRepository
    ) {
        this.salaryRepository = salaryRepository;
        this.employeeRepository = employeeRepository;
    }

    //============ CREATE SALARY =================

    public Salary createSalary(Long employeeId, Salary salary) {

        // ----------------- FIND EMPLOYEE -------------

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + employeeId));

        // --------------- SET EMPLOYEE -------------
        salary.setEmployee(employee);


        //---------------- PAYMENT TYPE -----------------

        String paymentType = salary.getPaymentType();

        if (paymentType == null || paymentType.isBlank()) {
            salary.setPaymentType("SALARY");
        } else {
            salary.setPaymentType(
                    paymentType.trim().toUpperCase()
            );
        }

        // ------------------- VALIDATE ADVANCE DEDUCTION --------------

        BigDecimal requestedAdvance =
                salary.getAdvance() != null
                        ? salary.getAdvance()
                        : BigDecimal.ZERO;

        if (requestedAdvance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Advance amount cannot be negative.");
        }


        if ("SALARY".equalsIgnoreCase(salary.getPaymentType()) &&
                        requestedAdvance.compareTo(BigDecimal.ZERO) > 0
        ) {

            EmployeeAdvanceSummaryResponse summary = getEmployeeAdvanceSummary(employeeId);

            if (requestedAdvance.compareTo(summary.getPendingAdvance()) > 0) {
                throw new RuntimeException(
                        "Advance deduction of "
                                + requestedAdvance
                                + " cannot be greater than pending advance of "
                                + summary.getPendingAdvance()
                );
            }
        }


        //--------------- CALCULATE FINAL SALARY ---------------
        BigDecimal netSalary =
                salary.getAmount() != null
                        ? salary.getAmount()
                        : BigDecimal.ZERO;

        BigDecimal advance =
                salary.getAdvance() != null
                        ? salary.getAdvance()
                        : BigDecimal.ZERO;

        BigDecimal reimbursement =
                salary.getReimbursement() != null
                        ? salary.getReimbursement()
                        : BigDecimal.ZERO;


        BigDecimal finalSalary =
                netSalary
                        .subtract(advance)
                        .add(reimbursement);


        salary.setAmount(finalSalary);

        return salaryRepository.save(salary);
    }

    // GET ALL SALARIES
    public List<Salary> getAllSalaries() {
        return salaryRepository.findAll();
    }


    // GET SALARIES BY EMPLOYEE
    public List<Salary> getSalariesByEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException(
                    "Employee not found with id: " + employeeId
            );
        }
        return salaryRepository.findByEmployeeId(employeeId);
    }


    // ================= GET EMPLOYEE ADVANCE SUMMARY ============================
    public EmployeeAdvanceSummaryResponse
    getEmployeeAdvanceSummary(Long employeeId) {

        // ------------ VALIDATE EMPLOYEE ------------------

        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }

        // -------- GET ALL SALARY / ADVANCE RECORDS ---------------------
        List<Salary> salaries = salaryRepository.findByEmployeeId(employeeId);

        // -------------- CALCULATE TOTAL ADVANCE TAKEN --------------------
        BigDecimal totalAdvanceTaken =
                salaries.stream()
                        .filter(salary ->
                                "ADVANCE".equalsIgnoreCase(
                                        salary.getPaymentType()
                                )
                        )
                        .map(salary ->
                                salary.getAdvance() != null
                                        ? salary.getAdvance()
                                        : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);


        // --------------- CALCULATE TOTAL ADVANCE DEDUCTED ---------------------

        BigDecimal totalAdvanceDeducted =
                salaries.stream()
                        .filter(salary -> {

                            String paymentType =
                                    salary.getPaymentType();

                            BigDecimal advance =
                                    salary.getAdvance() != null
                                            ? salary.getAdvance()
                                            : BigDecimal.ZERO;


                            return ("SALARY".equalsIgnoreCase(
                                            paymentType)
                                            &&
                                            advance.compareTo(BigDecimal.ZERO) > 0
                            );

                        })
                        .map(salary ->
                                salary.getAdvance() != null
                                        ? salary.getAdvance()
                                        : BigDecimal.ZERO
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );


        // ------------- CALCULATE PENDING ADVANCE ------------------------
        BigDecimal pendingAdvance = totalAdvanceTaken.subtract(totalAdvanceDeducted);


        // ------------ NEVER RETURN NEGATIVE BALANCE -------------------

        if (pendingAdvance.compareTo(BigDecimal.ZERO) < 0) {
            pendingAdvance = BigDecimal.ZERO;
        }

        // -------- RETURN SUMMARY --------------------------

        return new EmployeeAdvanceSummaryResponse(
                employeeId,
                totalAdvanceTaken,
                totalAdvanceDeducted,
                pendingAdvance
        );
    }



    // GET SALARY BY EMPLOYEE + MONTH

    public Optional<Salary> getSalaryByEmployeeAndMonth(
            Long employeeId,
            String month
    ) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException(
                    "Employee not found with id: " + employeeId
            );
        }
        return salaryRepository.findByEmployeeIdAndMonth(
                employeeId,
                month
        );
    }


    // GET SALARY BY ID

    public Optional<Salary> getSalaryById(Long id) {
        return salaryRepository.findById(id);
    }



    // UPDATE SALARY

    public Salary updateSalary(
            Long id,
            Long employeeId,
            Salary salaryData
    ) {
        Salary existingSalary = salaryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Salary not found with id: " + id
                        )
                );

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found with id: " + employeeId
                        )
                );

        // EMPLOYEE
        existingSalary.setEmployee(employee);


        // SALARY PERIOD
        existingSalary.setMonth(
                salaryData.getMonth()
        );



        // SALARY AMOUNT
        BigDecimal netSalary =
                salaryData.getAmount() != null
                        ? salaryData.getAmount()
                        : BigDecimal.ZERO;

        BigDecimal advance =
                salaryData.getAdvance() != null
                        ? salaryData.getAdvance()
                        : BigDecimal.ZERO;

        BigDecimal reimbursement =
                salaryData.getReimbursement() != null
                        ? salaryData.getReimbursement()
                        : BigDecimal.ZERO;

        BigDecimal finalSalary =
                netSalary
                        .subtract(advance)
                        .add(reimbursement);

        existingSalary.setAmount(finalSalary);
//        existingSalary.setAmount(
//                salaryData.getAmount()
//        );




        // SALARY COMPONENTS

        existingSalary.setBasic(
                salaryData.getBasic()
        );

        existingSalary.setHra(
                salaryData.getHra()
        );

        existingSalary.setConveyance(
                salaryData.getConveyance()
        );

        existingSalary.setFoodAllowance(
                salaryData.getFoodAllowance()
        );

        existingSalary.setPerformanceIncentive(
                salaryData.getPerformanceIncentive()
        );


        // DEDUCTIONS / OTHER AMOUNTS
        existingSalary.setProfessionTax(
                salaryData.getProfessionTax()
        );

        existingSalary.setAdvance(
                salaryData.getAdvance()
        );

        existingSalary.setReimbursement(
                salaryData.getReimbursement()
        );



        // PAYMENT DETAILS
        existingSalary.setPaymentType(
                salaryData.getPaymentType()
        );

        existingSalary.setAdvanceDate(
                salaryData.getAdvanceDate()
        );



        // PER DAY SALARY

        existingSalary.setPerDayAmount(
                salaryData.getPerDayAmount()
        );

        existingSalary.setPresentDays(
                salaryData.getPresentDays()
        );



        // REMARK
        existingSalary.setRemark(
                salaryData.getRemark()
        );

        return salaryRepository.save(existingSalary);
    }



    // DELETE SALARY
    public void deleteSalary(Long id) {

        if (!salaryRepository.existsById(id)) {
            throw new RuntimeException(
                    "Salary not found with id: " + id
            );
        }
        salaryRepository.deleteById(id);
    }
}
