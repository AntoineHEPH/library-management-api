package be.condorcet.library.repository;

import be.condorcet.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour gérer les opérations CRUD sur les auteurs.
 * Spring Data JPA génère automatiquement les implémentations des méthodes.
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Recherche un auteur par son nom de famille.
     * @param lastName le nom de famille
     * @return une liste d'auteurs ayant ce nom de famille
     */
    List<Author> findByLastName(String lastName);

    /**
     * Recherche un auteur par prénom ET nom de famille.
     * @param firstName le prénom
     * @param lastName le nom de famille
     * @return l'auteur trouvé, ou vide si non trouvé
     */
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);

    /**
     * Recherche les auteurs par nationalité.
     * @param nationality la nationalité
     * @return une liste d'auteurs de cette nationalité
     */
    List<Author> findByNationality(String nationality);

    /**
     * Vérifie si un auteur existe avec ce nom et prénom.
     * @param firstName le prénom
     * @param lastName le nom de famille
     * @return true si existe, false sinon
     */
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
