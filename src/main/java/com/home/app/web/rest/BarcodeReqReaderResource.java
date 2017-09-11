package com.home.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.home.app.domain.BarcodeReqReader;

import com.home.app.repository.BarcodeReqReaderRepository;
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
 * REST controller for managing BarcodeReqReader.
 */
@RestController
@RequestMapping("/api")
public class BarcodeReqReaderResource {

    private final Logger log = LoggerFactory.getLogger(BarcodeReqReaderResource.class);

    private static final String ENTITY_NAME = "barcodeReqReader";

    private final BarcodeReqReaderRepository barcodeReqReaderRepository;

    public BarcodeReqReaderResource(BarcodeReqReaderRepository barcodeReqReaderRepository) {
        this.barcodeReqReaderRepository = barcodeReqReaderRepository;
    }

    /**
     * POST  /barcode-req-readers : Create a new barcodeReqReader.
     *
     * @param barcodeReqReader the barcodeReqReader to create
     * @return the ResponseEntity with status 201 (Created) and with body the new barcodeReqReader, or with status 400 (Bad Request) if the barcodeReqReader has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/barcode-req-readers")
    @Timed
    public ResponseEntity<BarcodeReqReader> createBarcodeReqReader(@RequestBody BarcodeReqReader barcodeReqReader) throws URISyntaxException {
        log.debug("REST request to save BarcodeReqReader : {}", barcodeReqReader);
        if (barcodeReqReader.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new barcodeReqReader cannot already have an ID")).body(null);
        }
        BarcodeReqReader result = barcodeReqReaderRepository.save(barcodeReqReader);
        return ResponseEntity.created(new URI("/api/barcode-req-readers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /barcode-req-readers : Updates an existing barcodeReqReader.
     *
     * @param barcodeReqReader the barcodeReqReader to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated barcodeReqReader,
     * or with status 400 (Bad Request) if the barcodeReqReader is not valid,
     * or with status 500 (Internal Server Error) if the barcodeReqReader couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/barcode-req-readers")
    @Timed
    public ResponseEntity<BarcodeReqReader> updateBarcodeReqReader(@RequestBody BarcodeReqReader barcodeReqReader) throws URISyntaxException {
        log.debug("REST request to update BarcodeReqReader : {}", barcodeReqReader);
        if (barcodeReqReader.getId() == null) {
            return createBarcodeReqReader(barcodeReqReader);
        }
        BarcodeReqReader result = barcodeReqReaderRepository.save(barcodeReqReader);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, barcodeReqReader.getId().toString()))
            .body(result);
    }

    /**
     * GET  /barcode-req-readers : get all the barcodeReqReaders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of barcodeReqReaders in body
     */
    @GetMapping("/barcode-req-readers")
    @Timed
    public List<BarcodeReqReader> getAllBarcodeReqReaders() {
        log.debug("REST request to get all BarcodeReqReaders");
        return barcodeReqReaderRepository.findAll();
    }

    /**
     * GET  /barcode-req-readers/:id : get the "id" barcodeReqReader.
     *
     * @param id the id of the barcodeReqReader to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the barcodeReqReader, or with status 404 (Not Found)
     */
    @GetMapping("/barcode-req-readers/{id}")
    @Timed
    public ResponseEntity<BarcodeReqReader> getBarcodeReqReader(@PathVariable Long id) {
        log.debug("REST request to get BarcodeReqReader : {}", id);
        BarcodeReqReader barcodeReqReader = barcodeReqReaderRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(barcodeReqReader));
    }

    /**
     * DELETE  /barcode-req-readers/:id : delete the "id" barcodeReqReader.
     *
     * @param id the id of the barcodeReqReader to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/barcode-req-readers/{id}")
    @Timed
    public ResponseEntity<Void> deleteBarcodeReqReader(@PathVariable Long id) {
        log.debug("REST request to delete BarcodeReqReader : {}", id);
        barcodeReqReaderRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
