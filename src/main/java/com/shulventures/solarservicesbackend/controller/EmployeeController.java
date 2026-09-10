package com.shulventures.solarservicesbackend.controller;

import com.shulventures.solarservicesbackend.entity.Employee;
import com.shulventures.solarservicesbackend.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    // CREATE EMPLOYEE

    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @RequestBody Employee employee
    ) {
        Employee savedEmployee =
                employeeService.createEmployee(employee);
        return new ResponseEntity<>(
                savedEmployee,
                HttpStatus.CREATED
        );
    }


    // GET ALL EMPLOYEES

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees =
                employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }



    // GET EMPLOYEE BY ID

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable Long id
    ) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity.notFound().build()
                );
    }



    // UPDATE EMPLOYEE

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee
    ) {
        Employee updatedEmployee =
                employeeService.updateEmployee(
                        id,
                        employee
                );
        return ResponseEntity.ok(updatedEmployee);
    }



    // DELETE EMPLOYEE

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id
    ) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
