package be.condorcet.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un auteur de livres.
 * Un auteur peut écrire plusieurs livres (relation OneToMany).
 */
@Entity
@Table(name = "authors")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Le nom de famille est obligatoire")
    @Column(nullable = false)
    private String lastName;

    private String nationality;

    @Min(value = 1000, message = "L'année de naissance doit être supérieure à 1000")
    private Integer birthYear;

    // Relation OneToMany avec Book
    // mappedBy = "author" : indique que c'est Book qui possède la clé étrangère
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // éviter la sérialisation de la collection (lazy) pour ne pas déclencher d'erreur 500
    private List<Book> books = new ArrayList<>();

    // Constructeurs
    public Author() {
    }

    public Author(String firstName, String lastName, String nationality, Integer birthYear) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.birthYear = birthYear;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    // Méthode utilitaire pour ajouter un livre
    public void addBook(Book book) {
        books.add(book);
        book.setAuthor(this);
    }

    // Méthode utilitaire pour retirer un livre
    public void removeBook(Book book) {
        books.remove(book);
        book.setAuthor(null);
    }
}
