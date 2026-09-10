package com.shulventures.solarservicesbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "attendance",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_employee_attendance_date",
                        columnNames = {
                                "employee_id",
                                "attendance_date"
                        }
                )
        }
)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "employee_id",
            nullable = false
    )
    @JsonIgnore
    private Employee employee;

    @Column(name = "attendance_date",
            nullable = false)
    private LocalDate attendanceDate;

    @Column(nullable = false)
    private String status;


    @Column(columnDefinition = "TEXT")
    private String remark;


    public Attendance() {
    }

    
    // GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
