package com.shulventures.solarservicesbackend.repository;

import com.shulventures.solarservicesbackend.entity.EmployeeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDocumentRepository
        extends JpaRepository<EmployeeDocument, Long> {

    // ================= GET ALL DOCUMENTS OF AN EMPLOYEE ======================

    List<EmployeeDocument> findByEmployeeId(
            Long employeeId
    );
}
