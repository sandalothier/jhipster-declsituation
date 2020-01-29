package com.fisc.declsituation.web.rest;

import com.fisc.declsituation.DeclsituationApp;
import com.fisc.declsituation.domain.Pays;
import com.fisc.declsituation.repository.PaysRepository;
import com.fisc.declsituation.repository.search.PaysSearchRepository;
import com.fisc.declsituation.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.fisc.declsituation.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PaysResource} REST controller.
 */
@SpringBootTest(classes = DeclsituationApp.class)
public class PaysResourceIT {

    private static final String DEFAULT_NOM_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PAYS = "BBBBBBBBBB";

    private static final Double DEFAULT_SUPERFICIE = 1D;
    private static final Double UPDATED_SUPERFICIE = 2D;

    private static final String DEFAULT_SIGLE_AUTO = "AAAAAAAAAA";
    private static final String UPDATED_SIGLE_AUTO = "BBBBBBBBBB";

    private static final String DEFAULT_CAPITALE = "AAAAAAAAAA";
    private static final String UPDATED_CAPITALE = "BBBBBBBBBB";

    @Autowired
    private PaysRepository paysRepository;

    /**
     * This repository is mocked in the com.fisc.declsituation.repository.search test package.
     *
     * @see com.fisc.declsituation.repository.search.PaysSearchRepositoryMockConfiguration
     */
    @Autowired
    private PaysSearchRepository mockPaysSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPaysMockMvc;

    private Pays pays;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaysResource paysResource = new PaysResource(paysRepository, mockPaysSearchRepository);
        this.restPaysMockMvc = MockMvcBuilders.standaloneSetup(paysResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pays createEntity(EntityManager em) {
        Pays pays = new Pays()
            .nomPays(DEFAULT_NOM_PAYS)
            .superficie(DEFAULT_SUPERFICIE)
            .sigleAuto(DEFAULT_SIGLE_AUTO)
            .capitale(DEFAULT_CAPITALE);
        return pays;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pays createUpdatedEntity(EntityManager em) {
        Pays pays = new Pays()
            .nomPays(UPDATED_NOM_PAYS)
            .superficie(UPDATED_SUPERFICIE)
            .sigleAuto(UPDATED_SIGLE_AUTO)
            .capitale(UPDATED_CAPITALE);
        return pays;
    }

    @BeforeEach
    public void initTest() {
        pays = createEntity(em);
    }

    @Test
    @Transactional
    public void createPays() throws Exception {
        int databaseSizeBeforeCreate = paysRepository.findAll().size();

        // Create the Pays
        restPaysMockMvc.perform(post("/api/pays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pays)))
            .andExpect(status().isCreated());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeCreate + 1);
        Pays testPays = paysList.get(paysList.size() - 1);
        assertThat(testPays.getNomPays()).isEqualTo(DEFAULT_NOM_PAYS);
        assertThat(testPays.getSuperficie()).isEqualTo(DEFAULT_SUPERFICIE);
        assertThat(testPays.getSigleAuto()).isEqualTo(DEFAULT_SIGLE_AUTO);
        assertThat(testPays.getCapitale()).isEqualTo(DEFAULT_CAPITALE);

        // Validate the Pays in Elasticsearch
        verify(mockPaysSearchRepository, times(1)).save(testPays);
    }

    @Test
    @Transactional
    public void createPaysWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paysRepository.findAll().size();

        // Create the Pays with an existing ID
        pays.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaysMockMvc.perform(post("/api/pays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pays)))
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeCreate);

        // Validate the Pays in Elasticsearch
        verify(mockPaysSearchRepository, times(0)).save(pays);
    }


    @Test
    @Transactional
    public void getAllPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList
        restPaysMockMvc.perform(get("/api/pays?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pays.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPays").value(hasItem(DEFAULT_NOM_PAYS)))
            .andExpect(jsonPath("$.[*].superficie").value(hasItem(DEFAULT_SUPERFICIE.doubleValue())))
            .andExpect(jsonPath("$.[*].sigleAuto").value(hasItem(DEFAULT_SIGLE_AUTO)))
            .andExpect(jsonPath("$.[*].capitale").value(hasItem(DEFAULT_CAPITALE)));
    }
    
    @Test
    @Transactional
    public void getPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get the pays
        restPaysMockMvc.perform(get("/api/pays/{id}", pays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pays.getId().intValue()))
            .andExpect(jsonPath("$.nomPays").value(DEFAULT_NOM_PAYS))
            .andExpect(jsonPath("$.superficie").value(DEFAULT_SUPERFICIE.doubleValue()))
            .andExpect(jsonPath("$.sigleAuto").value(DEFAULT_SIGLE_AUTO))
            .andExpect(jsonPath("$.capitale").value(DEFAULT_CAPITALE));
    }

    @Test
    @Transactional
    public void getNonExistingPays() throws Exception {
        // Get the pays
        restPaysMockMvc.perform(get("/api/pays/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        int databaseSizeBeforeUpdate = paysRepository.findAll().size();

        // Update the pays
        Pays updatedPays = paysRepository.findById(pays.getId()).get();
        // Disconnect from session so that the updates on updatedPays are not directly saved in db
        em.detach(updatedPays);
        updatedPays
            .nomPays(UPDATED_NOM_PAYS)
            .superficie(UPDATED_SUPERFICIE)
            .sigleAuto(UPDATED_SIGLE_AUTO)
            .capitale(UPDATED_CAPITALE);

        restPaysMockMvc.perform(put("/api/pays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPays)))
            .andExpect(status().isOk());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        Pays testPays = paysList.get(paysList.size() - 1);
        assertThat(testPays.getNomPays()).isEqualTo(UPDATED_NOM_PAYS);
        assertThat(testPays.getSuperficie()).isEqualTo(UPDATED_SUPERFICIE);
        assertThat(testPays.getSigleAuto()).isEqualTo(UPDATED_SIGLE_AUTO);
        assertThat(testPays.getCapitale()).isEqualTo(UPDATED_CAPITALE);

        // Validate the Pays in Elasticsearch
        verify(mockPaysSearchRepository, times(1)).save(testPays);
    }

    @Test
    @Transactional
    public void updateNonExistingPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();

        // Create the Pays

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaysMockMvc.perform(put("/api/pays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pays)))
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Pays in Elasticsearch
        verify(mockPaysSearchRepository, times(0)).save(pays);
    }

    @Test
    @Transactional
    public void deletePays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        int databaseSizeBeforeDelete = paysRepository.findAll().size();

        // Delete the pays
        restPaysMockMvc.perform(delete("/api/pays/{id}", pays.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Pays in Elasticsearch
        verify(mockPaysSearchRepository, times(1)).deleteById(pays.getId());
    }

    @Test
    @Transactional
    public void searchPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);
        when(mockPaysSearchRepository.search(queryStringQuery("id:" + pays.getId())))
            .thenReturn(Collections.singletonList(pays));
        // Search the pays
        restPaysMockMvc.perform(get("/api/_search/pays?query=id:" + pays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pays.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPays").value(hasItem(DEFAULT_NOM_PAYS)))
            .andExpect(jsonPath("$.[*].superficie").value(hasItem(DEFAULT_SUPERFICIE.doubleValue())))
            .andExpect(jsonPath("$.[*].sigleAuto").value(hasItem(DEFAULT_SIGLE_AUTO)))
            .andExpect(jsonPath("$.[*].capitale").value(hasItem(DEFAULT_CAPITALE)));
    }
}
