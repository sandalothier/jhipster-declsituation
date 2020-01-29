package com.fisc.declsituation.repository;

import com.fisc.declsituation.domain.Monnaie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Monnaie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonnaieRepository extends JpaRepository<Monnaie, Long> {

}
