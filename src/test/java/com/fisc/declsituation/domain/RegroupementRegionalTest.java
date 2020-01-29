package com.fisc.declsituation.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.fisc.declsituation.web.rest.TestUtil;

public class RegroupementRegionalTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegroupementRegional.class);
        RegroupementRegional regroupementRegional1 = new RegroupementRegional();
        regroupementRegional1.setId(1L);
        RegroupementRegional regroupementRegional2 = new RegroupementRegional();
        regroupementRegional2.setId(regroupementRegional1.getId());
        assertThat(regroupementRegional1).isEqualTo(regroupementRegional2);
        regroupementRegional2.setId(2L);
        assertThat(regroupementRegional1).isNotEqualTo(regroupementRegional2);
        regroupementRegional1.setId(null);
        assertThat(regroupementRegional1).isNotEqualTo(regroupementRegional2);
    }
}
