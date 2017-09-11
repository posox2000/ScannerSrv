package com.home.app.web.rest;

import com.home.app.ScannerSrvApp;

import com.home.app.domain.BarcodeReqReader;
import com.home.app.repository.BarcodeReqReaderRepository;
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
 * Test class for the BarcodeReqReaderResource REST controller.
 *
 * @see BarcodeReqReaderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScannerSrvApp.class)
public class BarcodeReqReaderResourceIntTest {

    private static final String DEFAULT_READER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_READER_NAME = "BBBBBBBBBB";

    @Autowired
    private BarcodeReqReaderRepository barcodeReqReaderRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBarcodeReqReaderMockMvc;

    private BarcodeReqReader barcodeReqReader;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BarcodeReqReaderResource barcodeReqReaderResource = new BarcodeReqReaderResource(barcodeReqReaderRepository);
        this.restBarcodeReqReaderMockMvc = MockMvcBuilders.standaloneSetup(barcodeReqReaderResource)
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
    public static BarcodeReqReader createEntity(EntityManager em) {
        BarcodeReqReader barcodeReqReader = new BarcodeReqReader()
            .readerName(DEFAULT_READER_NAME);
        return barcodeReqReader;
    }

    @Before
    public void initTest() {
        barcodeReqReader = createEntity(em);
    }

    @Test
    @Transactional
    public void createBarcodeReqReader() throws Exception {
        int databaseSizeBeforeCreate = barcodeReqReaderRepository.findAll().size();

        // Create the BarcodeReqReader
        restBarcodeReqReaderMockMvc.perform(post("/api/barcode-req-readers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReqReader)))
            .andExpect(status().isCreated());

        // Validate the BarcodeReqReader in the database
        List<BarcodeReqReader> barcodeReqReaderList = barcodeReqReaderRepository.findAll();
        assertThat(barcodeReqReaderList).hasSize(databaseSizeBeforeCreate + 1);
        BarcodeReqReader testBarcodeReqReader = barcodeReqReaderList.get(barcodeReqReaderList.size() - 1);
        assertThat(testBarcodeReqReader.getReaderName()).isEqualTo(DEFAULT_READER_NAME);
    }

    @Test
    @Transactional
    public void createBarcodeReqReaderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = barcodeReqReaderRepository.findAll().size();

        // Create the BarcodeReqReader with an existing ID
        barcodeReqReader.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBarcodeReqReaderMockMvc.perform(post("/api/barcode-req-readers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReqReader)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BarcodeReqReader> barcodeReqReaderList = barcodeReqReaderRepository.findAll();
        assertThat(barcodeReqReaderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBarcodeReqReaders() throws Exception {
        // Initialize the database
        barcodeReqReaderRepository.saveAndFlush(barcodeReqReader);

        // Get all the barcodeReqReaderList
        restBarcodeReqReaderMockMvc.perform(get("/api/barcode-req-readers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(barcodeReqReader.getId().intValue())))
            .andExpect(jsonPath("$.[*].readerName").value(hasItem(DEFAULT_READER_NAME.toString())));
    }

    @Test
    @Transactional
    public void getBarcodeReqReader() throws Exception {
        // Initialize the database
        barcodeReqReaderRepository.saveAndFlush(barcodeReqReader);

        // Get the barcodeReqReader
        restBarcodeReqReaderMockMvc.perform(get("/api/barcode-req-readers/{id}", barcodeReqReader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(barcodeReqReader.getId().intValue()))
            .andExpect(jsonPath("$.readerName").value(DEFAULT_READER_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBarcodeReqReader() throws Exception {
        // Get the barcodeReqReader
        restBarcodeReqReaderMockMvc.perform(get("/api/barcode-req-readers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBarcodeReqReader() throws Exception {
        // Initialize the database
        barcodeReqReaderRepository.saveAndFlush(barcodeReqReader);
        int databaseSizeBeforeUpdate = barcodeReqReaderRepository.findAll().size();

        // Update the barcodeReqReader
        BarcodeReqReader updatedBarcodeReqReader = barcodeReqReaderRepository.findOne(barcodeReqReader.getId());
        updatedBarcodeReqReader
            .readerName(UPDATED_READER_NAME);

        restBarcodeReqReaderMockMvc.perform(put("/api/barcode-req-readers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBarcodeReqReader)))
            .andExpect(status().isOk());

        // Validate the BarcodeReqReader in the database
        List<BarcodeReqReader> barcodeReqReaderList = barcodeReqReaderRepository.findAll();
        assertThat(barcodeReqReaderList).hasSize(databaseSizeBeforeUpdate);
        BarcodeReqReader testBarcodeReqReader = barcodeReqReaderList.get(barcodeReqReaderList.size() - 1);
        assertThat(testBarcodeReqReader.getReaderName()).isEqualTo(UPDATED_READER_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingBarcodeReqReader() throws Exception {
        int databaseSizeBeforeUpdate = barcodeReqReaderRepository.findAll().size();

        // Create the BarcodeReqReader

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBarcodeReqReaderMockMvc.perform(put("/api/barcode-req-readers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReqReader)))
            .andExpect(status().isCreated());

        // Validate the BarcodeReqReader in the database
        List<BarcodeReqReader> barcodeReqReaderList = barcodeReqReaderRepository.findAll();
        assertThat(barcodeReqReaderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBarcodeReqReader() throws Exception {
        // Initialize the database
        barcodeReqReaderRepository.saveAndFlush(barcodeReqReader);
        int databaseSizeBeforeDelete = barcodeReqReaderRepository.findAll().size();

        // Get the barcodeReqReader
        restBarcodeReqReaderMockMvc.perform(delete("/api/barcode-req-readers/{id}", barcodeReqReader.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BarcodeReqReader> barcodeReqReaderList = barcodeReqReaderRepository.findAll();
        assertThat(barcodeReqReaderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BarcodeReqReader.class);
        BarcodeReqReader barcodeReqReader1 = new BarcodeReqReader();
        barcodeReqReader1.setId(1L);
        BarcodeReqReader barcodeReqReader2 = new BarcodeReqReader();
        barcodeReqReader2.setId(barcodeReqReader1.getId());
        assertThat(barcodeReqReader1).isEqualTo(barcodeReqReader2);
        barcodeReqReader2.setId(2L);
        assertThat(barcodeReqReader1).isNotEqualTo(barcodeReqReader2);
        barcodeReqReader1.setId(null);
        assertThat(barcodeReqReader1).isNotEqualTo(barcodeReqReader2);
    }
}
