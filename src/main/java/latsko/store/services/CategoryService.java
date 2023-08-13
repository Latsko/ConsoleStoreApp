package latsko.store.services;

import latsko.store.entities.Category;
import latsko.store.services.file_handling.FileService;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

public class CategoryService {
    private final List<Category> categories;
    private int lastID;

    public CategoryService(final FileService fileService) throws FileNotFoundException {
        categories = fileService.readCategoriesFromFile();
        updateID();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void addCategory(final String newCategoryName) {
        categories.add(new Category(newCategoryName, lastID++));
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
        updateID();
    }


    private Category findByName(final String name) {
        return categories.stream()
                .filter(category -> name.equals(category.name()))
                .findAny()
                .orElse(null);
    }

    private void updateID() {
        if (categories.isEmpty()) {
            lastID = 0;
        } else {
            lastID = categories.stream()
                    .mapToInt(Category::id)
                    .max()
                    .orElseThrow(NoSuchElementException::new);
        }
        lastID++;
    }
}
