package com.home.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.home.app.domain.BarcodeReq;
import com.home.app.domain.BarcodeReqFile;
import com.home.app.domain.BarcodeReqLocation;
import com.home.app.domain.BarcodeReqReader;
import com.home.app.domain.enumeration.BarcodeReqType;
import com.home.app.repository.BarcodeReqFileRepository;
import com.home.app.repository.BarcodeReqLocationRepository;
import com.home.app.repository.BarcodeReqReaderRepository;
import com.home.app.repository.BarcodeReqRepository;
import com.home.app.service.dto.ScannerReqDTO;
import com.home.app.service.dto.ScannerReqFile;
import com.home.app.service.dto.ScannerReqReader;
import com.home.app.service.dto.ScannerReqSection;
import com.home.app.web.rest.util.HeaderUtil;
import com.home.app.worker.BarcodeReqFileValidator;

@RestController
@RequestMapping("/api")
public class ScannerResource {
    private final Logger log = LoggerFactory.getLogger(BarcodeReqResource.class);
    private final BarcodeReqRepository barcodeReqRepository;
    private final BarcodeReqReaderRepository barcodeReqReaderRepository;
    private final BarcodeReqLocationRepository barcodeReqLocationRepository;
    private final BarcodeReqFileRepository barcodeReqFileRepository;
    
    public ScannerResource(BarcodeReqRepository barcodeReqRepository, 
    		BarcodeReqReaderRepository barcodeReqReaderRepository,
    		BarcodeReqLocationRepository barcodeReqLocationRepository,
    		BarcodeReqFileRepository barcodeReqFileRepository){
    	this.barcodeReqRepository=barcodeReqRepository;
    	this.barcodeReqReaderRepository = barcodeReqReaderRepository;
    	this.barcodeReqLocationRepository = barcodeReqLocationRepository;
    	this.barcodeReqFileRepository = barcodeReqFileRepository;
    }
    /**
     * POST  /scanner-requests : Create a new scannerRequests.
     *
     * @param scannerReq the barcodeReq to create
     * @return the ResponseEntity with status 201 (Created) and with body the new barcodeReq, or with status 400 (Bad Request) if the barcodeReq has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/scanner-requests")
    @Timed
    public ResponseEntity<String> createBarcodeReq(@RequestBody ScannerReqDTO scannerReq) throws URISyntaxException {
        log.debug("REST request to save ScannerReqDTO : {}", scannerReq);
        
        //step ONE
        final BarcodeReq resultReq = new BarcodeReq();
        try
        {
        	resultReq.setGovid(scannerReq.getUserId());
        	resultReq.setOfficeId(scannerReq.getOfficeId());
        	resultReq.setRequestDate(scannerReq.getDate());
        }
        finally{
        	barcodeReqRepository.save(resultReq);
        }
        
        //step TWO
        for(ScannerReqReader reader : scannerReq.getReaders()){
        	final BarcodeReqReader resultReqReader = new BarcodeReqReader();
        	try{
        		resultReqReader.setReaderName(reader.getReaderId());
        		resultReqReader.setReq(resultReq);
        	}
        	finally{
        		barcodeReqReaderRepository.saveAndFlush(resultReqReader);
        	}
        	
        	//step THREE
        	for(ScannerReqSection section: reader.getSectionRPCs()){
        		final BarcodeReqLocation loc = new BarcodeReqLocation();
        		try{
        			loc.setSectionRpc(section.getSectionRPC());
        			loc.setReqType(BarcodeReqType.AUDIT);
        			loc.setBarcodeReqReader(resultReqReader);
        		}
        		finally{
        			barcodeReqLocationRepository.saveAndFlush(loc);
        		}
        		
        		//step FOUR
        		final BarcodeReqFile resultFile = new BarcodeReqFile();
        		for(ScannerReqFile file: section.getFiles()){
        			try{
	        			resultFile.setFileNumber(file.getFile());
	        			resultFile.setLoc(loc);
	    			}
	        		finally{
	        			barcodeReqFileRepository.saveAndFlush(resultFile);
	        		}
        		}
        	}
        }
        //spin file validation process
        Thread trValidate = new Thread(new BarcodeReqFileValidator());
        trValidate.start();
        
        return ResponseEntity.created(new URI("/api/scanner-requests/"))
            .headers(HeaderUtil.createEntityCreationAlert("ScannerReq", ""))
            .body("test-post");
    }
    /**
     * GET  /scanner-requests : Get a list scannerRequests.
     */
    @GetMapping("/scanner-requests")
    @Timed
    public ResponseEntity<String> getBarcodeReq() throws URISyntaxException {
        log.debug("REST request to get ScannerReqDTO : {}");
        
        return ResponseEntity.created(new URI("/api/scanner-requests/"))
            .headers(HeaderUtil.createEntityCreationAlert("ScannerReq", ""))
            .body("test-get");
    }
}
