package be.condorcet.library.repository;

import be.condorcet.library.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour gérer les opérations CRUD sur les membres.
 * Spring Data JPA génère automatiquement les implémentations des méthodes.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Recherche un membre par son email (unique).
     * @param email l'email du membre
     * @return le membre trouvé, ou vide si non trouvé
     */
    Optional<Member> findByEmail(String email);

    /**
     * Vérifie si un membre existe avec cet email.
     * @param email l'email du membre
     * @return true si existe, false sinon
     */
    boolean existsByEmail(String email);

    /**
     * Compte le nombre de membres actifs.
     * @return le nombre de membres avec active = true
     */
    long countByActiveTrue();

    /**
     * Recherche tous les membres actifs.
     * @return une liste de membres actifs
     */
    java.util.List<Member> findByActiveTrue();

    /**
     * Recherche un membre par son prénom et nom.
     * @param firstName le prénom
     * @param lastName le nom de famille
     * @return le membre trouvé, ou vide si non trouvé
     */
    Optional<Member> findByFirstNameAndLastName(String firstName, String lastName);
}
