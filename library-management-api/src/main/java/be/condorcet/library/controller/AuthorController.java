package be.condorcet.library.controller;

import be.condorcet.library.model.Author;
import be.condorcet.library.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les auteurs.
 * Endpoints pour CRUD et recherches sur les auteurs.
 */
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    /**
     * GET /api/authors - Récupère tous les auteurs
     */
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    /**
     * GET /api/authors/{id} - Récupère un auteur par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        Author author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    /**
     * POST /api/authors - Crée un nouvel auteur
     */
    @PostMapping
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody Author author) {
        Author createdAuthor = authorService.createAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    /**
     * PUT /api/authors/{id} - Met à jour un auteur existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @Valid @RequestBody Author authorDetails) {
        Author updatedAuthor = authorService.updateAuthor(id, authorDetails);
        return ResponseEntity.ok(updatedAuthor);
    }

    /**
     * DELETE /api/authors/{id} - Supprime un auteur
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/authors/search/lastname?lastName=Dupont - Recherche par nom de famille
     */
    @GetMapping("/search/lastname")
    public ResponseEntity<List<Author>> searchByLastName(@RequestParam String lastName) {
        List<Author> authors = authorService.searchByLastName(lastName);
        return ResponseEntity.ok(authors);
    }

    /**
     * GET /api/authors/search/nationality?nationality=France - Recherche par nationalité
     */
    @GetMapping("/search/nationality")
    public ResponseEntity<List<Author>> searchByNationality(@RequestParam String nationality) {
        List<Author> authors = authorService.searchByNationality(nationality);
        return ResponseEntity.ok(authors);
    }
}
