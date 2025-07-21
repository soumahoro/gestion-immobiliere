package skc.immo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reglement.
 */
@Entity
@Table(name = "reglement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reglement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "idreg")
    private Integer idreg;

    @Column(name = "annee")
    private Integer annee;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "montant")
    private Integer montant;

    @Lob
    @Column(name = "montantlettres")
    private String montantlettres;

    @Column(name = "motif")
    private String motif;

    @Column(name = "observ_1")
    private String observ1;

    @Column(name = "observ_2")
    private String observ2;

    @Column(name = "observ_3")
    private String observ3;

    @Column(name = "reste")
    private Integer reste;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "appartement" }, allowSetters = true)
    private Locataire locataire;

    @ManyToOne(fetch = FetchType.LAZY)
    private Moi moi;

    @ManyToOne(fetch = FetchType.LAZY)
    private Utilisateur utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reglement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdreg() {
        return this.idreg;
    }

    public Reglement idreg(Integer idreg) {
        this.setIdreg(idreg);
        return this;
    }

    public void setIdreg(Integer idreg) {
        this.idreg = idreg;
    }

    public Integer getAnnee() {
        return this.annee;
    }

    public Reglement annee(Integer annee) {
        this.setAnnee(annee);
        return this;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Reglement date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getMontant() {
        return this.montant;
    }

    public Reglement montant(Integer montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Integer montant) {
        this.montant = montant;
    }

    public String getMontantlettres() {
        return this.montantlettres;
    }

    public Reglement montantlettres(String montantlettres) {
        this.setMontantlettres(montantlettres);
        return this;
    }

    public void setMontantlettres(String montantlettres) {
        this.montantlettres = montantlettres;
    }

    public String getMotif() {
        return this.motif;
    }

    public Reglement motif(String motif) {
        this.setMotif(motif);
        return this;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getObserv1() {
        return this.observ1;
    }

    public Reglement observ1(String observ1) {
        this.setObserv1(observ1);
        return this;
    }

    public void setObserv1(String observ1) {
        this.observ1 = observ1;
    }

    public String getObserv2() {
        return this.observ2;
    }

    public Reglement observ2(String observ2) {
        this.setObserv2(observ2);
        return this;
    }

    public void setObserv2(String observ2) {
        this.observ2 = observ2;
    }

    public String getObserv3() {
        return this.observ3;
    }

    public Reglement observ3(String observ3) {
        this.setObserv3(observ3);
        return this;
    }

    public void setObserv3(String observ3) {
        this.observ3 = observ3;
    }

    public Integer getReste() {
        return this.reste;
    }

    public Reglement reste(Integer reste) {
        this.setReste(reste);
        return this;
    }

    public void setReste(Integer reste) {
        this.reste = reste;
    }

    public Locataire getLocataire() {
        return this.locataire;
    }

    public void setLocataire(Locataire locataire) {
        this.locataire = locataire;
    }

    public Reglement locataire(Locataire locataire) {
        this.setLocataire(locataire);
        return this;
    }

    public Moi getMoi() {
        return this.moi;
    }

    public void setMoi(Moi moi) {
        this.moi = moi;
    }

    public Reglement moi(Moi moi) {
        this.setMoi(moi);
        return this;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Reglement utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reglement)) {
            return false;
        }
        return getId() != null && getId().equals(((Reglement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reglement{" +
            "id=" + getId() +
            ", idreg=" + getIdreg() +
            ", annee=" + getAnnee() +
            ", date='" + getDate() + "'" +
            ", montant=" + getMontant() +
            ", montantlettres='" + getMontantlettres() + "'" +
            ", motif='" + getMotif() + "'" +
            ", observ1='" + getObserv1() + "'" +
            ", observ2='" + getObserv2() + "'" +
            ", observ3='" + getObserv3() + "'" +
            ", reste=" + getReste() +
            "}";
    }
}
