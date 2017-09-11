package com.home.app.web.rest;

import com.home.app.ScannerSrvApp;

import com.home.app.domain.BarcodeReqFile;
import com.home.app.repository.BarcodeReqFileRepository;
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
 * Test class for the BarcodeReqFileResource REST controller.
 *
 * @see BarcodeReqFileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScannerSrvApp.class)
public class BarcodeReqFileResourceIntTest {

    private static final String DEFAULT_FILE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private BarcodeReqFileRepository barcodeReqFileRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBarcodeReqFileMockMvc;

    private BarcodeReqFile barcodeReqFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BarcodeReqFileResource barcodeReqFileResource = new BarcodeReqFileResource(barcodeReqFileRepository);
        this.restBarcodeReqFileMockMvc = MockMvcBuilders.standaloneSetup(barcodeReqFileResource)
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
    public static BarcodeReqFile createEntity(EntityManager em) {
        BarcodeReqFile barcodeReqFile = new BarcodeReqFile()
            .fileNumber(DEFAULT_FILE_NUMBER);
        return barcodeReqFile;
    }

    @Before
    public void initTest() {
        barcodeReqFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createBarcodeReqFile() throws Exception {
        int databaseSizeBeforeCreate = barcodeReqFileRepository.findAll().size();

        // Create the BarcodeReqFile
        restBarcodeReqFileMockMvc.perform(post("/api/barcode-req-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReqFile)))
            .andExpect(status().isCreated());

        // Validate the BarcodeReqFile in the database
        List<BarcodeReqFile> barcodeReqFileList = barcodeReqFileRepository.findAll();
        assertThat(barcodeReqFileList).hasSize(databaseSizeBeforeCreate + 1);
        BarcodeReqFile testBarcodeReqFile = barcodeReqFileList.get(barcodeReqFileList.size() - 1);
        assertThat(testBarcodeReqFile.getFileNumber()).isEqualTo(DEFAULT_FILE_NUMBER);
    }

    @Test
    @Transactional
    public void createBarcodeReqFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = barcodeReqFileRepository.findAll().size();

        // Create the BarcodeReqFile with an existing ID
        barcodeReqFile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBarcodeReqFileMockMvc.perform(post("/api/barcode-req-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReqFile)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BarcodeReqFile> barcodeReqFileList = barcodeReqFileRepository.findAll();
        assertThat(barcodeReqFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBarcodeReqFiles() throws Exception {
        // Initialize the database
        barcodeReqFileRepository.saveAndFlush(barcodeReqFile);

        // Get all the barcodeReqFileList
        restBarcodeReqFileMockMvc.perform(get("/api/barcode-req-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(barcodeReqFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileNumber").value(hasItem(DEFAULT_FILE_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getBarcodeReqFile() throws Exception {
        // Initialize the database
        barcodeReqFileRepository.saveAndFlush(barcodeReqFile);

        // Get the barcodeReqFile
        restBarcodeReqFileMockMvc.perform(get("/api/barcode-req-files/{id}", barcodeReqFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(barcodeReqFile.getId().intValue()))
            .andExpect(jsonPath("$.fileNumber").value(DEFAULT_FILE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBarcodeReqFile() throws Exception {
        // Get the barcodeReqFile
        restBarcodeReqFileMockMvc.perform(get("/api/barcode-req-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBarcodeReqFile() throws Exception {
        // Initialize the database
        barcodeReqFileRepository.saveAndFlush(barcodeReqFile);
        int databaseSizeBeforeUpdate = barcodeReqFileRepository.findAll().size();

        // Update the barcodeReqFile
        BarcodeReqFile updatedBarcodeReqFile = barcodeReqFileRepository.findOne(barcodeReqFile.getId());
        updatedBarcodeReqFile
            .fileNumber(UPDATED_FILE_NUMBER);

        restBarcodeReqFileMockMvc.perform(put("/api/barcode-req-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBarcodeReqFile)))
            .andExpect(status().isOk());

        // Validate the BarcodeReqFile in the database
        List<BarcodeReqFile> barcodeReqFileList = barcodeReqFileRepository.findAll();
        assertThat(barcodeReqFileList).hasSize(databaseSizeBeforeUpdate);
        BarcodeReqFile testBarcodeReqFile = barcodeReqFileList.get(barcodeReqFileList.size() - 1);
        assertThat(testBarcodeReqFile.getFileNumber()).isEqualTo(UPDATED_FILE_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingBarcodeReqFile() throws Exception {
        int databaseSizeBeforeUpdate = barcodeReqFileRepository.findAll().size();

        // Create the BarcodeReqFile

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBarcodeReqFileMockMvc.perform(put("/api/barcode-req-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeReqFile)))
            .andExpect(status().isCreated());

        // Validate the BarcodeReqFile in the database
        List<BarcodeReqFile> barcodeReqFileList = barcodeReqFileRepository.findAll();
        assertThat(barcodeReqFileList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBarcodeReqFile() throws Exception {
        // Initialize the database
        barcodeReqFileRepository.saveAndFlush(barcodeReqFile);
        int databaseSizeBeforeDelete = barcodeReqFileRepository.findAll().size();

        // Get the barcodeReqFile
        restBarcodeReqFileMockMvc.perform(delete("/api/barcode-req-files/{id}", barcodeReqFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BarcodeReqFile> barcodeReqFileList = barcodeReqFileRepository.findAll();
        assertThat(barcodeReqFileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BarcodeReqFile.class);
        BarcodeReqFile barcodeReqFile1 = new BarcodeReqFile();
        barcodeReqFile1.setId(1L);
        BarcodeReqFile barcodeReqFile2 = new BarcodeReqFile();
        barcodeReqFile2.setId(barcodeReqFile1.getId());
        assertThat(barcodeReqFile1).isEqualTo(barcodeReqFile2);
        barcodeReqFile2.setId(2L);
        assertThat(barcodeReqFile1).isNotEqualTo(barcodeReqFile2);
        barcodeReqFile1.setId(null);
        assertThat(barcodeReqFile1).isNotEqualTo(barcodeReqFile2);
    }
}
