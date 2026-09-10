package com.shulventures.solarservicesbackend.controller;

import com.shulventures.solarservicesbackend.dto.AttendanceSaveRequest;
import com.shulventures.solarservicesbackend.entity.Attendance;
import com.shulventures.solarservicesbackend.service.AttendanceService;
import org.springframework.http.HttpStatus;
import com.shulventures.solarservicesbackend.dto.AttendanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:5173")
public class AttendanceController {

    private final AttendanceService attendanceService;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public AttendanceController(
            AttendanceService attendanceService
    ) {
        this.attendanceService =
                attendanceService;
    }


    // ============================================================
    // SAVE ATTENDANCE
    // ============================================================

    @PostMapping
    public ResponseEntity<List<Attendance>> saveAttendance(
            @RequestBody AttendanceSaveRequest request
    ) {

        List<Attendance> savedAttendance =
                attendanceService.saveAttendance(
                        request
                );


        return new ResponseEntity<>(
                savedAttendance,
                HttpStatus.CREATED
        );
    }


    // ============================================================
    // GET MONTHLY ATTENDANCE
    // ============================================================

    @GetMapping("/month/{month}")
    public ResponseEntity<List<AttendanceResponse>>
    getMonthlyAttendance(
            @PathVariable String month
    ) {

        List<AttendanceResponse> attendance =
                attendanceService.getMonthlyAttendance(
                        month
                );

        return ResponseEntity.ok(
                attendance
        );
    }

//    @GetMapping("/month/{month}")
//    public ResponseEntity<List<Attendance>> getMonthlyAttendance(
//            @PathVariable String month
//    ) {
//
//        List<Attendance> attendance =
//                attendanceService.getMonthlyAttendance(
//                        month
//                );
//
//
//        return ResponseEntity.ok(
//                attendance
//        );
//    }


    // ============================================================
    // GET EMPLOYEE MONTHLY ATTENDANCE
    // ============================================================

    @GetMapping(
            "/employee/{employeeId}/month/{month}"
    )
    public ResponseEntity<List<AttendanceResponse>>
    getEmployeeMonthlyAttendance(
            @PathVariable Long employeeId,
            @PathVariable String month
    ) {

        List<AttendanceResponse> attendance =
                attendanceService
                        .getEmployeeMonthlyAttendance(
                                employeeId,
                                month
                        );

        return ResponseEntity.ok(
                attendance
        );
    }

//    @GetMapping(
//            "/employee/{employeeId}/month/{month}"
//    )
//    public ResponseEntity<List<Attendance>>
//    getEmployeeMonthlyAttendance(
//            @PathVariable Long employeeId,
//            @PathVariable String month
//    ) {
//
//        List<Attendance> attendance =
//                attendanceService
//                        .getEmployeeMonthlyAttendance(
//                                employeeId,
//                                month
//                        );
//
//
//        return ResponseEntity.ok(
//                attendance
//        );
//    }


    // ============================================================
    // GET ATTENDANCE BY ID
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<Attendance>
    getAttendanceById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                attendanceService
                        .getAttendanceById(id)
        );
    }

    // ============================================================
    // DELETE ATTENDANCE
    // ============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(
            @PathVariable Long id
    ) {
        attendanceService.deleteAttendance(
                id
        );
        return ResponseEntity.noContent()
                .build();
    }
}
