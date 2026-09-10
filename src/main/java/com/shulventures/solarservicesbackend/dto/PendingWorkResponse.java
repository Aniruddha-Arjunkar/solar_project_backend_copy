package com.shulventures.solarservicesbackend.dto;

import java.time.LocalDate;

public class PendingWorkResponse {

    private Long id;
    private Long clientId;
    private String clientName;
    private String workDescription;
    private String assignedTo;
    private LocalDate dueDate;
    private String status;

    // ---------------------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------------------

    public PendingWorkResponse(
            Long id,
            Long clientId,
            String clientName,
            String workDescription,
            String assignedTo,
            LocalDate dueDate,
            String status
    ) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.workDescription = workDescription;
        this.assignedTo = assignedTo;
        this.dueDate = dueDate;
        this.status = status;
    }

    // ---------------------------------------------------------
    // GETTERS
    // ---------------------------------------------------------

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    // ---------------------------------------------------------
    // SETTERS
    // ---------------------------------------------------------

    public void setId(Long id) {
        this.id = id;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
