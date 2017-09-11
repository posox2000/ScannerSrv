package com.home.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.home.app.domain.BarcodeReqFile;

import com.home.app.repository.BarcodeReqFileRepository;
import com.home.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BarcodeReqFile.
 */
@RestController
@RequestMapping("/api")
public class BarcodeReqFileResource {

    private final Logger log = LoggerFactory.getLogger(BarcodeReqFileResource.class);

    private static final String ENTITY_NAME = "barcodeReqFile";

    private final BarcodeReqFileRepository barcodeReqFileRepository;

    public BarcodeReqFileResource(BarcodeReqFileRepository barcodeReqFileRepository) {
        this.barcodeReqFileRepository = barcodeReqFileRepository;
    }

    /**
     * POST  /barcode-req-files : Create a new barcodeReqFile.
     *
     * @param barcodeReqFile the barcodeReqFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new barcodeReqFile, or with status 400 (Bad Request) if the barcodeReqFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/barcode-req-files")
    @Timed
    public ResponseEntity<BarcodeReqFile> createBarcodeReqFile(@RequestBody BarcodeReqFile barcodeReqFile) throws URISyntaxException {
        log.debug("REST request to save BarcodeReqFile : {}", barcodeReqFile);
        if (barcodeReqFile.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new barcodeReqFile cannot already have an ID")).body(null);
        }
        BarcodeReqFile result = barcodeReqFileRepository.save(barcodeReqFile);
        return ResponseEntity.created(new URI("/api/barcode-req-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /barcode-req-files : Updates an existing barcodeReqFile.
     *
     * @param barcodeReqFile the barcodeReqFile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated barcodeReqFile,
     * or with status 400 (Bad Request) if the barcodeReqFile is not valid,
     * or with status 500 (Internal Server Error) if the barcodeReqFile couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/barcode-req-files")
    @Timed
    public ResponseEntity<BarcodeReqFile> updateBarcodeReqFile(@RequestBody BarcodeReqFile barcodeReqFile) throws URISyntaxException {
        log.debug("REST request to update BarcodeReqFile : {}", barcodeReqFile);
        if (barcodeReqFile.getId() == null) {
            return createBarcodeReqFile(barcodeReqFile);
        }
        BarcodeReqFile result = barcodeReqFileRepository.save(barcodeReqFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, barcodeReqFile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /barcode-req-files : get all the barcodeReqFiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of barcodeReqFiles in body
     */
    @GetMapping("/barcode-req-files")
    @Timed
    public List<BarcodeReqFile> getAllBarcodeReqFiles() {
        log.debug("REST request to get all BarcodeReqFiles");
        return barcodeReqFileRepository.findAll();
    }

    /**
     * GET  /barcode-req-files/:id : get the "id" barcodeReqFile.
     *
     * @param id the id of the barcodeReqFile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the barcodeReqFile, or with status 404 (Not Found)
     */
    @GetMapping("/barcode-req-files/{id}")
    @Timed
    public ResponseEntity<BarcodeReqFile> getBarcodeReqFile(@PathVariable Long id) {
        log.debug("REST request to get BarcodeReqFile : {}", id);
        BarcodeReqFile barcodeReqFile = barcodeReqFileRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(barcodeReqFile));
    }

    /**
     * DELETE  /barcode-req-files/:id : delete the "id" barcodeReqFile.
     *
     * @param id the id of the barcodeReqFile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/barcode-req-files/{id}")
    @Timed
    public ResponseEntity<Void> deleteBarcodeReqFile(@PathVariable Long id) {
        log.debug("REST request to delete BarcodeReqFile : {}", id);
        barcodeReqFileRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
