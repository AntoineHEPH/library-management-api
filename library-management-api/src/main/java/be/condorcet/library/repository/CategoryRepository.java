package be.condorcet.library.repository;

import be.condorcet.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour gérer les opérations CRUD sur les catégories.
 * Spring Data JPA génère automatiquement les implémentations des méthodes.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Recherche une catégorie par son nom (unique).
     * @param name le nom de la catégorie
     * @return la catégorie trouvée, ou vide si non trouvée
     */
    Optional<Category> findByName(String name);

    /**
     * Vérifie si une catégorie existe avec ce nom.
     * @param name le nom de la catégorie
     * @return true si existe, false sinon
     */
    boolean existsByName(String name);
}
