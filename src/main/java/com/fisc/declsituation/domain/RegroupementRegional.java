package com.fisc.declsituation.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * RegroupementRegional entity.\n@author sanda\nC'est le découpage temporel\nrelation\n- OneToMany avec Pays
 */
@ApiModel(description = "RegroupementRegional entity.\n@author sanda\nC'est le découpage temporel\nrelation\n- OneToMany avec Pays")
@Entity
@Table(name = "regroupement_regional")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "regroupementregional")
public class RegroupementRegional implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "sigle")
    private String sigle;

    @Column(name = "nom_regroupement")
    private String nomRegroupement;

    @OneToMany(mappedBy = "regroupementRegional")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Pays> pays = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("regroupementRegionals")
    private Pays pays;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSigle() {
        return sigle;
    }

    public RegroupementRegional sigle(String sigle) {
        this.sigle = sigle;
        return this;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public String getNomRegroupement() {
        return nomRegroupement;
    }

    public RegroupementRegional nomRegroupement(String nomRegroupement) {
        this.nomRegroupement = nomRegroupement;
        return this;
    }

    public void setNomRegroupement(String nomRegroupement) {
        this.nomRegroupement = nomRegroupement;
    }

    public Set<Pays> getPays() {
        return pays;
    }

    public RegroupementRegional pays(Set<Pays> pays) {
        this.pays = pays;
        return this;
    }

    public RegroupementRegional addPays(Pays pays) {
        this.pays.add(pays);
        pays.setRegroupementRegional(this);
        return this;
    }

    public RegroupementRegional removePays(Pays pays) {
        this.pays.remove(pays);
        pays.setRegroupementRegional(null);
        return this;
    }

    public void setPays(Set<Pays> pays) {
        this.pays = pays;
    }

    public Pays getPays() {
        return pays;
    }

    public RegroupementRegional pays(Pays pays) {
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
        if (!(o instanceof RegroupementRegional)) {
            return false;
        }
        return id != null && id.equals(((RegroupementRegional) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RegroupementRegional{" +
            "id=" + getId() +
            ", sigle='" + getSigle() + "'" +
            ", nomRegroupement='" + getNomRegroupement() + "'" +
            "}";
    }
}
