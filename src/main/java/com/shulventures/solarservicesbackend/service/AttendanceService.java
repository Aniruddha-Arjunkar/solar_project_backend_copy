package com.shulventures.solarservicesbackend.service;

import com.shulventures.solarservicesbackend.dto.AttendanceSaveRequest;
import com.shulventures.solarservicesbackend.entity.Attendance;
import com.shulventures.solarservicesbackend.entity.Employee;
import com.shulventures.solarservicesbackend.repository.AttendanceRepository;
import com.shulventures.solarservicesbackend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import com.shulventures.solarservicesbackend.dto.AttendanceResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final EmployeeRepository employeeRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            EmployeeRepository employeeRepository
    ) {
        this.attendanceRepository =
                attendanceRepository;

        this.employeeRepository =
                employeeRepository;
    }


    public List<Attendance> saveAttendance(
            AttendanceSaveRequest request
    ) {

        if (request.getAttendanceDate() == null) {

            throw new RuntimeException(
                    "Attendance date is required."
            );
        }


        if (
                request.getAttendance() == null
                        ||
                        request.getAttendance().isEmpty()
        ) {

            throw new RuntimeException(
                    "Attendance data is required."
            );
        }


        LocalDate attendanceDate =
                request.getAttendanceDate();


        List<Attendance> savedAttendance =
                new ArrayList<>();


        // PROCESS EVERY EMPLOYEE
        for (
                AttendanceSaveRequest.AttendanceItem item
                : request.getAttendance()
        ) {


            if (item.getEmployeeId() == null) {

                throw new RuntimeException(
                        "Employee ID is required."
                );

            }


            if (
                    item.getStatus() == null
                            ||
                            item.getStatus().isBlank()
            ) {

                throw new RuntimeException(
                        "Attendance status is required."
                );

            }


            // VALIDATE STATUS
            validateStatus(
                    item.getStatus()
            );

            // FIND EMPLOYEE
            Employee employee =
                    employeeRepository.findById(
                                    item.getEmployeeId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Employee not found with id: "
                                                    + item.getEmployeeId()
                                    )
                            );


            // CHECK EXISTING ATTENDANCE

            Attendance attendance =
                    attendanceRepository
                            .findByEmployeeIdAndAttendanceDate(
                                    item.getEmployeeId(),
                                    attendanceDate
                            )
                            .orElseGet(
                                    Attendance::new
                            );



            // SET DATA
            attendance.setEmployee(
                    employee
            );

            attendance.setAttendanceDate(
                    attendanceDate
            );

            attendance.setStatus(
                    item.getStatus()
            );

            attendance.setRemark(
                    item.getRemark()
            );



            // SAVE
            Attendance saved =
                    attendanceRepository.save(
                            attendance
                    );


            savedAttendance.add(saved);
        }


        return savedAttendance;
    }


    // ============================================================
    // GET MONTHLY ATTENDANCE
    // ============================================================

    public List<AttendanceResponse> getMonthlyAttendance(
            String month
    ) {

        LocalDate startDate =
                LocalDate.parse(
                        month + "-01"
                );


        LocalDate endDate =
                startDate
                        .withDayOfMonth(
                                startDate.lengthOfMonth()
                        );


        List<Attendance> attendanceList =
                attendanceRepository
                        .findByAttendanceDateBetween(
                                startDate,
                                endDate
                        );


        return attendanceList
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

//    public List<Attendance> getMonthlyAttendance(
//            String month
//    ) {
//
//        LocalDate startDate =
//                LocalDate.parse(
//                        month + "-01"
//                );
//
//
//        LocalDate endDate =
//                startDate
//                        .withDayOfMonth(
//                                startDate.lengthOfMonth()
//                        );
//
//
//        return attendanceRepository
//                .findByAttendanceDateBetween(
//                        startDate,
//                        endDate
//                );
//    }


    // ============================================================
    // GET EMPLOYEE MONTHLY ATTENDANCE
    // ============================================================

    // ============================================================
// GET EMPLOYEE MONTHLY ATTENDANCE
// ============================================================

    public List<AttendanceResponse> getEmployeeMonthlyAttendance(
            Long employeeId,
            String month
    ) {

        if (
                !employeeRepository.existsById(
                        employeeId
                )
        ) {

            throw new RuntimeException(
                    "Employee not found with id: "
                            + employeeId
            );
        }


        LocalDate startDate =
                LocalDate.parse(
                        month + "-01"
                );


        LocalDate endDate =
                startDate
                        .withDayOfMonth(
                                startDate.lengthOfMonth()
                        );


        List<Attendance> attendanceList =
                attendanceRepository
                        .findByEmployeeIdAndAttendanceDateBetween(
                                employeeId,
                                startDate,
                                endDate
                        );


        return attendanceList
                .stream()
                .map(this::convertToResponse)
                .toList();
    }
//    public List<Attendance> getEmployeeMonthlyAttendance(
//            Long employeeId,
//            String month
//    ) {
//
//        if (
//                !employeeRepository.existsById(
//                        employeeId
//                )
//        ) {
//
//            throw new RuntimeException(
//                    "Employee not found with id: "
//                            + employeeId
//            );
//        }
//
//
//        LocalDate startDate =
//                LocalDate.parse(
//                        month + "-01"
//                );
//
//
//        LocalDate endDate =
//                startDate
//                        .withDayOfMonth(
//                                startDate.lengthOfMonth()
//                        );
//
//
//        return attendanceRepository
//                .findByEmployeeIdAndAttendanceDateBetween(
//                        employeeId,
//                        startDate,
//                        endDate
//                );
//    }


    // ============================================================
    // GET ATTENDANCE BY ID
    // ============================================================

    public Attendance getAttendanceById(
            Long id
    ) {

        return attendanceRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Attendance not found with id: "
                                        + id
                        )
                );
    }


    // ============================================================
    // DELETE ATTENDANCE
    // ============================================================

    public void deleteAttendance(
            Long id
    ) {

        if (
                !attendanceRepository.existsById(
                        id
                )
        ) {

            throw new RuntimeException(
                    "Attendance not found with id: "
                            + id
            );
        }


        attendanceRepository.deleteById(id);
    }


    // ============================================================
    // VALIDATE ATTENDANCE STATUS
    // ============================================================

    private void validateStatus(
            String status
    ) {

        switch (status) {

            case "Present":
            case "Absent":
            case "Leave":
            case "Half Day":
            case "Holiday":
            case "Week Off":
                break;

            default:

                throw new RuntimeException(
                        "Invalid attendance status: "
                                + status
                );
        }
    }

    // ============================================================
// CONVERT ATTENDANCE ENTITY TO RESPONSE DTO
// ============================================================

    private AttendanceResponse convertToResponse(
            Attendance attendance
    ) {

        return new AttendanceResponse(
                attendance.getId(),
                attendance.getEmployee().getId(),
                attendance.getAttendanceDate(),
                attendance.getStatus(),
                attendance.getRemark()
        );
    }
}
