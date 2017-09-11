package com.home.app.repository;

import com.home.app.domain.BarcodeReqLocDet;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BarcodeReqLocDet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BarcodeReqLocDetRepository extends JpaRepository<BarcodeReqLocDet,Long> {
    
}
