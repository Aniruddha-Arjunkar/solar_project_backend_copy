package com.shulventures.solarservicesbackend.controller;

import com.shulventures.solarservicesbackend.entity.Expense;
import com.shulventures.solarservicesbackend.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "http://localhost:5173")
public class ExpenseController {

    private final ExpenseService expenseService;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public ExpenseController(
            ExpenseService expenseService
    ) {

        this.expenseService = expenseService;
    }


    // ============================================================
    // CREATE EXPENSE
    // ============================================================

    @PostMapping
    public ResponseEntity<Expense> createExpense(
            @RequestBody Expense expense
    ) {

        Expense savedExpense =
                expenseService.createExpense(expense);

        return new ResponseEntity<>(
                savedExpense,
                HttpStatus.CREATED
        );
    }


    // ============================================================
    // GET ALL EXPENSES
    // ============================================================

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {

        return ResponseEntity.ok(
                expenseService.getAllExpenses()
        );
    }


    // ============================================================
    // GET EXPENSES BY DATE RANGE
    // ============================================================

    @GetMapping("/history")
    public ResponseEntity<List<Expense>> getExpensesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {

        return ResponseEntity.ok(
                expenseService.getExpensesByDateRange(
                        startDate,
                        endDate
                )
        );
    }


    // ============================================================
    // GET EXPENSE BY ID
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(
            @PathVariable Long id
    ) {

        return expenseService
                .getExpenseById(id)
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity.notFound().build()
                );
    }


    // ============================================================
    // UPDATE EXPENSE
    // ============================================================

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(
            @PathVariable Long id,
            @RequestBody Expense expense
    ) {

        Expense updatedExpense =
                expenseService.updateExpense(
                        id,
                        expense
                );

        return ResponseEntity.ok(
                updatedExpense
        );
    }


    // ============================================================
    // DELETE EXPENSE
    // ============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id
    ) {

        expenseService.deleteExpense(id);

        return ResponseEntity.noContent().build();
    }
}
