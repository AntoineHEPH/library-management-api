package be.condorcet.library.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entité représentant un livre dans le catalogue de la bibliothèque.
 * Relations :
 * - ManyToOne avec Author (un livre a un auteur principal)
 * - ManyToMany avec Category (un livre peut appartenir à plusieurs catégories)
 * - OneToMany avec Loan (un livre peut être emprunté plusieurs fois)
 */
@Entity
@Table(name = "books")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "L'ISBN est obligatoire")
    @Column(nullable = false, unique = true)
    private String isbn;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false)
    private String title;

    @Min(value = 1000, message = "L'année de publication doit être supérieure à 1000")
    private Integer publicationYear;

    @NotNull(message = "Le nombre d'exemplaires disponibles est obligatoire")
    @Min(value = 0, message = "Le nombre d'exemplaires disponibles ne peut pas être négatif")
    @Column(nullable = false)
    private Integer availableCopies;

    @NotNull(message = "Le nombre total d'exemplaires est obligatoire")
    @Min(value = 1, message = "Le nombre total d'exemplaires doit être au moins 1")
    @Column(nullable = false)
    private Integer totalCopies;

    // Relation ManyToOne avec Author
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnoreProperties({"books"})
    private Author author;

    // Relation ManyToMany avec Category
    @ManyToMany
    @JoinTable(
        name = "book_categories",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonIgnoreProperties({"books"})
    private Set<Category> categories = new HashSet<>();

    // Relation OneToMany avec Loan
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"book", "member"})
    @com.fasterxml.jackson.annotation.JsonIgnore // ne pas sérialiser les emprunts pour éviter le chargement lazy
    private List<Loan> loans = new ArrayList<>();

    // Constructeurs
    public Book() {
    }

    public Book(String isbn, String title, Integer publicationYear, Integer totalCopies, Author author) {
        this.isbn = isbn;
        this.title = title;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies; // Par défaut, tous les exemplaires sont disponibles
        this.author = author;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    // Méthodes utilitaires pour gérer les catégories
    public void addCategory(Category category) {
        this.categories.add(category);
        category.getBooks().add(this);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.getBooks().remove(this);
    }

    // Méthodes utilitaires pour gérer les emprunts
    public void addLoan(Loan loan) {
        loans.add(loan);
        loan.setBook(this);
    }

    public void removeLoan(Loan loan) {
        loans.remove(loan);
        loan.setBook(null);
    }
}
