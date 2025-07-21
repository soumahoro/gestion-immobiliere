package skc.immo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Appartement.
 */
@Entity
@Table(name = "appartement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Appartement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "idapp", nullable = false)
    private String idapp;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "loyer")
    private Integer loyer;

    @Column(name = "nbrepieces")
    private Integer nbrepieces;

    @Column(name = "taux")
    private Integer taux;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "proprietaire" }, allowSetters = true)
    private Residence residence;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Appartement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdapp() {
        return this.idapp;
    }

    public Appartement idapp(String idapp) {
        this.setIdapp(idapp);
        return this;
    }

    public void setIdapp(String idapp) {
        this.idapp = idapp;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Appartement libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getLoyer() {
        return this.loyer;
    }

    public Appartement loyer(Integer loyer) {
        this.setLoyer(loyer);
        return this;
    }

    public void setLoyer(Integer loyer) {
        this.loyer = loyer;
    }

    public Integer getNbrepieces() {
        return this.nbrepieces;
    }

    public Appartement nbrepieces(Integer nbrepieces) {
        this.setNbrepieces(nbrepieces);
        return this;
    }

    public void setNbrepieces(Integer nbrepieces) {
        this.nbrepieces = nbrepieces;
    }

    public Integer getTaux() {
        return this.taux;
    }

    public Appartement taux(Integer taux) {
        this.setTaux(taux);
        return this;
    }

    public void setTaux(Integer taux) {
        this.taux = taux;
    }

    public Residence getResidence() {
        return this.residence;
    }

    public void setResidence(Residence residence) {
        this.residence = residence;
    }

    public Appartement residence(Residence residence) {
        this.setResidence(residence);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appartement)) {
            return false;
        }
        return getId() != null && getId().equals(((Appartement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Appartement{" +
            "id=" + getId() +
            ", idapp='" + getIdapp() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", loyer=" + getLoyer() +
            ", nbrepieces=" + getNbrepieces() +
            ", taux=" + getTaux() +
            "}";
    }
}
