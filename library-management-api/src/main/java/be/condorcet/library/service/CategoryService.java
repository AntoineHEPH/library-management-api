package be.condorcet.library.service;

import be.condorcet.library.model.Category;
import be.condorcet.library.repository.CategoryRepository;
import be.condorcet.library.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service pour gérer les catégories de livres.
 * Contient la logique métier liée aux catégories.
 */
@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Récupère toutes les catégories.
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Récupère une catégorie par son ID.
     */
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie avec l'ID " + id + " non trouvée"));
    }

    /**
     * Crée une nouvelle catégorie.
     */
    public Category createCategory(Category category) {
        // Vérifier que la catégorie n'existe pas déjà
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Une catégorie avec le nom '" + category.getName() + "' existe déjà");
        }
        return categoryRepository.save(category);
    }

    /**
     * Met à jour une catégorie existante.
     */
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = getCategoryById(id);
        
        if (categoryDetails.getName() != null) {
            // Vérifier que le nouveau nom n'existe pas déjà (sauf si c'est le même)
            if (!categoryDetails.getName().equals(category.getName()) && 
                categoryRepository.existsByName(categoryDetails.getName())) {
                throw new RuntimeException("Une catégorie avec le nom '" + categoryDetails.getName() + "' existe déjà");
            }
            category.setName(categoryDetails.getName());
        }
        if (categoryDetails.getDescription() != null) {
            category.setDescription(categoryDetails.getDescription());
        }
        
        return categoryRepository.save(category);
    }

    /**
     * Supprime une catégorie par son ID.
     */
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    /**
     * Recherche une catégorie par son nom.
     */
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie '" + name + "' non trouvée"));
    }

    /**
     * Vérifie si une catégorie existe avec ce nom.
     */
    public boolean categoryExists(String name) {
        return categoryRepository.existsByName(name);
    }
}
