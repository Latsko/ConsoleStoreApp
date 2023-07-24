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

    public void addCategory(final String newCategoryName) {
        categories.add(new Category(newCategoryName));
    }

    public void removeCategory(final String categoryToRemove) {
        if (categories == null) {
            throw new NullPointerException("There is no elements to remove!");
        }
        final Category category = findByName(categoryToRemove);
        if (category == null) {
            throw new IllegalArgumentException("No category under that name was found!");
        }
        categories.remove(category);
    }

    private Category findByName(final String name) {
        return categories.stream()
                .filter(category -> name.equals(category.getName()))
                .findAny()
                .orElse(null);
    }
}
