package com.fisc.declsituation.repository.search;

import com.fisc.declsituation.domain.RegroupementRegional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link RegroupementRegional} entity.
 */
public interface RegroupementRegionalSearchRepository extends ElasticsearchRepository<RegroupementRegional, Long> {
}
