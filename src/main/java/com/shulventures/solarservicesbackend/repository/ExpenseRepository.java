package com.shulventures.solarservicesbackend.repository;

import com.shulventures.solarservicesbackend.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // ============================================================
    // FIND EXPENSES BY DATE RANGE
    // ============================================================

    List<Expense> findByExpenseDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}
