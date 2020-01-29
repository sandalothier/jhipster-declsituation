package com.fisc.declsituation.repository.search;

import com.fisc.declsituation.domain.Regions;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Regions} entity.
 */
public interface RegionsSearchRepository extends ElasticsearchRepository<Regions, Long> {
}
