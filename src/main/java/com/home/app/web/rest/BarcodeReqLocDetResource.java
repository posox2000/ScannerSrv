package com.home.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.home.app.domain.BarcodeReqLocDet;

import com.home.app.repository.BarcodeReqLocDetRepository;
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
 * REST controller for managing BarcodeReqLocDet.
 */
@RestController
@RequestMapping("/api")
public class BarcodeReqLocDetResource {

    private final Logger log = LoggerFactory.getLogger(BarcodeReqLocDetResource.class);

    private static final String ENTITY_NAME = "barcodeReqLocDet";

    private final BarcodeReqLocDetRepository barcodeReqLocDetRepository;

    public BarcodeReqLocDetResource(BarcodeReqLocDetRepository barcodeReqLocDetRepository) {
        this.barcodeReqLocDetRepository = barcodeReqLocDetRepository;
    }

    /**
     * POST  /barcode-req-loc-dets : Create a new barcodeReqLocDet.
     *
     * @param barcodeReqLocDet the barcodeReqLocDet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new barcodeReqLocDet, or with status 400 (Bad Request) if the barcodeReqLocDet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/barcode-req-loc-dets")
    @Timed
    public ResponseEntity<BarcodeReqLocDet> createBarcodeReqLocDet(@RequestBody BarcodeReqLocDet barcodeReqLocDet) throws URISyntaxException {
        log.debug("REST request to save BarcodeReqLocDet : {}", barcodeReqLocDet);
        if (barcodeReqLocDet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new barcodeReqLocDet cannot already have an ID")).body(null);
        }
        BarcodeReqLocDet result = barcodeReqLocDetRepository.save(barcodeReqLocDet);
        return ResponseEntity.created(new URI("/api/barcode-req-loc-dets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /barcode-req-loc-dets : Updates an existing barcodeReqLocDet.
     *
     * @param barcodeReqLocDet the barcodeReqLocDet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated barcodeReqLocDet,
     * or with status 400 (Bad Request) if the barcodeReqLocDet is not valid,
     * or with status 500 (Internal Server Error) if the barcodeReqLocDet couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/barcode-req-loc-dets")
    @Timed
    public ResponseEntity<BarcodeReqLocDet> updateBarcodeReqLocDet(@RequestBody BarcodeReqLocDet barcodeReqLocDet) throws URISyntaxException {
        log.debug("REST request to update BarcodeReqLocDet : {}", barcodeReqLocDet);
        if (barcodeReqLocDet.getId() == null) {
            return createBarcodeReqLocDet(barcodeReqLocDet);
        }
        BarcodeReqLocDet result = barcodeReqLocDetRepository.save(barcodeReqLocDet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, barcodeReqLocDet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /barcode-req-loc-dets : get all the barcodeReqLocDets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of barcodeReqLocDets in body
     */
    @GetMapping("/barcode-req-loc-dets")
    @Timed
    public List<BarcodeReqLocDet> getAllBarcodeReqLocDets() {
        log.debug("REST request to get all BarcodeReqLocDets");
        return barcodeReqLocDetRepository.findAll();
    }

    /**
     * GET  /barcode-req-loc-dets/:id : get the "id" barcodeReqLocDet.
     *
     * @param id the id of the barcodeReqLocDet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the barcodeReqLocDet, or with status 404 (Not Found)
     */
    @GetMapping("/barcode-req-loc-dets/{id}")
    @Timed
    public ResponseEntity<BarcodeReqLocDet> getBarcodeReqLocDet(@PathVariable Long id) {
        log.debug("REST request to get BarcodeReqLocDet : {}", id);
        BarcodeReqLocDet barcodeReqLocDet = barcodeReqLocDetRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(barcodeReqLocDet));
    }

    /**
     * DELETE  /barcode-req-loc-dets/:id : delete the "id" barcodeReqLocDet.
     *
     * @param id the id of the barcodeReqLocDet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/barcode-req-loc-dets/{id}")
    @Timed
    public ResponseEntity<Void> deleteBarcodeReqLocDet(@PathVariable Long id) {
        log.debug("REST request to delete BarcodeReqLocDet : {}", id);
        barcodeReqLocDetRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
