package com.home.app.repository;

import com.home.app.domain.BarcodeReqFile;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BarcodeReqFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BarcodeReqFileRepository extends JpaRepository<BarcodeReqFile,Long> {
    
}
