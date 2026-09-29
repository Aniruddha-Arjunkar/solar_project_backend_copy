package com.shulventures.solarservicesbackend.dto;

import java.math.BigDecimal;

public class EmployeeAdvanceSummaryResponse {

    private Long employeeId;
    private BigDecimal totalAdvanceTaken;
    private BigDecimal totalAdvanceDeducted;
    private BigDecimal pendingAdvance;


    // =========== CONSTRUCTOR ===================

    public EmployeeAdvanceSummaryResponse(
            Long employeeId,
            BigDecimal totalAdvanceTaken,
            BigDecimal totalAdvanceDeducted,
            BigDecimal pendingAdvance
    ) {
        this.employeeId = employeeId;
        this.totalAdvanceTaken = totalAdvanceTaken;
        this.totalAdvanceDeducted = totalAdvanceDeducted;
        this.pendingAdvance = pendingAdvance;
    }


    // ============================================================
    // GETTERS
    // ============================================================

    public Long getEmployeeId() {
        return employeeId;
    }

    public BigDecimal getTotalAdvanceTaken() {
        return totalAdvanceTaken;
    }

    public BigDecimal getTotalAdvanceDeducted() {
        return totalAdvanceDeducted;
    }

    public BigDecimal getPendingAdvance() {
        return pendingAdvance;
    }
}
