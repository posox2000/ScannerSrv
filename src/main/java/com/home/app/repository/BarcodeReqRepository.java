package com.home.app.repository;

import com.home.app.domain.BarcodeReq;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BarcodeReq entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BarcodeReqRepository extends JpaRepository<BarcodeReq,Long> {
    
}
