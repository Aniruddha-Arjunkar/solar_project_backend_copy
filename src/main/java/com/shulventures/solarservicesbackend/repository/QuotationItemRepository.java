package com.shulventures.solarservicesbackend.repository;

import com.shulventures.solarservicesbackend.entity.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationItemRepository extends JpaRepository<QuotationItem, Long> {
}