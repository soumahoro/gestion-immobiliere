package skc.immo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Locataire.
 */
@Entity
@Table(name = "locataire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Locataire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "idloc", nullable = false)
    private String idloc;

    @Column(name = "arriere")
    private Integer arriere;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "datedepart")
    private ZonedDateTime datedepart;

    @Column(name = "fonction")
    private String fonction;

    @Column(name = "loyer")
    private Integer loyer;

    @Column(name = "motifdepart")
    private String motifdepart;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "nom")
    private String nom;

    @Column(name = "numpiece")
    private String numpiece;

    @Column(name = "observation")
    private String observation;

    @Column(name = "statut")
    private String statut;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "typepiece")
    private String typepiece;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "residence" }, allowSetters = true)
    private Appartement appartement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Locataire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdloc() {
        return this.idloc;
    }

    public Locataire idloc(String idloc) {
        this.setIdloc(idloc);
        return this;
    }

    public void setIdloc(String idloc) {
        this.idloc = idloc;
    }

    public Integer getArriere() {
        return this.arriere;
    }

    public Locataire arriere(Integer arriere) {
        this.setArriere(arriere);
        return this;
    }

    public void setArriere(Integer arriere) {
        this.arriere = arriere;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Locataire date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public ZonedDateTime getDatedepart() {
        return this.datedepart;
    }

    public Locataire datedepart(ZonedDateTime datedepart) {
        this.setDatedepart(datedepart);
        return this;
    }

    public void setDatedepart(ZonedDateTime datedepart) {
        this.datedepart = datedepart;
    }

    public String getFonction() {
        return this.fonction;
    }

    public Locataire fonction(String fonction) {
        this.setFonction(fonction);
        return this;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public Integer getLoyer() {
        return this.loyer;
    }

    public Locataire loyer(Integer loyer) {
        this.setLoyer(loyer);
        return this;
    }

    public void setLoyer(Integer loyer) {
        this.loyer = loyer;
    }

    public String getMotifdepart() {
        return this.motifdepart;
    }

    public Locataire motifdepart(String motifdepart) {
        this.setMotifdepart(motifdepart);
        return this;
    }

    public void setMotifdepart(String motifdepart) {
        this.motifdepart = motifdepart;
    }

    public String getNationalite() {
        return this.nationalite;
    }

    public Locataire nationalite(String nationalite) {
        this.setNationalite(nationalite);
        return this;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getNom() {
        return this.nom;
    }

    public Locataire nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumpiece() {
        return this.numpiece;
    }

    public Locataire numpiece(String numpiece) {
        this.setNumpiece(numpiece);
        return this;
    }

    public void setNumpiece(String numpiece) {
        this.numpiece = numpiece;
    }

    public String getObservation() {
        return this.observation;
    }

    public Locataire observation(String observation) {
        this.setObservation(observation);
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getStatut() {
        return this.statut;
    }

    public Locataire statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Locataire telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTypepiece() {
        return this.typepiece;
    }

    public Locataire typepiece(String typepiece) {
        this.setTypepiece(typepiece);
        return this;
    }

    public void setTypepiece(String typepiece) {
        this.typepiece = typepiece;
    }

    public Appartement getAppartement() {
        return this.appartement;
    }

    public void setAppartement(Appartement appartement) {
        this.appartement = appartement;
    }

    public Locataire appartement(Appartement appartement) {
        this.setAppartement(appartement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Locataire)) {
            return false;
        }
        return getId() != null && getId().equals(((Locataire) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Locataire{" +
            "id=" + getId() +
            ", idloc='" + getIdloc() + "'" +
            ", arriere=" + getArriere() +
            ", date='" + getDate() + "'" +
            ", datedepart='" + getDatedepart() + "'" +
            ", fonction='" + getFonction() + "'" +
            ", loyer=" + getLoyer() +
            ", motifdepart='" + getMotifdepart() + "'" +
            ", nationalite='" + getNationalite() + "'" +
            ", nom='" + getNom() + "'" +
            ", numpiece='" + getNumpiece() + "'" +
            ", observation='" + getObservation() + "'" +
            ", statut='" + getStatut() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", typepiece='" + getTypepiece() + "'" +
            "}";
    }
}
