package com.shulventures.solarservicesbackend.repository;

import com.shulventures.solarservicesbackend.entity.ClientDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientDocumentRepository
        extends JpaRepository<ClientDocument, Long> {

    // GET ALL DOCUMENTS OF A CLIENT
    List<ClientDocument> findByClientId(Long clientId);

}
