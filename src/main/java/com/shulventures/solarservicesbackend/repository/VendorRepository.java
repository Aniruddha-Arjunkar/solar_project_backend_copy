package com.shulventures.solarservicesbackend.repository;

import com.shulventures.solarservicesbackend.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository
        extends JpaRepository<Vendor, Long> {
}
