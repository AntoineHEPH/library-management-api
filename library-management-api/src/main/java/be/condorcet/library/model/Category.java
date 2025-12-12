package be.condorcet.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant une catégorie de livres.
 * Une catégorie peut contenir plusieurs livres, et un livre peut appartenir à plusieurs catégories (relation ManyToMany).
 */
@Entity
@Table(name = "categories")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de la catégorie est obligatoire")
    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    // Relation ManyToMany avec Book
    // mappedBy = "categories" : c'est Book qui gère la table de jointure
    @ManyToMany(mappedBy = "categories")
    @JsonIgnore // éviter la sérialisation de la collection (lazy)
    private Set<Book> books = new HashSet<>();

    // Constructeurs
    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
