package com.fisc.declsituation.web.rest;

import com.fisc.declsituation.DeclsituationApp;
import com.fisc.declsituation.domain.Monnaie;
import com.fisc.declsituation.repository.MonnaieRepository;
import com.fisc.declsituation.repository.search.MonnaieSearchRepository;
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
 * Integration tests for the {@link MonnaieResource} REST controller.
 */
@SpringBootTest(classes = DeclsituationApp.class)
public class MonnaieResourceIT {

    private static final String DEFAULT_MONNAIE = "AAAAAAAAAA";
    private static final String UPDATED_MONNAIE = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLE = "AAAAAAAAAA";
    private static final String UPDATED_SIGLE = "BBBBBBBBBB";

    @Autowired
    private MonnaieRepository monnaieRepository;

    /**
     * This repository is mocked in the com.fisc.declsituation.repository.search test package.
     *
     * @see com.fisc.declsituation.repository.search.MonnaieSearchRepositoryMockConfiguration
     */
    @Autowired
    private MonnaieSearchRepository mockMonnaieSearchRepository;

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

    private MockMvc restMonnaieMockMvc;

    private Monnaie monnaie;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MonnaieResource monnaieResource = new MonnaieResource(monnaieRepository, mockMonnaieSearchRepository);
        this.restMonnaieMockMvc = MockMvcBuilders.standaloneSetup(monnaieResource)
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
    public static Monnaie createEntity(EntityManager em) {
        Monnaie monnaie = new Monnaie()
            .monnaie(DEFAULT_MONNAIE)
            .sigle(DEFAULT_SIGLE);
        return monnaie;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Monnaie createUpdatedEntity(EntityManager em) {
        Monnaie monnaie = new Monnaie()
            .monnaie(UPDATED_MONNAIE)
            .sigle(UPDATED_SIGLE);
        return monnaie;
    }

    @BeforeEach
    public void initTest() {
        monnaie = createEntity(em);
    }

    @Test
    @Transactional
    public void createMonnaie() throws Exception {
        int databaseSizeBeforeCreate = monnaieRepository.findAll().size();

        // Create the Monnaie
        restMonnaieMockMvc.perform(post("/api/monnaies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monnaie)))
            .andExpect(status().isCreated());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeCreate + 1);
        Monnaie testMonnaie = monnaieList.get(monnaieList.size() - 1);
        assertThat(testMonnaie.getMonnaie()).isEqualTo(DEFAULT_MONNAIE);
        assertThat(testMonnaie.getSigle()).isEqualTo(DEFAULT_SIGLE);

        // Validate the Monnaie in Elasticsearch
        verify(mockMonnaieSearchRepository, times(1)).save(testMonnaie);
    }

    @Test
    @Transactional
    public void createMonnaieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = monnaieRepository.findAll().size();

        // Create the Monnaie with an existing ID
        monnaie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonnaieMockMvc.perform(post("/api/monnaies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monnaie)))
            .andExpect(status().isBadRequest());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeCreate);

        // Validate the Monnaie in Elasticsearch
        verify(mockMonnaieSearchRepository, times(0)).save(monnaie);
    }


    @Test
    @Transactional
    public void getAllMonnaies() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList
        restMonnaieMockMvc.perform(get("/api/monnaies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monnaie.getId().intValue())))
            .andExpect(jsonPath("$.[*].monnaie").value(hasItem(DEFAULT_MONNAIE)))
            .andExpect(jsonPath("$.[*].sigle").value(hasItem(DEFAULT_SIGLE)));
    }
    
    @Test
    @Transactional
    public void getMonnaie() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get the monnaie
        restMonnaieMockMvc.perform(get("/api/monnaies/{id}", monnaie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(monnaie.getId().intValue()))
            .andExpect(jsonPath("$.monnaie").value(DEFAULT_MONNAIE))
            .andExpect(jsonPath("$.sigle").value(DEFAULT_SIGLE));
    }

    @Test
    @Transactional
    public void getNonExistingMonnaie() throws Exception {
        // Get the monnaie
        restMonnaieMockMvc.perform(get("/api/monnaies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMonnaie() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();

        // Update the monnaie
        Monnaie updatedMonnaie = monnaieRepository.findById(monnaie.getId()).get();
        // Disconnect from session so that the updates on updatedMonnaie are not directly saved in db
        em.detach(updatedMonnaie);
        updatedMonnaie
            .monnaie(UPDATED_MONNAIE)
            .sigle(UPDATED_SIGLE);

        restMonnaieMockMvc.perform(put("/api/monnaies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMonnaie)))
            .andExpect(status().isOk());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
        Monnaie testMonnaie = monnaieList.get(monnaieList.size() - 1);
        assertThat(testMonnaie.getMonnaie()).isEqualTo(UPDATED_MONNAIE);
        assertThat(testMonnaie.getSigle()).isEqualTo(UPDATED_SIGLE);

        // Validate the Monnaie in Elasticsearch
        verify(mockMonnaieSearchRepository, times(1)).save(testMonnaie);
    }

    @Test
    @Transactional
    public void updateNonExistingMonnaie() throws Exception {
        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();

        // Create the Monnaie

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonnaieMockMvc.perform(put("/api/monnaies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(monnaie)))
            .andExpect(status().isBadRequest());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Monnaie in Elasticsearch
        verify(mockMonnaieSearchRepository, times(0)).save(monnaie);
    }

    @Test
    @Transactional
    public void deleteMonnaie() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        int databaseSizeBeforeDelete = monnaieRepository.findAll().size();

        // Delete the monnaie
        restMonnaieMockMvc.perform(delete("/api/monnaies/{id}", monnaie.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Monnaie in Elasticsearch
        verify(mockMonnaieSearchRepository, times(1)).deleteById(monnaie.getId());
    }

    @Test
    @Transactional
    public void searchMonnaie() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);
        when(mockMonnaieSearchRepository.search(queryStringQuery("id:" + monnaie.getId())))
            .thenReturn(Collections.singletonList(monnaie));
        // Search the monnaie
        restMonnaieMockMvc.perform(get("/api/_search/monnaies?query=id:" + monnaie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monnaie.getId().intValue())))
            .andExpect(jsonPath("$.[*].monnaie").value(hasItem(DEFAULT_MONNAIE)))
            .andExpect(jsonPath("$.[*].sigle").value(hasItem(DEFAULT_SIGLE)));
    }
}
