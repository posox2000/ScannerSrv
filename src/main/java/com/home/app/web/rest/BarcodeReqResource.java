package com.home.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.home.app.domain.BarcodeReq;

import com.home.app.repository.BarcodeReqRepository;
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
 * REST controller for managing BarcodeReq.
 */
@RestController
@RequestMapping("/api")
public class BarcodeReqResource {

    private final Logger log = LoggerFactory.getLogger(BarcodeReqResource.class);

    private static final String ENTITY_NAME = "barcodeReq";

    private final BarcodeReqRepository barcodeReqRepository;

    public BarcodeReqResource(BarcodeReqRepository barcodeReqRepository) {
        this.barcodeReqRepository = barcodeReqRepository;
    }

    /**
     * POST  /barcode-reqs : Create a new barcodeReq.
     *
     * @param barcodeReq the barcodeReq to create
     * @return the ResponseEntity with status 201 (Created) and with body the new barcodeReq, or with status 400 (Bad Request) if the barcodeReq has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/barcode-reqs")
    @Timed
    public ResponseEntity<BarcodeReq> createBarcodeReq(@RequestBody BarcodeReq barcodeReq) throws URISyntaxException {
        log.debug("REST request to save BarcodeReq : {}", barcodeReq);
        if (barcodeReq.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new barcodeReq cannot already have an ID")).body(null);
        }
        BarcodeReq result = barcodeReqRepository.save(barcodeReq);
        return ResponseEntity.created(new URI("/api/barcode-reqs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /barcode-reqs : Updates an existing barcodeReq.
     *
     * @param barcodeReq the barcodeReq to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated barcodeReq,
     * or with status 400 (Bad Request) if the barcodeReq is not valid,
     * or with status 500 (Internal Server Error) if the barcodeReq couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/barcode-reqs")
    @Timed
    public ResponseEntity<BarcodeReq> updateBarcodeReq(@RequestBody BarcodeReq barcodeReq) throws URISyntaxException {
        log.debug("REST request to update BarcodeReq : {}", barcodeReq);
        if (barcodeReq.getId() == null) {
            return createBarcodeReq(barcodeReq);
        }
        BarcodeReq result = barcodeReqRepository.save(barcodeReq);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, barcodeReq.getId().toString()))
            .body(result);
    }

    /**
     * GET  /barcode-reqs : get all the barcodeReqs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of barcodeReqs in body
     */
    @GetMapping("/barcode-reqs")
    @Timed
    public List<BarcodeReq> getAllBarcodeReqs() {
        log.debug("REST request to get all BarcodeReqs");
        return barcodeReqRepository.findAll();
    }

    /**
     * GET  /barcode-reqs/:id : get the "id" barcodeReq.
     *
     * @param id the id of the barcodeReq to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the barcodeReq, or with status 404 (Not Found)
     */
    @GetMapping("/barcode-reqs/{id}")
    @Timed
    public ResponseEntity<BarcodeReq> getBarcodeReq(@PathVariable Long id) {
        log.debug("REST request to get BarcodeReq : {}", id);
        BarcodeReq barcodeReq = barcodeReqRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(barcodeReq));
    }

    /**
     * DELETE  /barcode-reqs/:id : delete the "id" barcodeReq.
     *
     * @param id the id of the barcodeReq to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/barcode-reqs/{id}")
    @Timed
    public ResponseEntity<Void> deleteBarcodeReq(@PathVariable Long id) {
        log.debug("REST request to delete BarcodeReq : {}", id);
        barcodeReqRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
