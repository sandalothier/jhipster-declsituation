package com.fisc.declsituation.web.rest;

import com.fisc.declsituation.domain.Continent;
import com.fisc.declsituation.repository.ContinentRepository;
import com.fisc.declsituation.repository.search.ContinentSearchRepository;
import com.fisc.declsituation.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.fisc.declsituation.domain.Continent}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContinentResource {

    private final Logger log = LoggerFactory.getLogger(ContinentResource.class);

    private static final String ENTITY_NAME = "declsituationContinent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContinentRepository continentRepository;

    private final ContinentSearchRepository continentSearchRepository;

    public ContinentResource(ContinentRepository continentRepository, ContinentSearchRepository continentSearchRepository) {
        this.continentRepository = continentRepository;
        this.continentSearchRepository = continentSearchRepository;
    }

    /**
     * {@code POST  /continents} : Create a new continent.
     *
     * @param continent the continent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new continent, or with status {@code 400 (Bad Request)} if the continent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/continents")
    public ResponseEntity<Continent> createContinent(@RequestBody Continent continent) throws URISyntaxException {
        log.debug("REST request to save Continent : {}", continent);
        if (continent.getId() != null) {
            throw new BadRequestAlertException("A new continent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Continent result = continentRepository.save(continent);
        continentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/continents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /continents} : Updates an existing continent.
     *
     * @param continent the continent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated continent,
     * or with status {@code 400 (Bad Request)} if the continent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the continent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/continents")
    public ResponseEntity<Continent> updateContinent(@RequestBody Continent continent) throws URISyntaxException {
        log.debug("REST request to update Continent : {}", continent);
        if (continent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Continent result = continentRepository.save(continent);
        continentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, continent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /continents} : get all the continents.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of continents in body.
     */
    @GetMapping("/continents")
    public List<Continent> getAllContinents() {
        log.debug("REST request to get all Continents");
        return continentRepository.findAll();
    }

    /**
     * {@code GET  /continents/:id} : get the "id" continent.
     *
     * @param id the id of the continent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the continent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/continents/{id}")
    public ResponseEntity<Continent> getContinent(@PathVariable Long id) {
        log.debug("REST request to get Continent : {}", id);
        Optional<Continent> continent = continentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(continent);
    }

    /**
     * {@code DELETE  /continents/:id} : delete the "id" continent.
     *
     * @param id the id of the continent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/continents/{id}")
    public ResponseEntity<Void> deleteContinent(@PathVariable Long id) {
        log.debug("REST request to delete Continent : {}", id);
        continentRepository.deleteById(id);
        continentSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/continents?query=:query} : search for the continent corresponding
     * to the query.
     *
     * @param query the query of the continent search.
     * @return the result of the search.
     */
    @GetMapping("/_search/continents")
    public List<Continent> searchContinents(@RequestParam String query) {
        log.debug("REST request to search Continents for query {}", query);
        return StreamSupport
            .stream(continentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
