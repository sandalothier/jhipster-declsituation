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
 * Pays entity.\n@author sanda\nC'est l'ensemble des pays qui compose chaque continent\nrelation\n- OneToMany avec Region\n- ManyToOne avec Continent\n- OneToMany avec RegroupementRegional\n- ManyToOne avec Langue\n- ManyToOne avec Monnaie\n
 */
@ApiModel(description = "Pays entity.\n@author sanda\nC'est l'ensemble des pays qui compose chaque continent\nrelation\n- OneToMany avec Region\n- ManyToOne avec Continent\n- OneToMany avec RegroupementRegional\n- ManyToOne avec Langue\n- ManyToOne avec Monnaie\n")
@Entity
@Table(name = "pays")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "pays")
public class Pays implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "nom_pays")
    private String nomPays;

    @Column(name = "superficie")
    private Double superficie;

    @Column(name = "sigle_auto")
    private String sigleAuto;

    @Column(name = "capitale")
    private String capitale;

    @OneToOne
    @JoinColumn(unique = true)
    private Monnaie monnaie;

    @OneToMany(mappedBy = "pays")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Regions> regions = new HashSet<>();

    @OneToMany(mappedBy = "pays")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RegroupementRegional> regroupementRegionals = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("pays")
    private Continent continent;

    @ManyToOne
    @JsonIgnoreProperties("pays")
    private RegroupementRegional regroupementRegional;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomPays() {
        return nomPays;
    }

    public Pays nomPays(String nomPays) {
        this.nomPays = nomPays;
        return this;
    }

    public void setNomPays(String nomPays) {
        this.nomPays = nomPays;
    }

    public Double getSuperficie() {
        return superficie;
    }

    public Pays superficie(Double superficie) {
        this.superficie = superficie;
        return this;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public String getSigleAuto() {
        return sigleAuto;
    }

    public Pays sigleAuto(String sigleAuto) {
        this.sigleAuto = sigleAuto;
        return this;
    }

    public void setSigleAuto(String sigleAuto) {
        this.sigleAuto = sigleAuto;
    }

    public String getCapitale() {
        return capitale;
    }

    public Pays capitale(String capitale) {
        this.capitale = capitale;
        return this;
    }

    public void setCapitale(String capitale) {
        this.capitale = capitale;
    }

    public Monnaie getMonnaie() {
        return monnaie;
    }

    public Pays monnaie(Monnaie monnaie) {
        this.monnaie = monnaie;
        return this;
    }

    public void setMonnaie(Monnaie monnaie) {
        this.monnaie = monnaie;
    }

    public Set<Regions> getRegions() {
        return regions;
    }

    public Pays regions(Set<Regions> regions) {
        this.regions = regions;
        return this;
    }

    public Pays addRegions(Regions regions) {
        this.regions.add(regions);
        regions.setPays(this);
        return this;
    }

    public Pays removeRegions(Regions regions) {
        this.regions.remove(regions);
        regions.setPays(null);
        return this;
    }

    public void setRegions(Set<Regions> regions) {
        this.regions = regions;
    }

    public Set<RegroupementRegional> getRegroupementRegionals() {
        return regroupementRegionals;
    }

    public Pays regroupementRegionals(Set<RegroupementRegional> regroupementRegionals) {
        this.regroupementRegionals = regroupementRegionals;
        return this;
    }

    public Pays addRegroupementRegional(RegroupementRegional regroupementRegional) {
        this.regroupementRegionals.add(regroupementRegional);
        regroupementRegional.setPays(this);
        return this;
    }

    public Pays removeRegroupementRegional(RegroupementRegional regroupementRegional) {
        this.regroupementRegionals.remove(regroupementRegional);
        regroupementRegional.setPays(null);
        return this;
    }

    public void setRegroupementRegionals(Set<RegroupementRegional> regroupementRegionals) {
        this.regroupementRegionals = regroupementRegionals;
    }

    public Continent getContinent() {
        return continent;
    }

    public Pays continent(Continent continent) {
        this.continent = continent;
        return this;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public RegroupementRegional getRegroupementRegional() {
        return regroupementRegional;
    }

    public Pays regroupementRegional(RegroupementRegional regroupementRegional) {
        this.regroupementRegional = regroupementRegional;
        return this;
    }

    public void setRegroupementRegional(RegroupementRegional regroupementRegional) {
        this.regroupementRegional = regroupementRegional;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pays)) {
            return false;
        }
        return id != null && id.equals(((Pays) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Pays{" +
            "id=" + getId() +
            ", nomPays='" + getNomPays() + "'" +
            ", superficie=" + getSuperficie() +
            ", sigleAuto='" + getSigleAuto() + "'" +
            ", capitale='" + getCapitale() + "'" +
            "}";
    }
}
