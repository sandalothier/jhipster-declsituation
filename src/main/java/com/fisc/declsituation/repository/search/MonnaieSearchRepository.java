package com.fisc.declsituation.repository.search;

import com.fisc.declsituation.domain.Monnaie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Monnaie} entity.
 */
public interface MonnaieSearchRepository extends ElasticsearchRepository<Monnaie, Long> {
}
