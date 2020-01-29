package com.fisc.declsituation.web.rest;

import com.fisc.declsituation.domain.Regions;
import com.fisc.declsituation.repository.RegionsRepository;
import com.fisc.declsituation.repository.search.RegionsSearchRepository;
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
 * REST controller for managing {@link com.fisc.declsituation.domain.Regions}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RegionsResource {

    private final Logger log = LoggerFactory.getLogger(RegionsResource.class);

    private static final String ENTITY_NAME = "declsituationRegions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegionsRepository regionsRepository;

    private final RegionsSearchRepository regionsSearchRepository;

    public RegionsResource(RegionsRepository regionsRepository, RegionsSearchRepository regionsSearchRepository) {
        this.regionsRepository = regionsRepository;
        this.regionsSearchRepository = regionsSearchRepository;
    }

    /**
     * {@code POST  /regions} : Create a new regions.
     *
     * @param regions the regions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new regions, or with status {@code 400 (Bad Request)} if the regions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/regions")
    public ResponseEntity<Regions> createRegions(@RequestBody Regions regions) throws URISyntaxException {
        log.debug("REST request to save Regions : {}", regions);
        if (regions.getId() != null) {
            throw new BadRequestAlertException("A new regions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Regions result = regionsRepository.save(regions);
        regionsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/regions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /regions} : Updates an existing regions.
     *
     * @param regions the regions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regions,
     * or with status {@code 400 (Bad Request)} if the regions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the regions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/regions")
    public ResponseEntity<Regions> updateRegions(@RequestBody Regions regions) throws URISyntaxException {
        log.debug("REST request to update Regions : {}", regions);
        if (regions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Regions result = regionsRepository.save(regions);
        regionsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, regions.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /regions} : get all the regions.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of regions in body.
     */
    @GetMapping("/regions")
    public List<Regions> getAllRegions() {
        log.debug("REST request to get all Regions");
        return regionsRepository.findAll();
    }

    /**
     * {@code GET  /regions/:id} : get the "id" regions.
     *
     * @param id the id of the regions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the regions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/regions/{id}")
    public ResponseEntity<Regions> getRegions(@PathVariable Long id) {
        log.debug("REST request to get Regions : {}", id);
        Optional<Regions> regions = regionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(regions);
    }

    /**
     * {@code DELETE  /regions/:id} : delete the "id" regions.
     *
     * @param id the id of the regions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/regions/{id}")
    public ResponseEntity<Void> deleteRegions(@PathVariable Long id) {
        log.debug("REST request to delete Regions : {}", id);
        regionsRepository.deleteById(id);
        regionsSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/regions?query=:query} : search for the regions corresponding
     * to the query.
     *
     * @param query the query of the regions search.
     * @return the result of the search.
     */
    @GetMapping("/_search/regions")
    public List<Regions> searchRegions(@RequestParam String query) {
        log.debug("REST request to search Regions for query {}", query);
        return StreamSupport
            .stream(regionsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
