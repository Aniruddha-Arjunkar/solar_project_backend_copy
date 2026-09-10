package com.shulventures.solarservicesbackend.dto;
import java.time.LocalDate;

public class AttendanceResponse {

    // ============================================================
    // ATTENDANCE DETAILS
    // ============================================================

    private Long id;

    private Long employeeId;

    private LocalDate attendanceDate;

    private String status;

    private String remark;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public AttendanceResponse() {
    }


    public AttendanceResponse(
            Long id,
            Long employeeId,
            LocalDate attendanceDate,
            String status,
            String remark
    ) {
        this.id = id;
        this.employeeId = employeeId;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.remark = remark;
    }


    // ============================================================
    // GETTERS AND SETTERS
    // ============================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }


    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(
            LocalDate attendanceDate
    ) {
        this.attendanceDate = attendanceDate;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
