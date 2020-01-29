package com.fisc.declsituation.domain;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Continent entity.\n@author sanda\nC'est l'ensemble des cinq continents du monde
 */
@ApiModel(description = "Continent entity.\n@author sanda\nC'est l'ensemble des cinq continents du monde")
@Entity
@Table(name = "continent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "continent")
public class Continent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "nom_continent")
    private String nomContinent;

    @Column(name = "superficie")
    private Double superficie;

    @OneToMany(mappedBy = "continent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Pays> pays = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomContinent() {
        return nomContinent;
    }

    public Continent nomContinent(String nomContinent) {
        this.nomContinent = nomContinent;
        return this;
    }

    public void setNomContinent(String nomContinent) {
        this.nomContinent = nomContinent;
    }

    public Double getSuperficie() {
        return superficie;
    }

    public Continent superficie(Double superficie) {
        this.superficie = superficie;
        return this;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public Set<Pays> getPays() {
        return pays;
    }

    public Continent pays(Set<Pays> pays) {
        this.pays = pays;
        return this;
    }

    public Continent addPays(Pays pays) {
        this.pays.add(pays);
        pays.setContinent(this);
        return this;
    }

    public Continent removePays(Pays pays) {
        this.pays.remove(pays);
        pays.setContinent(null);
        return this;
    }

    public void setPays(Set<Pays> pays) {
        this.pays = pays;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Continent)) {
            return false;
        }
        return id != null && id.equals(((Continent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Continent{" +
            "id=" + getId() +
            ", nomContinent='" + getNomContinent() + "'" +
            ", superficie=" + getSuperficie() +
            "}";
    }
}
