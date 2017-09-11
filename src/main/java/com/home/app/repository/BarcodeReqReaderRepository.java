package com.home.app.repository;

import com.home.app.domain.BarcodeReqReader;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BarcodeReqReader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BarcodeReqReaderRepository extends JpaRepository<BarcodeReqReader,Long> {
    
}
