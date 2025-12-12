package be.condorcet.library.repository;

import be.condorcet.library.model.Book;
import be.condorcet.library.model.Author;
import be.condorcet.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour gérer les opérations CRUD sur les livres.
 * Spring Data JPA génère automatiquement les implémentations des méthodes.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Recherche un livre par son ISBN (unique).
     * @param isbn l'ISBN du livre
     * @return le livre trouvé, ou vide si non trouvé
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Vérifie si un livre existe avec cet ISBN.
     * @param isbn l'ISBN du livre
     * @return true si existe, false sinon
     */
    boolean existsByIsbn(String isbn);

    /**
     * Recherche les livres par titre (recherche partielle).
     * @param title le titre (ou partie du titre)
     * @return une liste de livres correspondant au titre
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Recherche les livres d'un auteur.
     * @param author l'auteur
     * @return une liste de livres de cet auteur
     */
    List<Book> findByAuthor(Author author);

    /**
     * Recherche les livres d'une catégorie.
     * @param category la catégorie
     * @return une liste de livres de cette catégorie
     */
    List<Book> findByCategories(Category category);

    /**
     * Recherche les livres disponibles (au moins 1 exemplaire disponible).
     * @return une liste de livres disponibles
     */
    List<Book> findByAvailableCopiesGreaterThan(int minAvailable);

    /**
     * Recherche les livres avec aucun exemplaire disponible.
     * @return une liste de livres indisponibles
     */
    List<Book> findByAvailableCopies(int copies);

    /**
     * Compte le nombre de livres disponibles.
     * @return le nombre de livres avec au moins 1 exemplaire disponible
     */
    long countByAvailableCopiesGreaterThan(int minAvailable);

    /**
     * Recherche les livres publiés après une certaine année.
     * @param year l'année
     * @return une liste de livres publiés après cette année
     */
    List<Book> findByPublicationYearGreaterThanEqual(Integer year);

    /**
     * Requête personnalisée pour rechercher les livres disponibles d'une catégorie.
     * @param categoryName le nom de la catégorie
     * @return une liste de livres disponibles de cette catégorie
     */
    @Query("SELECT DISTINCT b FROM Book b " +
           "JOIN b.categories c " +
           "WHERE c.name = :categoryName AND b.availableCopies > 0 " +
           "ORDER BY b.title")
    List<Book> findAvailableBooksByCategory(@Param("categoryName") String categoryName);
}
