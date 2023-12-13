package latsko.store.services;

import latsko.store.entities.Category;
import latsko.store.services.file_handling.FileService;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

public class CategoryService {
    private final List<Category> categories;
    private int lastID;

    /**
     * Constructor uses dependency injected FileService to create categories.
     * List of categories is either initialized from a file or if no such file
     * is provided file with data being created after which categories are initialized.
     * ID is updated to maintain order of IDs assigned to every new created object
     *
     * @throws FileNotFoundException if file with Categories does not exist
     **/
    public CategoryService(final FileService fileService) throws FileNotFoundException {
        categories = fileService.readCategoriesFromFile();
        updateID();
    }

    /**
     * This method simply returns list of categories
     *
     * @return categories
     **/
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * This method adds new category to list and gives incremented last ID to
     * that category.
     *
     * @param newCategoryName name for new category
     **/
    public void addCategory(final String newCategoryName) {
        categories.add(new Category(newCategoryName, lastID++));
    }

    /**
     * This method removes category by name
     *
     * @param categoryToRemove name of the category
     * @throws NullPointerException     There is no elements to remove!
     * @throws IllegalArgumentException No category under that name was found!
     **/
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

    /**
     * This method finds category by name or returns null
     *
     * @param name category name
     * @return returns Category under given name
     **/
    private Category findByName(final String name) {
        return categories.stream()
                .filter(category -> name.equals(category.name()))
                .findAny()
                .orElse(null);
    }

    /**
     * This method helps to keep track for what last ID for categories is
     **/
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
