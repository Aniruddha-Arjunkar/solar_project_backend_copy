package com.shulventures.solarservicesbackend.controller;


import com.shulventures.solarservicesbackend.entity.Salary;
import com.shulventures.solarservicesbackend.service.SalaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salaries")
@CrossOrigin(origins = "http://localhost:5173")
public class SalaryController {

    private final SalaryService salaryService;


    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }


    //====================================================
    // CREATE SALARY FOR EMPLOYEE
    //====================================================

    /*
     * Example:
     *
     * POST
     * /api/salaries/employee/1
     *
     * Employee ID = 1
     */

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


    //====================================================
    // GET ALL SALARIES
    //====================================================

    /*
     * Example:
     *
     * GET
     * /api/salaries
     */

    @GetMapping
    public ResponseEntity<List<Salary>> getAllSalaries() {

        return ResponseEntity.ok(
                salaryService.getAllSalaries()
        );
    }


    //====================================================
    // GET ALL SALARIES OF AN EMPLOYEE
    //====================================================

    /*
     * Example:
     *
     * GET
     * /api/salaries/employee/1
     */

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Salary>> getSalariesByEmployee(
            @PathVariable Long employeeId
    ) {

        return ResponseEntity.ok(
                salaryService.getSalariesByEmployee(employeeId)
        );
    }


    //====================================================
    // GET SALARY BY EMPLOYEE + MONTH
    //====================================================

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


    //====================================================
    // GET SALARY BY ID
    //====================================================

    /*
     * Example:
     *
     * GET
     * /api/salaries/10
     */

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


    //====================================================
    // UPDATE SALARY
    //====================================================

    /*
     * Example:
     *
     * PUT
     * /api/salaries/10/employee/1
     */

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


    //====================================================
    // DELETE SALARY
    //====================================================

    /*
     * Example:
     *
     * DELETE
     * /api/salaries/10
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalary(
            @PathVariable Long id
    ) {
        salaryService.deleteSalary(id);
        return ResponseEntity.noContent().build();
    }
}
