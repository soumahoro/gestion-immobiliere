package skc.immo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Residence.
 */
@Entity
@Table(name = "residence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Residence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "idres", nullable = false)
    private String idres;

    @Column(name = "ilot")
    private String ilot;

    @Column(name = "localisation")
    private String localisation;

    @Column(name = "observation")
    private String observation;

    @Column(name = "quartier")
    private String quartier;

    @Column(name = "ville")
    private String ville;

    @ManyToOne(fetch = FetchType.LAZY)
    private Proprietaire proprietaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Residence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdres() {
        return this.idres;
    }

    public Residence idres(String idres) {
        this.setIdres(idres);
        return this;
    }

    public void setIdres(String idres) {
        this.idres = idres;
    }

    public String getIlot() {
        return this.ilot;
    }

    public Residence ilot(String ilot) {
        this.setIlot(ilot);
        return this;
    }

    public void setIlot(String ilot) {
        this.ilot = ilot;
    }

    public String getLocalisation() {
        return this.localisation;
    }

    public Residence localisation(String localisation) {
        this.setLocalisation(localisation);
        return this;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getObservation() {
        return this.observation;
    }

    public Residence observation(String observation) {
        this.setObservation(observation);
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getQuartier() {
        return this.quartier;
    }

    public Residence quartier(String quartier) {
        this.setQuartier(quartier);
        return this;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public String getVille() {
        return this.ville;
    }

    public Residence ville(String ville) {
        this.setVille(ville);
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Proprietaire getProprietaire() {
        return this.proprietaire;
    }

    public void setProprietaire(Proprietaire proprietaire) {
        this.proprietaire = proprietaire;
    }

    public Residence proprietaire(Proprietaire proprietaire) {
        this.setProprietaire(proprietaire);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Residence)) {
            return false;
        }
        return getId() != null && getId().equals(((Residence) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Residence{" +
            "id=" + getId() +
            ", idres='" + getIdres() + "'" +
            ", ilot='" + getIlot() + "'" +
            ", localisation='" + getLocalisation() + "'" +
            ", observation='" + getObservation() + "'" +
            ", quartier='" + getQuartier() + "'" +
            ", ville='" + getVille() + "'" +
            "}";
    }
}
