package com.shulventures.solarservicesbackend.service;

import com.shulventures.solarservicesbackend.entity.Expense;
import com.shulventures.solarservicesbackend.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }


    // ============================================================
    // CREATE EXPENSE
    // ============================================================

    public Expense createExpense(Expense expense) {

        validateExpense(expense);

        return expenseRepository.save(expense);
    }


    // ============================================================
    // GET ALL EXPENSES
    // ============================================================

    public List<Expense> getAllExpenses() {

        return expenseRepository.findAll();
    }


    // ============================================================
    // GET EXPENSE BY ID
    // ============================================================

    public Optional<Expense> getExpenseById(Long id) {

        return expenseRepository.findById(id);
    }


    // ============================================================
    // GET EXPENSES BY DATE RANGE
    // ============================================================

    public List<Expense> getExpensesByDateRange(
            LocalDate startDate,
            LocalDate endDate
    ) {

        return expenseRepository.findByExpenseDateBetween(
                startDate,
                endDate
        );
    }


    // ============================================================
    // UPDATE EXPENSE
    // ============================================================

    public Expense updateExpense(
            Long id,
            Expense expense
    ) {

        Expense existingExpense =
                expenseRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Expense not found with id: " + id
                                )
                        );

        validateExpense(expense);


        existingExpense.setExpense(
                expense.getExpense()
        );

        existingExpense.setExpenseDate(
                expense.getExpenseDate()
        );

        existingExpense.setAmount(
                expense.getAmount()
        );

        existingExpense.setPaidBy(
                expense.getPaidBy()
        );

        existingExpense.setService(
                expense.getService()
        );


        return expenseRepository.save(
                existingExpense
        );
    }


    // ============================================================
    // DELETE EXPENSE
    // ============================================================

    public void deleteExpense(Long id) {

        if (!expenseRepository.existsById(id)) {

            throw new RuntimeException(
                    "Expense not found with id: " + id
            );
        }

        expenseRepository.deleteById(id);
    }


    // ============================================================
    // VALIDATION
    // ============================================================

    private void validateExpense(
            Expense expense
    ) {

        if (
                expense.getExpense() == null ||
                        expense.getExpense().isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Expense name is required."
            );
        }


        if (expense.getExpenseDate() == null) {

            throw new IllegalArgumentException(
                    "Expense date is required."
            );
        }


        if (
                expense.getAmount() == null ||
                        expense.getAmount()
                                .compareTo(BigDecimal.ZERO) <= 0
        ) {

            throw new IllegalArgumentException(
                    "Expense amount must be greater than zero."
            );
        }


        if (
                expense.getPaidBy() == null ||
                        expense.getPaidBy().isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Paid By is required."
            );
        }
    }
}
