package be.condorcet.library.model;

import be.condorcet.library.model.enums.LoanStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Entité représentant un emprunt de livre par un membre.
 * Relations :
 * - ManyToOne avec Member (un emprunt appartient à un membre)
 * - ManyToOne avec Book (un emprunt concerne un livre)
 */
@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date d'emprunt est obligatoire")
    @Column(nullable = false)
    private LocalDate loanDate;

    @NotNull(message = "La date de retour prévue est obligatoire")
    @Column(nullable = false)
    private LocalDate dueDate;

    // Date de retour réelle (null si pas encore retourné)
    private LocalDate returnDate;

    @NotNull(message = "Le statut de l'emprunt est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    // Relation ManyToOne avec Member
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnoreProperties({"loans"})
    private Member member;

    // Relation ManyToOne avec Book
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @JsonIgnoreProperties({"loans", "categories"})
    private Book book;

    // Constructeurs
    public Loan() {
    }

    public Loan(LocalDate loanDate, LocalDate dueDate, Member member, Book book) {
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.member = member;
        this.book = book;
        this.status = LoanStatus.ACTIVE; // Par défaut, l'emprunt est actif
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Méthode utilitaire pour vérifier si l'emprunt est en retard.
     * Un emprunt est en retard si :
     * - Il n'a pas encore été retourné (returnDate == null)
     * - La date de retour prévue (dueDate) est dépassée
     */
    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }
}
