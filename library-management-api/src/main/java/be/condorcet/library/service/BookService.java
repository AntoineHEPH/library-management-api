package be.condorcet.library.service;

import be.condorcet.library.model.Book;
import be.condorcet.library.model.Author;
import be.condorcet.library.model.Category;
import be.condorcet.library.repository.BookRepository;
import be.condorcet.library.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service pour gérer les livres du catalogue.
 * Contient la logique métier liée aux livres et au catalogue.
 */
@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public BookService(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    /**
     * Récupère tous les livres.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Récupère un livre par son ID.
     */
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre avec l'ID " + id + " non trouvé"));
    }

    /**
     * Crée un nouveau livre.
     */
    public Book createBook(Book book) {
        // Vérifier que l'ISBN n'existe pas déjà
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new RuntimeException("Un livre avec l'ISBN '" + book.getIsbn() + "' existe déjà");
        }
        
        // Vérifier que l'auteur existe
        if (book.getAuthor() != null && book.getAuthor().getId() != null) {
            Author author = authorService.getAuthorById(book.getAuthor().getId());
            book.setAuthor(author);
        }
        
        // Par défaut, tous les exemplaires sont disponibles
        if (book.getAvailableCopies() == null) {
            book.setAvailableCopies(book.getTotalCopies());
        }
        
        return bookRepository.save(book);
    }

    /**
     * Met à jour un livre existant.
     */
    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBookById(id);
        
        if (bookDetails.getIsbn() != null) {
            // Vérifier que le nouvel ISBN n'existe pas déjà (sauf si c'est le même)
            if (!bookDetails.getIsbn().equals(book.getIsbn()) && 
                bookRepository.existsByIsbn(bookDetails.getIsbn())) {
                throw new RuntimeException("Un livre avec l'ISBN '" + bookDetails.getIsbn() + "' existe déjà");
            }
            book.setIsbn(bookDetails.getIsbn());
        }
        if (bookDetails.getTitle() != null) {
            book.setTitle(bookDetails.getTitle());
        }
        if (bookDetails.getPublicationYear() != null) {
            book.setPublicationYear(bookDetails.getPublicationYear());
        }
        if (bookDetails.getTotalCopies() != null) {
            book.setTotalCopies(bookDetails.getTotalCopies());
        }
        if (bookDetails.getAvailableCopies() != null) {
            book.setAvailableCopies(bookDetails.getAvailableCopies());
        }
        
        return bookRepository.save(book);
    }

    /**
     * Supprime un livre par son ID.
     */
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    /**
     * Recherche un livre par son ISBN.
     */
    public Book getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Livre avec l'ISBN '" + isbn + "' non trouvé"));
    }

    /**
     * Recherche les livres par titre (recherche partielle).
     */
    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Récupère les livres d'un auteur.
     */
    public List<Book> getBooksByAuthor(Long authorId) {
        Author author = authorService.getAuthorById(authorId);
        return bookRepository.findByAuthor(author);
    }

    /**
     * Récupère les livres d'une catégorie.
     */
    public List<Book> getBooksByCategory(Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        return bookRepository.findByCategories(category);
    }

    /**
     * Récupère les livres disponibles (au moins 1 exemplaire).
     */
    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableCopiesGreaterThan(0);
    }

    /**
     * Récupère les livres indisponibles.
     */
    public List<Book> getUnavailableBooks() {
        return bookRepository.findByAvailableCopies(0);
    }

    /**
     * Compte le nombre de livres disponibles.
     */
    public long countAvailableBooks() {
        return bookRepository.countByAvailableCopiesGreaterThan(0);
    }

    /**
     * Ajoute une catégorie à un livre.
     */
    public Book addCategoryToBook(Long bookId, Long categoryId) {
        Book book = getBookById(bookId);
        Category category = categoryService.getCategoryById(categoryId);
        
        book.addCategory(category);
        return bookRepository.save(book);
    }

    /**
     * Retire une catégorie d'un livre.
     */
    public Book removeCategoryFromBook(Long bookId, Long categoryId) {
        Book book = getBookById(bookId);
        Category category = categoryService.getCategoryById(categoryId);
        
        book.removeCategory(category);
        return bookRepository.save(book);
    }

    /**
     * Vérifie si un livre existe avec cet ISBN.
     */
    public boolean bookExists(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    /**
     * Récupère les livres disponibles d'une catégorie donnée.
     */
    public List<Book> getAvailableBooksByCategory(String categoryName) {
        return bookRepository.findAvailableBooksByCategory(categoryName);
    }
}
