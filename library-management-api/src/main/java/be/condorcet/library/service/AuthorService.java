package be.condorcet.library.service;

import be.condorcet.library.model.Author;
import be.condorcet.library.repository.AuthorRepository;
import be.condorcet.library.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les auteurs.
 * Contient la logique métier liée aux auteurs.
 */
@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * Récupère tous les auteurs.
     */
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    /**
     * Récupère un auteur par son ID.
     */
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auteur avec l'ID " + id + " non trouvé"));
    }

    /**
     * Crée un nouvel auteur.
     */
    public Author createAuthor(Author author) {
        // Vérifier que l'auteur n'existe pas déjà
        if (authorRepository.existsByFirstNameAndLastName(author.getFirstName(), author.getLastName())) {
            throw new RuntimeException("Un auteur avec le prénom " + author.getFirstName() + 
                    " et le nom " + author.getLastName() + " existe déjà");
        }
        return authorRepository.save(author);
    }

    /**
     * Met à jour un auteur existant.
     */
    public Author updateAuthor(Long id, Author authorDetails) {
        Author author = getAuthorById(id);
        
        if (authorDetails.getFirstName() != null) {
            author.setFirstName(authorDetails.getFirstName());
        }
        if (authorDetails.getLastName() != null) {
            author.setLastName(authorDetails.getLastName());
        }
        if (authorDetails.getNationality() != null) {
            author.setNationality(authorDetails.getNationality());
        }
        if (authorDetails.getBirthYear() != null) {
            author.setBirthYear(authorDetails.getBirthYear());
        }
        
        return authorRepository.save(author);
    }

    /**
     * Supprime un auteur par son ID.
     */
    public void deleteAuthor(Long id) {
        Author author = getAuthorById(id);
        authorRepository.delete(author);
    }

    /**
     * Recherche les auteurs par nom de famille.
     */
    public List<Author> searchByLastName(String lastName) {
        return authorRepository.findByLastName(lastName);
    }

    /**
     * Recherche les auteurs par nationalité.
     */
    public List<Author> searchByNationality(String nationality) {
        return authorRepository.findByNationality(nationality);
    }

    /**
     * Vérifie si un auteur existe avec ce prénom et nom.
     */
    public boolean authorExists(String firstName, String lastName) {
        return authorRepository.existsByFirstNameAndLastName(firstName, lastName);
    }
}
