package com.shulventures.solarservicesbackend.controller;


import com.shulventures.solarservicesbackend.entity.Salary;
import com.shulventures.solarservicesbackend.service.SalaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shulventures.solarservicesbackend.dto.EmployeeAdvanceSummaryResponse;

import java.util.List;

@RestController
@RequestMapping("/api/salaries")
@CrossOrigin(origins = "http://localhost:5173")
public class SalaryController {

    private final SalaryService salaryService;


    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }


    //============ CREATE SALARY FOR EMPLOYEE =====================

    @PostMapping("/employee/{employeeId}")
    public ResponseEntity<Salary> createSalary(
            @PathVariable Long employeeId,
            @RequestBody Salary salary
    ) {

        Salary savedSalary =
                salaryService.createSalary(employeeId, salary);

        return new ResponseEntity<>(
                savedSalary,
                HttpStatus.CREATED
        );
    }


    //=================== GET ALL SALARIES ===============

    @GetMapping
    public ResponseEntity<List<Salary>> getAllSalaries() {

        return ResponseEntity.ok(
                salaryService.getAllSalaries()
        );
    }


    //============= GET ALL SALARIES OF AN EMPLOYEE ====================

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Salary>> getSalariesByEmployee(
            @PathVariable Long employeeId
    ) {

        return ResponseEntity.ok(
                salaryService.getSalariesByEmployee(employeeId)
        );
    }


    //=============== GET EMPLOYEE ADVANCE SUMMARY ============================

    /*
     * Example:
     *
     * GET
     * /api/salaries/employee/1/advance-summary
     *
     * Returns:
     *
     * totalAdvanceTaken
     * totalAdvanceDeducted
     * pendingAdvance
     */

    @GetMapping("/employee/{employeeId}/advance-summary")
    public ResponseEntity<EmployeeAdvanceSummaryResponse>
    getEmployeeAdvanceSummary(@PathVariable Long employeeId) {

        return ResponseEntity.ok(
                salaryService.getEmployeeAdvanceSummary(
                        employeeId
                )
        );
    }


    //================= GET SALARY BY EMPLOYEE + MONTH ===========================

    /*
     * Example:
     *
     * GET
     * /api/salaries/employee/1/month/2026-08
     *
     * This returns the salary of employee 1
     * for August 2026.
     */

    @GetMapping("/employee/{employeeId}/month/{month}")
    public ResponseEntity<Salary> getSalaryByEmployeeAndMonth(
            @PathVariable Long employeeId,
            @PathVariable String month
    ) {

        return salaryService
                .getSalaryByEmployeeAndMonth(
                        employeeId,
                        month
                )
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity.notFound().build()
                );
    }


    //====== GET SALARY BY ID ==========================

    @GetMapping("/{id}")
    public ResponseEntity<Salary> getSalaryById(
            @PathVariable Long id
    ) {

        return salaryService
                .getSalaryById(id)
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity.notFound().build()
                );
    }


    //==================== UPDATE SALARY ====================
    @PutMapping("/{id}/employee/{employeeId}")
    public ResponseEntity<Salary> updateSalary(
            @PathVariable Long id,
            @PathVariable Long employeeId,
            @RequestBody Salary salary
    ) {

        Salary updatedSalary =
                salaryService.updateSalary(
                        id,
                        employeeId,
                        salary
                );
        return ResponseEntity.ok(updatedSalary);
    }


    //============= DELETE SALARY ===================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalary(
            @PathVariable Long id
    ) {
        salaryService.deleteSalary(id);
        return ResponseEntity.noContent().build();
    }
}
