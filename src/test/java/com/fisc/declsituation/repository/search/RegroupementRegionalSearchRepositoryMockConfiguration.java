package com.fisc.declsituation.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link RegroupementRegionalSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RegroupementRegionalSearchRepositoryMockConfiguration {

    @MockBean
    private RegroupementRegionalSearchRepository mockRegroupementRegionalSearchRepository;

}
