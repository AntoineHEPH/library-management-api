package be.condorcet.library.model.enums;

/**
 * Énumération représentant les différents statuts d'un emprunt.
 */
public enum LoanStatus {
    /**
     * Emprunt en cours (le livre n'a pas encore été retourné)
     */
    ACTIVE,

    /**
     * Emprunt terminé (le livre a été retourné)
     */
    RETURNED,

    /**
     * Emprunt en retard (la date de retour prévue est dépassée et le livre n'est pas encore retourné)
     */
    OVERDUE
}
