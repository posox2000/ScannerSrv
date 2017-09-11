package com.home.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.home.app.domain.BarcodeReqLocation;

import com.home.app.repository.BarcodeReqLocationRepository;
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
 * REST controller for managing BarcodeReqLocation.
 */
@RestController
@RequestMapping("/api")
public class BarcodeReqLocationResource {

    private final Logger log = LoggerFactory.getLogger(BarcodeReqLocationResource.class);

    private static final String ENTITY_NAME = "barcodeReqLocation";

    private final BarcodeReqLocationRepository barcodeReqLocationRepository;

    public BarcodeReqLocationResource(BarcodeReqLocationRepository barcodeReqLocationRepository) {
        this.barcodeReqLocationRepository = barcodeReqLocationRepository;
    }

    /**
     * POST  /barcode-req-locations : Create a new barcodeReqLocation.
     *
     * @param barcodeReqLocation the barcodeReqLocation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new barcodeReqLocation, or with status 400 (Bad Request) if the barcodeReqLocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/barcode-req-locations")
    @Timed
    public ResponseEntity<BarcodeReqLocation> createBarcodeReqLocation(@RequestBody BarcodeReqLocation barcodeReqLocation) throws URISyntaxException {
        log.debug("REST request to save BarcodeReqLocation : {}", barcodeReqLocation);
        if (barcodeReqLocation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new barcodeReqLocation cannot already have an ID")).body(null);
        }
        BarcodeReqLocation result = barcodeReqLocationRepository.save(barcodeReqLocation);
        return ResponseEntity.created(new URI("/api/barcode-req-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /barcode-req-locations : Updates an existing barcodeReqLocation.
     *
     * @param barcodeReqLocation the barcodeReqLocation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated barcodeReqLocation,
     * or with status 400 (Bad Request) if the barcodeReqLocation is not valid,
     * or with status 500 (Internal Server Error) if the barcodeReqLocation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/barcode-req-locations")
    @Timed
    public ResponseEntity<BarcodeReqLocation> updateBarcodeReqLocation(@RequestBody BarcodeReqLocation barcodeReqLocation) throws URISyntaxException {
        log.debug("REST request to update BarcodeReqLocation : {}", barcodeReqLocation);
        if (barcodeReqLocation.getId() == null) {
            return createBarcodeReqLocation(barcodeReqLocation);
        }
        BarcodeReqLocation result = barcodeReqLocationRepository.save(barcodeReqLocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, barcodeReqLocation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /barcode-req-locations : get all the barcodeReqLocations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of barcodeReqLocations in body
     */
    @GetMapping("/barcode-req-locations")
    @Timed
    public List<BarcodeReqLocation> getAllBarcodeReqLocations() {
        log.debug("REST request to get all BarcodeReqLocations");
        return barcodeReqLocationRepository.findAll();
    }

    /**
     * GET  /barcode-req-locations/:id : get the "id" barcodeReqLocation.
     *
     * @param id the id of the barcodeReqLocation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the barcodeReqLocation, or with status 404 (Not Found)
     */
    @GetMapping("/barcode-req-locations/{id}")
    @Timed
    public ResponseEntity<BarcodeReqLocation> getBarcodeReqLocation(@PathVariable Long id) {
        log.debug("REST request to get BarcodeReqLocation : {}", id);
        BarcodeReqLocation barcodeReqLocation = barcodeReqLocationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(barcodeReqLocation));
    }

    /**
     * DELETE  /barcode-req-locations/:id : delete the "id" barcodeReqLocation.
     *
     * @param id the id of the barcodeReqLocation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/barcode-req-locations/{id}")
    @Timed
    public ResponseEntity<Void> deleteBarcodeReqLocation(@PathVariable Long id) {
        log.debug("REST request to delete BarcodeReqLocation : {}", id);
        barcodeReqLocationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
