package be.condorcet.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un membre de la bibliothèque.
 * Un membre peut avoir plusieurs emprunts (relation OneToMany).
 */
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Le nom de famille est obligatoire")
    @Column(nullable = false)
    private String lastName;

    @NotNull(message = "La date d'inscription est obligatoire")
    @Column(nullable = false)
    private LocalDate membershipDate;

    @Column(nullable = false)
    private Boolean active = true;

    // Relation OneToMany avec Loan
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // éviter la sérialisation de la collection (lazy)
    private List<Loan> loans = new ArrayList<>();

    // Constructeurs
    public Member() {
    }

    public Member(String email, String firstName, String lastName, LocalDate membershipDate) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.membershipDate = membershipDate;
        this.active = true;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    // Méthode utilitaire pour ajouter un emprunt
    public void addLoan(Loan loan) {
        loans.add(loan);
        loan.setMember(this);
    }

    // Méthode utilitaire pour retirer un emprunt
    public void removeLoan(Loan loan) {
        loans.remove(loan);
        loan.setMember(null);
    }
}
