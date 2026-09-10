package com.shulventures.solarservicesbackend.repository;

import com.shulventures.solarservicesbackend.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {

    // GET SALARY BY EMPLOYEE
    List<Salary> findByEmployeeId(Long employeeId);

    // GET SALARY BY EMPLOYEE + MONTH
    Optional<Salary> findByEmployeeIdAndMonth(
            Long employeeId,
            String month
    );
}
