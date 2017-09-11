package com.home.app.web.rest;

import com.home.app.ScannerSrvApp;

import com.home.app.domain.BarcodeReqLocation;
import com.home.app.repository.BarcodeReqLocationRepository;
import com.home.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.home.app.domain.enumeration.BarcodeReqType;
/**
 * Test class for the BarcodeReqLocationResource REST controller.
 *
 * @see BarcodeReqLocationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScannerSrvApp.class)
public class BarcodeReqLocationResourceIntTest {

    private static final String DEFAULT_SECTION_RPC = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_RPC = "BBBBBBBBBB";

    private static final BarcodeReqType DEFAULT_REQ_TYPE = BarcodeReqType.AUDIT;
    private static final BarcodeReqType UPDATED_REQ_TYPE = BarcodeReqType.RECEIVE;

    @Autowired
    private BarcodeReqLocationRepository barcodeReqLocationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBarcodeReqLocationMockMvc;

    private BarcodeReqLocation barcodeReqLocation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BarcodeReqLocationResource barcodeReqLocationResource = new BarcodeReqLocationResource(barcodeReqLocationRepository);
        this.restBarcodeReqLocationMockMvc = MockMvcBuilders.standaloneSetup(barcodeReqLocationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BarcodeReqLocation createEntity(EntityManager em) {
        BarcodeReqLocation barcodeReqLocation = new BarcodeReqLocation()
            .sectionRpc(DEFAULT_SECTION_RPC)
            .reqType(DEFAULT_REQ_TYPE);
        return barcodeReqLocation;
    }

    @Before
    public void initTest() {
        barcodeReqLocation = createEntity(em);
    }

    @Test
    @Transactional
    public void createBarcodeReqLocation() throws Exception {
        int databaseSizeBeforeCreate = barcodeReqLocationRepository.findAll().size();

        // Create the BarcodeReqLocation
        restBarcodeReqLocationMockMvc.perform(post("/api/barcode-req-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReqLocation)))
            .andExpect(status().isCreated());

        // Validate the BarcodeReqLocation in the database
        List<BarcodeReqLocation> barcodeReqLocationList = barcodeReqLocationRepository.findAll();
        assertThat(barcodeReqLocationList).hasSize(databaseSizeBeforeCreate + 1);
        BarcodeReqLocation testBarcodeReqLocation = barcodeReqLocationList.get(barcodeReqLocationList.size() - 1);
        assertThat(testBarcodeReqLocation.getSectionRpc()).isEqualTo(DEFAULT_SECTION_RPC);
        assertThat(testBarcodeReqLocation.getReqType()).isEqualTo(DEFAULT_REQ_TYPE);
    }

    @Test
    @Transactional
    public void createBarcodeReqLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = barcodeReqLocationRepository.findAll().size();

        // Create the BarcodeReqLocation with an existing ID
        barcodeReqLocation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBarcodeReqLocationMockMvc.perform(post("/api/barcode-req-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReqLocation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BarcodeReqLocation> barcodeReqLocationList = barcodeReqLocationRepository.findAll();
        assertThat(barcodeReqLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBarcodeReqLocations() throws Exception {
        // Initialize the database
        barcodeReqLocationRepository.saveAndFlush(barcodeReqLocation);

        // Get all the barcodeReqLocationList
        restBarcodeReqLocationMockMvc.perform(get("/api/barcode-req-locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(barcodeReqLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionRpc").value(hasItem(DEFAULT_SECTION_RPC.toString())))
            .andExpect(jsonPath("$.[*].reqType").value(hasItem(DEFAULT_REQ_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getBarcodeReqLocation() throws Exception {
        // Initialize the database
        barcodeReqLocationRepository.saveAndFlush(barcodeReqLocation);

        // Get the barcodeReqLocation
        restBarcodeReqLocationMockMvc.perform(get("/api/barcode-req-locations/{id}", barcodeReqLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(barcodeReqLocation.getId().intValue()))
            .andExpect(jsonPath("$.sectionRpc").value(DEFAULT_SECTION_RPC.toString()))
            .andExpect(jsonPath("$.reqType").value(DEFAULT_REQ_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBarcodeReqLocation() throws Exception {
        // Get the barcodeReqLocation
        restBarcodeReqLocationMockMvc.perform(get("/api/barcode-req-locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBarcodeReqLocation() throws Exception {
        // Initialize the database
        barcodeReqLocationRepository.saveAndFlush(barcodeReqLocation);
        int databaseSizeBeforeUpdate = barcodeReqLocationRepository.findAll().size();

        // Update the barcodeReqLocation
        BarcodeReqLocation updatedBarcodeReqLocation = barcodeReqLocationRepository.findOne(barcodeReqLocation.getId());
        updatedBarcodeReqLocation
            .sectionRpc(UPDATED_SECTION_RPC)
            .reqType(UPDATED_REQ_TYPE);

        restBarcodeReqLocationMockMvc.perform(put("/api/barcode-req-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBarcodeReqLocation)))
            .andExpect(status().isOk());

        // Validate the BarcodeReqLocation in the database
        List<BarcodeReqLocation> barcodeReqLocationList = barcodeReqLocationRepository.findAll();
        assertThat(barcodeReqLocationList).hasSize(databaseSizeBeforeUpdate);
        BarcodeReqLocation testBarcodeReqLocation = barcodeReqLocationList.get(barcodeReqLocationList.size() - 1);
        assertThat(testBarcodeReqLocation.getSectionRpc()).isEqualTo(UPDATED_SECTION_RPC);
        assertThat(testBarcodeReqLocation.getReqType()).isEqualTo(UPDATED_REQ_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingBarcodeReqLocation() throws Exception {
        int databaseSizeBeforeUpdate = barcodeReqLocationRepository.findAll().size();

        // Create the BarcodeReqLocation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBarcodeReqLocationMockMvc.perform(put("/api/barcode-req-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReqLocation)))
            .andExpect(status().isCreated());

        // Validate the BarcodeReqLocation in the database
        List<BarcodeReqLocation> barcodeReqLocationList = barcodeReqLocationRepository.findAll();
        assertThat(barcodeReqLocationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBarcodeReqLocation() throws Exception {
        // Initialize the database
        barcodeReqLocationRepository.saveAndFlush(barcodeReqLocation);
        int databaseSizeBeforeDelete = barcodeReqLocationRepository.findAll().size();

        // Get the barcodeReqLocation
        restBarcodeReqLocationMockMvc.perform(delete("/api/barcode-req-locations/{id}", barcodeReqLocation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BarcodeReqLocation> barcodeReqLocationList = barcodeReqLocationRepository.findAll();
        assertThat(barcodeReqLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BarcodeReqLocation.class);
        BarcodeReqLocation barcodeReqLocation1 = new BarcodeReqLocation();
        barcodeReqLocation1.setId(1L);
        BarcodeReqLocation barcodeReqLocation2 = new BarcodeReqLocation();
        barcodeReqLocation2.setId(barcodeReqLocation1.getId());
        assertThat(barcodeReqLocation1).isEqualTo(barcodeReqLocation2);
        barcodeReqLocation2.setId(2L);
        assertThat(barcodeReqLocation1).isNotEqualTo(barcodeReqLocation2);
        barcodeReqLocation1.setId(null);
        assertThat(barcodeReqLocation1).isNotEqualTo(barcodeReqLocation2);
    }
}
