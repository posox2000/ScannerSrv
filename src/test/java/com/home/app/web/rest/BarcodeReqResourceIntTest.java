package com.home.app.web.rest;

import com.home.app.ScannerSrvApp;

import com.home.app.domain.BarcodeReq;
import com.home.app.repository.BarcodeReqRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BarcodeReqResource REST controller.
 *
 * @see BarcodeReqResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScannerSrvApp.class)
public class BarcodeReqResourceIntTest {

    private static final LocalDate DEFAULT_REQUEST_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REQUEST_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_GOVID = "AAAAAAAAAA";
    private static final String UPDATED_GOVID = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_OFFICE_ID = "BBBBBBBBBB";

    @Autowired
    private BarcodeReqRepository barcodeReqRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBarcodeReqMockMvc;

    private BarcodeReq barcodeReq;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BarcodeReqResource barcodeReqResource = new BarcodeReqResource(barcodeReqRepository);
        this.restBarcodeReqMockMvc = MockMvcBuilders.standaloneSetup(barcodeReqResource)
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
    public static BarcodeReq createEntity(EntityManager em) {
        BarcodeReq barcodeReq = new BarcodeReq()
            .requestDate(DEFAULT_REQUEST_DATE)
            .govid(DEFAULT_GOVID)
            .officeId(DEFAULT_OFFICE_ID);
        return barcodeReq;
    }

    @Before
    public void initTest() {
        barcodeReq = createEntity(em);
    }

    @Test
    @Transactional
    public void createBarcodeReq() throws Exception {
        int databaseSizeBeforeCreate = barcodeReqRepository.findAll().size();

        // Create the BarcodeReq
        restBarcodeReqMockMvc.perform(post("/api/barcode-reqs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReq)))
            .andExpect(status().isCreated());

        // Validate the BarcodeReq in the database
        List<BarcodeReq> barcodeReqList = barcodeReqRepository.findAll();
        assertThat(barcodeReqList).hasSize(databaseSizeBeforeCreate + 1);
        BarcodeReq testBarcodeReq = barcodeReqList.get(barcodeReqList.size() - 1);
        assertThat(testBarcodeReq.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
        assertThat(testBarcodeReq.getGovid()).isEqualTo(DEFAULT_GOVID);
        assertThat(testBarcodeReq.getOfficeId()).isEqualTo(DEFAULT_OFFICE_ID);
    }

    @Test
    @Transactional
    public void createBarcodeReqWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = barcodeReqRepository.findAll().size();

        // Create the BarcodeReq with an existing ID
        barcodeReq.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBarcodeReqMockMvc.perform(post("/api/barcode-reqs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReq)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BarcodeReq> barcodeReqList = barcodeReqRepository.findAll();
        assertThat(barcodeReqList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBarcodeReqs() throws Exception {
        // Initialize the database
        barcodeReqRepository.saveAndFlush(barcodeReq);

        // Get all the barcodeReqList
        restBarcodeReqMockMvc.perform(get("/api/barcode-reqs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(barcodeReq.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].govid").value(hasItem(DEFAULT_GOVID.toString())))
            .andExpect(jsonPath("$.[*].officeId").value(hasItem(DEFAULT_OFFICE_ID.toString())));
    }

    @Test
    @Transactional
    public void getBarcodeReq() throws Exception {
        // Initialize the database
        barcodeReqRepository.saveAndFlush(barcodeReq);

        // Get the barcodeReq
        restBarcodeReqMockMvc.perform(get("/api/barcode-reqs/{id}", barcodeReq.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(barcodeReq.getId().intValue()))
            .andExpect(jsonPath("$.requestDate").value(DEFAULT_REQUEST_DATE.toString()))
            .andExpect(jsonPath("$.govid").value(DEFAULT_GOVID.toString()))
            .andExpect(jsonPath("$.officeId").value(DEFAULT_OFFICE_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBarcodeReq() throws Exception {
        // Get the barcodeReq
        restBarcodeReqMockMvc.perform(get("/api/barcode-reqs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBarcodeReq() throws Exception {
        // Initialize the database
        barcodeReqRepository.saveAndFlush(barcodeReq);
        int databaseSizeBeforeUpdate = barcodeReqRepository.findAll().size();

        // Update the barcodeReq
        BarcodeReq updatedBarcodeReq = barcodeReqRepository.findOne(barcodeReq.getId());
        updatedBarcodeReq
            .requestDate(UPDATED_REQUEST_DATE)
            .govid(UPDATED_GOVID)
            .officeId(UPDATED_OFFICE_ID);

        restBarcodeReqMockMvc.perform(put("/api/barcode-reqs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBarcodeReq)))
            .andExpect(status().isOk());

        // Validate the BarcodeReq in the database
        List<BarcodeReq> barcodeReqList = barcodeReqRepository.findAll();
        assertThat(barcodeReqList).hasSize(databaseSizeBeforeUpdate);
        BarcodeReq testBarcodeReq = barcodeReqList.get(barcodeReqList.size() - 1);
        assertThat(testBarcodeReq.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testBarcodeReq.getGovid()).isEqualTo(UPDATED_GOVID);
        assertThat(testBarcodeReq.getOfficeId()).isEqualTo(UPDATED_OFFICE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingBarcodeReq() throws Exception {
        int databaseSizeBeforeUpdate = barcodeReqRepository.findAll().size();

        // Create the BarcodeReq

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBarcodeReqMockMvc.perform(put("/api/barcode-reqs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReq)))
            .andExpect(status().isCreated());

        // Validate the BarcodeReq in the database
        List<BarcodeReq> barcodeReqList = barcodeReqRepository.findAll();
        assertThat(barcodeReqList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBarcodeReq() throws Exception {
        // Initialize the database
        barcodeReqRepository.saveAndFlush(barcodeReq);
        int databaseSizeBeforeDelete = barcodeReqRepository.findAll().size();

        // Get the barcodeReq
        restBarcodeReqMockMvc.perform(delete("/api/barcode-reqs/{id}", barcodeReq.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BarcodeReq> barcodeReqList = barcodeReqRepository.findAll();
        assertThat(barcodeReqList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BarcodeReq.class);
        BarcodeReq barcodeReq1 = new BarcodeReq();
        barcodeReq1.setId(1L);
        BarcodeReq barcodeReq2 = new BarcodeReq();
        barcodeReq2.setId(barcodeReq1.getId());
        assertThat(barcodeReq1).isEqualTo(barcodeReq2);
        barcodeReq2.setId(2L);
        assertThat(barcodeReq1).isNotEqualTo(barcodeReq2);
        barcodeReq1.setId(null);
        assertThat(barcodeReq1).isNotEqualTo(barcodeReq2);
    }
}
