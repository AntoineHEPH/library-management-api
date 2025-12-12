package be.condorcet.library.service;

import be.condorcet.library.model.Loan;
import be.condorcet.library.model.Member;
import be.condorcet.library.model.Book;
import be.condorcet.library.model.enums.LoanStatus;
import be.condorcet.library.repository.LoanRepository;
import be.condorcet.library.exception.ResourceNotFoundException;
import be.condorcet.library.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service pour gérer les emprunts de livres.
 * Contient les règles métier complexes de la bibliothèque.
 * 
 * RÈGLES MÉTIER PRINCIPALES :
 * 1. Un membre ne peut pas emprunter plus de 3 livres simultanément
 * 2. On ne peut emprunter que si le livre a des exemplaires disponibles
 * 3. Un emprunt devient OVERDUE si la date limite est dépassée et le livre n'est pas rendu
 */
@Service
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final MemberService memberService;
    private final BookService bookService;

    // Constante : limite d'emprunts actifs par membre
    private static final int MAX_ACTIVE_LOANS_PER_MEMBER = 3;

    public LoanService(LoanRepository loanRepository, MemberService memberService, BookService bookService) {
        this.loanRepository = loanRepository;
        this.memberService = memberService;
        this.bookService = bookService;
    }

    /**
     * Récupère tous les emprunts.
     */
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    /**
     * Récupère un emprunt par son ID.
     */
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emprunt avec l'ID " + id + " non trouvé"));
    }

    /**
     * Crée un nouvel emprunt (RÈGLE MÉTIER IMPORTANTE).
     * 
     * Validations :
     * - Le membre existe et est actif
     * - Le livre existe
     * - Le livre a des exemplaires disponibles
     * - Le membre n'a pas déjà 3 emprunts actifs
     * - Le membre n'a pas déjà emprunté ce livre (sans l'avoir rendu)
     */
    public Loan createLoan(Long memberId, Long bookId, LocalDate dueDate) {
        // Vérifier que le membre existe et est actif
        Member member = memberService.getMemberById(memberId);
        if (!member.getActive()) {
            throw new BusinessException("Le compte du membre est suspendu. Impossible d'emprunter.");
        }

        // Vérifier que le livre existe
        Book book = bookService.getBookById(bookId);

        // RÈGLE 1 : Vérifier que le livre a des exemplaires disponibles
        if (book.getAvailableCopies() <= 0) {
            throw new BusinessException("Aucun exemplaire disponible pour le livre '" + book.getTitle() + "'");
        }

        // RÈGLE 2 : Vérifier que le membre n'a pas déjà 3 emprunts actifs
        long activeLoans = loanRepository.countByMemberAndStatus(member, LoanStatus.ACTIVE);
        if (activeLoans >= MAX_ACTIVE_LOANS_PER_MEMBER) {
            throw new BusinessException("Le membre a déjà atteint la limite de " + MAX_ACTIVE_LOANS_PER_MEMBER + 
                    " emprunts actifs simultanés");
        }

        // RÈGLE 3 : Vérifier que le membre n'a pas déjà cet exemplaire emprunté (sans l'avoir rendu)
        if (loanRepository.existsByMemberAndBookAndStatus(member, book, LoanStatus.ACTIVE)) {
            throw new BusinessException("Le membre a déjà emprunté ce livre et ne l'a pas encore rendu");
        }

        // Créer l'emprunt
        Loan loan = new Loan(LocalDate.now(), dueDate, member, book);
        loan.setStatus(LoanStatus.ACTIVE);

        // Décrémenter le nombre d'exemplaires disponibles
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookService.updateBook(book.getId(), book);

        return loanRepository.save(loan);
    }

    /**
     * Retourne un livre (enregistre le retour).
     */
    public Loan returnBook(Long loanId) {
        Loan loan = getLoanById(loanId);

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new BusinessException("Ce livre a déjà été retourné");
        }

        // Enregistrer la date de retour
        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);

        // Incrémenter le nombre d'exemplaires disponibles
        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookService.updateBook(book.getId(), book);

        return loanRepository.save(loan);
    }

    /**
     * Met à jour le statut des emprunts (vérifie et marque les retards).
     * Cette méthode doit être appelée régulièrement (par une tâche planifiée).
     */
    @Transactional
    public void updateOverdueLoans() {
        List<Loan> activeLoans = loanRepository.findByStatus(LoanStatus.ACTIVE);

        for (Loan loan : activeLoans) {
            if (loan.isOverdue()) {
                loan.setStatus(LoanStatus.OVERDUE);
                loanRepository.save(loan);
            }
        }
    }

    /**
     * Récupère les emprunts actifs d'un membre.
     */
    public List<Loan> getActiveLoansByMember(Long memberId) {
        Member member = memberService.getMemberById(memberId);
        return loanRepository.findByMemberAndStatus(member, LoanStatus.ACTIVE);
    }

    /**
     * Récupère tous les emprunts d'un membre.
     */
    public List<Loan> getLoansByMember(Long memberId) {
        Member member = memberService.getMemberById(memberId);
        return loanRepository.findLoansByMemberOrderByDate(member);
    }

    /**
     * Récupère les emprunts en retard.
     */
    public List<Loan> getOverdueLoans() {
        return loanRepository.findByStatus(LoanStatus.OVERDUE);
    }

    /**
     * Récupère les emprunts d'un livre.
     */
    public List<Loan> getLoansByBook(Long bookId) {
        Book book = bookService.getBookById(bookId);
        return loanRepository.findByBook(book);
    }

    /**
     * Compte le nombre d'emprunts actifs d'un membre.
     */
    public long countActiveLoansByMember(Long memberId) {
        Member member = memberService.getMemberById(memberId);
        return loanRepository.countByMemberAndStatus(member, LoanStatus.ACTIVE);
    }

    /**
     * Compte le nombre d'emprunts total d'un membre.
     */
    public long countTotalLoansByMember(Long memberId) {
        return loanRepository.countTotalLoansByMember(memberId);
    }

    /**
     * Vérifie si un membre peut emprunter (moins de 3 emprunts actifs).
     */
    public boolean canMemberBorrow(Long memberId) {
        long activeLoans = loanRepository.countByMemberAndStatus(
                memberService.getMemberById(memberId), 
                LoanStatus.ACTIVE
        );
        return activeLoans < MAX_ACTIVE_LOANS_PER_MEMBER;
    }

    /**
     * Récupère le nombre d'emprunts actifs restants qu'un membre peut faire.
     */
    public int getRemainingBorrowQuota(Long memberId) {
        long activeLoans = loanRepository.countByMemberAndStatus(
                memberService.getMemberById(memberId), 
                LoanStatus.ACTIVE
        );
        return Math.max(0, (int) (MAX_ACTIVE_LOANS_PER_MEMBER - activeLoans));
    }
}
