package com.shulventures.solarservicesbackend.repository;

import com.shulventures.solarservicesbackend.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    List<Quotation> findByLeadId(Long leadId);

    void deleteByLeadId(Long leadId);
}
