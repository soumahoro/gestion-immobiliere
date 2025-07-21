package skc.immo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Proprietaire.
 */
@Entity
@Table(name = "proprietaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Proprietaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "idpro", nullable = false)
    private String idpro;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "nom")
    private String nom;

    @Column(name = "residence")
    private String residence;

    @Column(name = "tel")
    private String tel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Proprietaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdpro() {
        return this.idpro;
    }

    public Proprietaire idpro(String idpro) {
        this.setIdpro(idpro);
        return this;
    }

    public void setIdpro(String idpro) {
        this.idpro = idpro;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Proprietaire date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getNom() {
        return this.nom;
    }

    public Proprietaire nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getResidence() {
        return this.residence;
    }

    public Proprietaire residence(String residence) {
        this.setResidence(residence);
        return this;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getTel() {
        return this.tel;
    }

    public Proprietaire tel(String tel) {
        this.setTel(tel);
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proprietaire)) {
            return false;
        }
        return getId() != null && getId().equals(((Proprietaire) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proprietaire{" +
            "id=" + getId() +
            ", idpro='" + getIdpro() + "'" +
            ", date='" + getDate() + "'" +
            ", nom='" + getNom() + "'" +
            ", residence='" + getResidence() + "'" +
            ", tel='" + getTel() + "'" +
            "}";
    }
}
