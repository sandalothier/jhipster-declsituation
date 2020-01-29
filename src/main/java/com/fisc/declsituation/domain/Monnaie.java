package com.fisc.declsituation.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * Monnaie entity.\n@author sanda\nC'est l'ensemble des composante du territoire
 */
@ApiModel(description = "Monnaie entity.\n@author sanda\nC'est l'ensemble des composante du territoire")
@Entity
@Table(name = "monnaie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "monnaie")
public class Monnaie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "monnaie")
    private String monnaie;

    @Column(name = "sigle")
    private String sigle;

    @OneToOne(mappedBy = "monnaie")
    @JsonIgnore
    private Pays pays;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMonnaie() {
        return monnaie;
    }

    public Monnaie monnaie(String monnaie) {
        this.monnaie = monnaie;
        return this;
    }

    public void setMonnaie(String monnaie) {
        this.monnaie = monnaie;
    }

    public String getSigle() {
        return sigle;
    }

    public Monnaie sigle(String sigle) {
        this.sigle = sigle;
        return this;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public Pays getPays() {
        return pays;
    }

    public Monnaie pays(Pays pays) {
        this.pays = pays;
        return this;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Monnaie)) {
            return false;
        }
        return id != null && id.equals(((Monnaie) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Monnaie{" +
            "id=" + getId() +
            ", monnaie='" + getMonnaie() + "'" +
            ", sigle='" + getSigle() + "'" +
            "}";
    }
}
