package com.fisc.declsituation.web.rest;

import com.fisc.declsituation.domain.Monnaie;
import com.fisc.declsituation.repository.MonnaieRepository;
import com.fisc.declsituation.repository.search.MonnaieSearchRepository;
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
 * REST controller for managing {@link com.fisc.declsituation.domain.Monnaie}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MonnaieResource {

    private final Logger log = LoggerFactory.getLogger(MonnaieResource.class);

    private static final String ENTITY_NAME = "declsituationMonnaie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonnaieRepository monnaieRepository;

    private final MonnaieSearchRepository monnaieSearchRepository;

    public MonnaieResource(MonnaieRepository monnaieRepository, MonnaieSearchRepository monnaieSearchRepository) {
        this.monnaieRepository = monnaieRepository;
        this.monnaieSearchRepository = monnaieSearchRepository;
    }

    /**
     * {@code POST  /monnaies} : Create a new monnaie.
     *
     * @param monnaie the monnaie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monnaie, or with status {@code 400 (Bad Request)} if the monnaie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/monnaies")
    public ResponseEntity<Monnaie> createMonnaie(@RequestBody Monnaie monnaie) throws URISyntaxException {
        log.debug("REST request to save Monnaie : {}", monnaie);
        if (monnaie.getId() != null) {
            throw new BadRequestAlertException("A new monnaie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Monnaie result = monnaieRepository.save(monnaie);
        monnaieSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/monnaies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /monnaies} : Updates an existing monnaie.
     *
     * @param monnaie the monnaie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monnaie,
     * or with status {@code 400 (Bad Request)} if the monnaie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monnaie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/monnaies")
    public ResponseEntity<Monnaie> updateMonnaie(@RequestBody Monnaie monnaie) throws URISyntaxException {
        log.debug("REST request to update Monnaie : {}", monnaie);
        if (monnaie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Monnaie result = monnaieRepository.save(monnaie);
        monnaieSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monnaie.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /monnaies} : get all the monnaies.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monnaies in body.
     */
    @GetMapping("/monnaies")
    public List<Monnaie> getAllMonnaies(@RequestParam(required = false) String filter) {
        if ("pays-is-null".equals(filter)) {
            log.debug("REST request to get all Monnaies where pays is null");
            return StreamSupport
                .stream(monnaieRepository.findAll().spliterator(), false)
                .filter(monnaie -> monnaie.getPays() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Monnaies");
        return monnaieRepository.findAll();
    }

    /**
     * {@code GET  /monnaies/:id} : get the "id" monnaie.
     *
     * @param id the id of the monnaie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monnaie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/monnaies/{id}")
    public ResponseEntity<Monnaie> getMonnaie(@PathVariable Long id) {
        log.debug("REST request to get Monnaie : {}", id);
        Optional<Monnaie> monnaie = monnaieRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(monnaie);
    }

    /**
     * {@code DELETE  /monnaies/:id} : delete the "id" monnaie.
     *
     * @param id the id of the monnaie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/monnaies/{id}")
    public ResponseEntity<Void> deleteMonnaie(@PathVariable Long id) {
        log.debug("REST request to delete Monnaie : {}", id);
        monnaieRepository.deleteById(id);
        monnaieSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/monnaies?query=:query} : search for the monnaie corresponding
     * to the query.
     *
     * @param query the query of the monnaie search.
     * @return the result of the search.
     */
    @GetMapping("/_search/monnaies")
    public List<Monnaie> searchMonnaies(@RequestParam String query) {
        log.debug("REST request to search Monnaies for query {}", query);
        return StreamSupport
            .stream(monnaieSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
