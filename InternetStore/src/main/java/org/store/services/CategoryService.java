package org.store.services;

import org.store.entities.Category;
import org.store.services.fileHandling.FileService;

import java.io.FileNotFoundException;
import java.util.List;

public class CategoryService {
    private final List<Category> categories;

    public CategoryService(final FileService fileService) throws FileNotFoundException {
        categories = fileService.readCategoriesFromFile();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void addCategory(String newCategoryName) {
        categories.add(new Category(newCategoryName));
    }

    public void removeCategory(String categoryToRemove) {
        Category category = findByName(categoryToRemove);
        if (category != null) {
            categories.remove(category);
        } else {
            throw new IllegalArgumentException("No category under that name was found!");
        }
    }

    private Category findByName(final String name) {
        return categories.stream()
                .filter(category -> name.equals(category.getName()))
                .findAny()
                .orElse(null);
    }
}
