package com.fisc.declsituation.repository.search;

import com.fisc.declsituation.domain.Pays;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Pays} entity.
 */
public interface PaysSearchRepository extends ElasticsearchRepository<Pays, Long> {
}
