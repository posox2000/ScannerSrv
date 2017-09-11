package com.home.app.web.rest;

import com.home.app.ScannerSrvApp;

import com.home.app.domain.BarcodeReqLocDet;
import com.home.app.repository.BarcodeReqLocDetRepository;
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

/**
 * Test class for the BarcodeReqLocDetResource REST controller.
 *
 * @see BarcodeReqLocDetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScannerSrvApp.class)
public class BarcodeReqLocDetResourceIntTest {

    private static final String DEFAULT_LOC_ATTR_ONE = "AAAAAAAAAA";
    private static final String UPDATED_LOC_ATTR_ONE = "BBBBBBBBBB";

    @Autowired
    private BarcodeReqLocDetRepository barcodeReqLocDetRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBarcodeReqLocDetMockMvc;

    private BarcodeReqLocDet barcodeReqLocDet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BarcodeReqLocDetResource barcodeReqLocDetResource = new BarcodeReqLocDetResource(barcodeReqLocDetRepository);
        this.restBarcodeReqLocDetMockMvc = MockMvcBuilders.standaloneSetup(barcodeReqLocDetResource)
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
    public static BarcodeReqLocDet createEntity(EntityManager em) {
        BarcodeReqLocDet barcodeReqLocDet = new BarcodeReqLocDet()
            .locAttrOne(DEFAULT_LOC_ATTR_ONE);
        return barcodeReqLocDet;
    }

    @Before
    public void initTest() {
        barcodeReqLocDet = createEntity(em);
    }

    @Test
    @Transactional
    public void createBarcodeReqLocDet() throws Exception {
        int databaseSizeBeforeCreate = barcodeReqLocDetRepository.findAll().size();

        // Create the BarcodeReqLocDet
        restBarcodeReqLocDetMockMvc.perform(post("/api/barcode-req-loc-dets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReqLocDet)))
            .andExpect(status().isCreated());

        // Validate the BarcodeReqLocDet in the database
        List<BarcodeReqLocDet> barcodeReqLocDetList = barcodeReqLocDetRepository.findAll();
        assertThat(barcodeReqLocDetList).hasSize(databaseSizeBeforeCreate + 1);
        BarcodeReqLocDet testBarcodeReqLocDet = barcodeReqLocDetList.get(barcodeReqLocDetList.size() - 1);
        assertThat(testBarcodeReqLocDet.getLocAttrOne()).isEqualTo(DEFAULT_LOC_ATTR_ONE);
    }

    @Test
    @Transactional
    public void createBarcodeReqLocDetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = barcodeReqLocDetRepository.findAll().size();

        // Create the BarcodeReqLocDet with an existing ID
        barcodeReqLocDet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBarcodeReqLocDetMockMvc.perform(post("/api/barcode-req-loc-dets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReqLocDet)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BarcodeReqLocDet> barcodeReqLocDetList = barcodeReqLocDetRepository.findAll();
        assertThat(barcodeReqLocDetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBarcodeReqLocDets() throws Exception {
        // Initialize the database
        barcodeReqLocDetRepository.saveAndFlush(barcodeReqLocDet);

        // Get all the barcodeReqLocDetList
        restBarcodeReqLocDetMockMvc.perform(get("/api/barcode-req-loc-dets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(barcodeReqLocDet.getId().intValue())))
            .andExpect(jsonPath("$.[*].locAttrOne").value(hasItem(DEFAULT_LOC_ATTR_ONE.toString())));
    }

    @Test
    @Transactional
    public void getBarcodeReqLocDet() throws Exception {
        // Initialize the database
        barcodeReqLocDetRepository.saveAndFlush(barcodeReqLocDet);

        // Get the barcodeReqLocDet
        restBarcodeReqLocDetMockMvc.perform(get("/api/barcode-req-loc-dets/{id}", barcodeReqLocDet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(barcodeReqLocDet.getId().intValue()))
            .andExpect(jsonPath("$.locAttrOne").value(DEFAULT_LOC_ATTR_ONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBarcodeReqLocDet() throws Exception {
        // Get the barcodeReqLocDet
        restBarcodeReqLocDetMockMvc.perform(get("/api/barcode-req-loc-dets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBarcodeReqLocDet() throws Exception {
        // Initialize the database
        barcodeReqLocDetRepository.saveAndFlush(barcodeReqLocDet);
        int databaseSizeBeforeUpdate = barcodeReqLocDetRepository.findAll().size();

        // Update the barcodeReqLocDet
        BarcodeReqLocDet updatedBarcodeReqLocDet = barcodeReqLocDetRepository.findOne(barcodeReqLocDet.getId());
        updatedBarcodeReqLocDet
            .locAttrOne(UPDATED_LOC_ATTR_ONE);

        restBarcodeReqLocDetMockMvc.perform(put("/api/barcode-req-loc-dets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBarcodeReqLocDet)))
            .andExpect(status().isOk());

        // Validate the BarcodeReqLocDet in the database
        List<BarcodeReqLocDet> barcodeReqLocDetList = barcodeReqLocDetRepository.findAll();
        assertThat(barcodeReqLocDetList).hasSize(databaseSizeBeforeUpdate);
        BarcodeReqLocDet testBarcodeReqLocDet = barcodeReqLocDetList.get(barcodeReqLocDetList.size() - 1);
        assertThat(testBarcodeReqLocDet.getLocAttrOne()).isEqualTo(UPDATED_LOC_ATTR_ONE);
    }

    @Test
    @Transactional
    public void updateNonExistingBarcodeReqLocDet() throws Exception {
        int databaseSizeBeforeUpdate = barcodeReqLocDetRepository.findAll().size();

        // Create the BarcodeReqLocDet

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBarcodeReqLocDetMockMvc.perform(put("/api/barcode-req-loc-dets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReqLocDet)))
            .andExpect(status().isCreated());

        // Validate the BarcodeReqLocDet in the database
        List<BarcodeReqLocDet> barcodeReqLocDetList = barcodeReqLocDetRepository.findAll();
        assertThat(barcodeReqLocDetList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBarcodeReqLocDet() throws Exception {
        // Initialize the database
        barcodeReqLocDetRepository.saveAndFlush(barcodeReqLocDet);
        int databaseSizeBeforeDelete = barcodeReqLocDetRepository.findAll().size();

        // Get the barcodeReqLocDet
        restBarcodeReqLocDetMockMvc.perform(delete("/api/barcode-req-loc-dets/{id}", barcodeReqLocDet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BarcodeReqLocDet> barcodeReqLocDetList = barcodeReqLocDetRepository.findAll();
        assertThat(barcodeReqLocDetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BarcodeReqLocDet.class);
        BarcodeReqLocDet barcodeReqLocDet1 = new BarcodeReqLocDet();
        barcodeReqLocDet1.setId(1L);
        BarcodeReqLocDet barcodeReqLocDet2 = new BarcodeReqLocDet();
        barcodeReqLocDet2.setId(barcodeReqLocDet1.getId());
        assertThat(barcodeReqLocDet1).isEqualTo(barcodeReqLocDet2);
        barcodeReqLocDet2.setId(2L);
        assertThat(barcodeReqLocDet1).isNotEqualTo(barcodeReqLocDet2);
        barcodeReqLocDet1.setId(null);
        assertThat(barcodeReqLocDet1).isNotEqualTo(barcodeReqLocDet2);
    }
}
