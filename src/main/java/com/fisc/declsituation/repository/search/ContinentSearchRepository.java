package com.fisc.declsituation.repository.search;

import com.fisc.declsituation.domain.Continent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Continent} entity.
 */
public interface ContinentSearchRepository extends ElasticsearchRepository<Continent, Long> {
}
