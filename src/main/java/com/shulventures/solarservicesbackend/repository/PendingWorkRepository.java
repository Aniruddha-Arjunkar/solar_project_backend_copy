package com.shulventures.solarservicesbackend.repository;


import com.shulventures.solarservicesbackend.entity.PendingWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingWorkRepository
        extends JpaRepository<PendingWork, Long> {

    // Get all work belonging to a particular Client
    List<PendingWork> findByClientId(Long clientId);


    // Get pending work
    List<PendingWork> findByStatus(String status);
}
