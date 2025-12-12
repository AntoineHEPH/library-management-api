package be.condorcet.library.repository;

import be.condorcet.library.model.Loan;
import be.condorcet.library.model.Member;
import be.condorcet.library.model.Book;
import be.condorcet.library.model.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour gérer les opérations CRUD sur les emprunts.
 * Ce repository est crucial pour implémenter les règles métier.
 * Spring Data JPA génère automatiquement les implémentations des méthodes.
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    /**
     * Recherche tous les emprunts actifs d'un membre.
     * @param member le membre
     * @return une liste d'emprunts actifs de ce membre
     */
    List<Loan> findByMemberAndStatus(Member member, LoanStatus status);

    /**
     * Compte le nombre d'emprunts actifs d'un membre.
     * IMPORTANT : utilisé pour vérifier la limite de 3 emprunts simultanés.
     * @param member le membre
     * @return le nombre d'emprunts actifs
     */
    long countByMemberAndStatus(Member member, LoanStatus status);

    /**
     * Recherche tous les emprunts d'un membre.
     * @param member le membre
     * @return une liste de tous les emprunts du membre
     */
    List<Loan> findByMember(Member member);

    /**
     * Recherche tous les emprunts d'un livre.
     * @param book le livre
     * @return une liste d'emprunts de ce livre
     */
    List<Loan> findByBook(Book book);

    /**
     * Recherche les emprunts actifs d'un livre.
     * @param book le livre
     * @return une liste d'emprunts actifs de ce livre
     */
    List<Loan> findByBookAndStatus(Book book, LoanStatus status);

    /**
     * Recherche les emprunts en retard (status OVERDUE).
     * @return une liste d'emprunts en retard
     */
    List<Loan> findByStatus(LoanStatus status);

    /**
     * Recherche les emprunts dont la date de retour prévue est dépassée et statut actif.
     * @param today la date du jour
     * @return une liste d'emprunts en retard
     */
    List<Loan> findByDueDateBeforeAndStatus(LocalDate today, LoanStatus status);

    /**
     * Vérifie si un emprunt actif existe pour un livre et un membre.
     * @param member le membre
     * @param book le livre
     * @return true si un emprunt actif existe, false sinon
     */
    boolean existsByMemberAndBookAndStatus(Member member, Book book, LoanStatus status);

    /**
     * Recherche l'emprunt actif d'un livre par un membre (s'il existe).
     * @param member le membre
     * @param book le livre
     * @param status le statut
     * @return l'emprunt trouvé, ou vide si non trouvé
     */
    Optional<Loan> findByMemberAndBookAndStatus(Member member, Book book, LoanStatus status);

    /**
     * Requête personnalisée pour obtenir les statistiques d'emprunts par membre.
     * @param memberId l'ID du membre
     * @return le nombre total d'emprunts du membre
     */
    @Query("SELECT COUNT(l) FROM Loan l WHERE l.member.id = :memberId")
    long countTotalLoansByMember(@Param("memberId") Long memberId);

    /**
     * Requête personnalisée pour obtenir les emprunts d'un membre triés par date.
     * @param member le membre
     * @return une liste d'emprunts triés par date de prêt décroissante
     */
    @Query("SELECT l FROM Loan l WHERE l.member = :member ORDER BY l.loanDate DESC")
    List<Loan> findLoansByMemberOrderByDate(@Param("member") Member member);
}
