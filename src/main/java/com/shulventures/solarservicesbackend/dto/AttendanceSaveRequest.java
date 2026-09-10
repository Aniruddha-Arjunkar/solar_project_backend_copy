package com.shulventures.solarservicesbackend.dto;

import java.time.LocalDate;
import java.util.List;

public class AttendanceSaveRequest {

    // ATTENDANCE DATE
    private LocalDate attendanceDate;



    // EMPLOYEE ATTENDANCE LIST
    private List<AttendanceItem> attendance;


    // GETTERS AND SETTERS
    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(
            LocalDate attendanceDate
    ) {
        this.attendanceDate = attendanceDate;
    }


    public List<AttendanceItem> getAttendance() {
        return attendance;
    }

    public void setAttendance(
            List<AttendanceItem> attendance
    ) {
        this.attendance = attendance;
    }


    // ATTENDANCE ITEM
    public static class AttendanceItem {
        private Long employeeId;
        private String status;
        private String remark;

        public Long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(
                Long employeeId
        ) {
            this.employeeId = employeeId;
        }


        public String getStatus() {
            return status;
        }

        public void setStatus(
                String status
        ) {
            this.status = status;
        }


        public String getRemark() {
            return remark;
        }

        public void setRemark(
                String remark
        ) {
            this.remark = remark;
        }
    }
}
