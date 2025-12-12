package be.condorcet.library.controller;

import be.condorcet.library.model.Loan;
import be.condorcet.library.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour gérer les emprunts de livres.
 * Endpoints pour créer, retourner, et rechercher des emprunts.
 * 
 * IMPORTANT : Ce contrôleur applique les règles métier complexes via le LoanService.
 */
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * GET /api/loans - Récupère tous les emprunts
     */
    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    /**
     * GET /api/loans/{id} - Récupère un emprunt par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        Loan loan = loanService.getLoanById(id);
        return ResponseEntity.ok(loan);
    }

    /**
     * POST /api/loans - Crée un nouvel emprunt (APPLIQUE LES RÈGLES MÉTIER)
     * 
     * Body JSON attendu :
     * {
     *   "memberId": 1,
     *   "bookId": 5,
     *   "dueDate": "2025-12-31"
     * }
     * 
     * Règles vérifiées :
     * - Le membre existe et est actif
     * - Le livre existe
     * - Le livre a des exemplaires disponibles
     * - Le membre n'a pas déjà 3 emprunts actifs
     * - Le membre n'a pas déjà emprunté ce livre
     */
    @PostMapping
    public ResponseEntity<?> createLoan(
            @RequestParam Long memberId,
            @RequestParam Long bookId,
            @RequestParam LocalDate dueDate) {
        try {
            Loan createdLoan = loanService.createLoan(memberId, bookId, dueDate);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * POST /api/loans/{id}/return - Retourne un livre (enregistre le retour)
     */
    @PostMapping("/{id}/return")
    public ResponseEntity<Loan> returnBook(@PathVariable Long id) {
        Loan returnedLoan = loanService.returnBook(id);
        return ResponseEntity.ok(returnedLoan);
    }

    /**
     * GET /api/loans/member/{memberId} - Récupère tous les emprunts d'un membre
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Loan>> getLoansByMember(@PathVariable Long memberId) {
        List<Loan> loans = loanService.getLoansByMember(memberId);
        return ResponseEntity.ok(loans);
    }

    /**
     * GET /api/loans/member/{memberId}/active - Récupère les emprunts actifs d'un membre
     */
    @GetMapping("/member/{memberId}/active")
    public ResponseEntity<List<Loan>> getActiveLoansByMember(@PathVariable Long memberId) {
        List<Loan> loans = loanService.getActiveLoansByMember(memberId);
        return ResponseEntity.ok(loans);
    }

    /**
     * GET /api/loans/book/{bookId} - Récupère tous les emprunts d'un livre
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Loan>> getLoansByBook(@PathVariable Long bookId) {
        List<Loan> loans = loanService.getLoansByBook(bookId);
        return ResponseEntity.ok(loans);
    }

    /**
     * GET /api/loans/overdue - Récupère tous les emprunts en retard
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<Loan>> getOverdueLoans() {
        List<Loan> loans = loanService.getOverdueLoans();
        return ResponseEntity.ok(loans);
    }

    /**
     * GET /api/loans/stats/member/{memberId}/active-count - Compte les emprunts actifs d'un membre
     */
    @GetMapping("/stats/member/{memberId}/active-count")
    public ResponseEntity<Long> countActiveLoansByMember(@PathVariable Long memberId) {
        long count = loanService.countActiveLoansByMember(memberId);
        return ResponseEntity.ok(count);
    }

    /**
     * GET /api/loans/stats/member/{memberId}/total-count - Compte tous les emprunts d'un membre
     */
    @GetMapping("/stats/member/{memberId}/total-count")
    public ResponseEntity<Long> countTotalLoansByMember(@PathVariable Long memberId) {
        long count = loanService.countTotalLoansByMember(memberId);
        return ResponseEntity.ok(count);
    }

    /**
     * GET /api/loans/quota/member/{memberId} - Retourne le quota d'emprunts restant pour un membre
     * (combien de livres il peut encore emprunter avant d'atteindre la limite de 3)
     */
    @GetMapping("/quota/member/{memberId}")
    public ResponseEntity<Map<String, Object>> getRemainingBorrowQuota(@PathVariable Long memberId) {
        int remainingQuota = loanService.getRemainingBorrowQuota(memberId);
        boolean canBorrow = loanService.canMemberBorrow(memberId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("memberId", memberId);
        response.put("remainingQuota", remainingQuota);
        response.put("canBorrow", canBorrow);
        response.put("maxLoansPerMember", 3);
        
        return ResponseEntity.ok(response);
    }
}
