package be.condorcet.library.controller;

import be.condorcet.library.model.Book;
import be.condorcet.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer le catalogue de livres.
 * Endpoints pour CRUD et recherches avancées sur les livres.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * GET /api/books - Récupère tous les livres
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * GET /api/books/{id} - Récupère un livre par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    /**
     * POST /api/books - Crée un nouveau livre
     */
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    /**
     * PUT /api/books/{id} - Met à jour un livre existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book bookDetails) {
        Book updatedBook = bookService.updateBook(id, bookDetails);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * DELETE /api/books/{id} - Supprime un livre
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/books/search/isbn?isbn=978-3-16-148410-0 - Recherche par ISBN
     */
    @GetMapping("/search/isbn")
    public ResponseEntity<Book> searchByIsbn(@RequestParam String isbn) {
        Book book = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    /**
     * GET /api/books/search/title?title=Harry - Recherche par titre (partiel)
     */
    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> searchByTitle(@RequestParam String title) {
        List<Book> books = bookService.searchByTitle(title);
        return ResponseEntity.ok(books);
    }

    /**
     * GET /api/books/author/{authorId} - Récupère les livres d'un auteur
     */
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable Long authorId) {
        List<Book> books = bookService.getBooksByAuthor(authorId);
        return ResponseEntity.ok(books);
    }

    /**
     * GET /api/books/category/{categoryId} - Récupère les livres d'une catégorie
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable Long categoryId) {
        List<Book> books = bookService.getBooksByCategory(categoryId);
        return ResponseEntity.ok(books);
    }

    /**
     * GET /api/books/available - Récupère les livres disponibles
     */
    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        List<Book> books = bookService.getAvailableBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * GET /api/books/unavailable - Récupère les livres indisponibles
     */
    @GetMapping("/unavailable")
    public ResponseEntity<List<Book>> getUnavailableBooks() {
        List<Book> books = bookService.getUnavailableBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * GET /api/books/stats/available-count - Compte les livres disponibles
     */
    @GetMapping("/stats/available-count")
    public ResponseEntity<Long> countAvailableBooks() {
        long count = bookService.countAvailableBooks();
        return ResponseEntity.ok(count);
    }

    /**
     * POST /api/books/{bookId}/category/{categoryId} - Ajoute une catégorie à un livre
     */
    @PostMapping("/{bookId}/category/{categoryId}")
    public ResponseEntity<Book> addCategoryToBook(@PathVariable Long bookId, @PathVariable Long categoryId) {
        Book book = bookService.addCategoryToBook(bookId, categoryId);
        return ResponseEntity.ok(book);
    }

    /**
     * DELETE /api/books/{bookId}/category/{categoryId} - Retire une catégorie d'un livre
     */
    @DeleteMapping("/{bookId}/category/{categoryId}")
    public ResponseEntity<Book> removeCategoryFromBook(@PathVariable Long bookId, @PathVariable Long categoryId) {
        Book book = bookService.removeCategoryFromBook(bookId, categoryId);
        return ResponseEntity.ok(book);
    }

    /**
     * GET /api/books/available/category?categoryName=Science-Fiction - Récupère les livres disponibles d'une catégorie
     */
    @GetMapping("/available/category")
    public ResponseEntity<List<Book>> getAvailableBooksByCategory(@RequestParam String categoryName) {
        List<Book> books = bookService.getAvailableBooksByCategory(categoryName);
        return ResponseEntity.ok(books);
    }
}
