package com.home.app.repository;

import com.home.app.domain.BarcodeReqLocation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BarcodeReqLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BarcodeReqLocationRepository extends JpaRepository<BarcodeReqLocation,Long> {
    
}
