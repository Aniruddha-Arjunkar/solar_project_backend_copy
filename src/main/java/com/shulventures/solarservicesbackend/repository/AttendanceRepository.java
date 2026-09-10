package com.shulventures.solarservicesbackend.repository;

import com.shulventures.solarservicesbackend.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {



    // FIND ATTENDANCE FOR ONE EMPLOYEE ON ONE DATE
    Optional<Attendance> findByEmployeeIdAndAttendanceDate(
            Long employeeId,
            LocalDate attendanceDate
    );


    // FIND ATTENDANCE BETWEEN TWO DATES
    List<Attendance> findByAttendanceDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );



    // FIND ONE EMPLOYEE'S ATTENDANCE BETWEEN TWO DATES
    List<Attendance> findByEmployeeIdAndAttendanceDateBetween(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate
    );
}
