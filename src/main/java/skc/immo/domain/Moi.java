package skc.immo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Moi.
 */
@Entity
@Table(name = "moi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Moi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "idmois", nullable = false)
    private Integer idmois;

    @Column(name = "mois")
    private String mois;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Moi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdmois() {
        return this.idmois;
    }

    public Moi idmois(Integer idmois) {
        this.setIdmois(idmois);
        return this;
    }

    public void setIdmois(Integer idmois) {
        this.idmois = idmois;
    }

    public String getMois() {
        return this.mois;
    }

    public Moi mois(String mois) {
        this.setMois(mois);
        return this;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Moi)) {
            return false;
        }
        return getId() != null && getId().equals(((Moi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Moi{" +
            "id=" + getId() +
            ", idmois=" + getIdmois() +
            ", mois='" + getMois() + "'" +
            "}";
    }
}
