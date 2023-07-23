package org.store.services;

import org.store.entities.Category;
import org.store.entities.Product;
import org.store.services.fileHandling.ReadData;
import org.store.services.fileHandling.WriteData;
import org.store.services.helper.CreateData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryService {
    private final List<Category> categories;

    public CategoryService(final ReadData readData) throws FileNotFoundException {
        File categoryFile = CreateData.getCategoriesPath().toFile();
        if (!categoryFile.exists()) {
            new WriteData().createCategoriesInFile();
        }
        categories = readData.readCategoriesFromFile();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public String showCategory(String name) throws FileNotFoundException {
        List<Product> productList = new ReadData().readProductsFromFile();

        return productList.stream()
                .filter(product -> product.getCategory().getName().equals(name))
                .map(product -> "[" + product.getID() + "] " + product.getName() + "\n")
                .collect(Collectors.joining())
                .trim();
    }

    public String showAllCategories() {
        return categories.stream()
                .map(category -> "\t[" + category.getID() + "] " + category.getName() + "\n")
                .collect(Collectors.joining())
                .trim();
    }

    public void addCategory(String newCategoryName) throws FileNotFoundException {
        categories.add(new Category(newCategoryName));
        WriteData update = new WriteData();
        update.writeCategories(categories);
    }

    public void removeCategory(String categoryToRemove) throws FileNotFoundException {
        categories.remove(findByName(categoryToRemove));
        WriteData update = new WriteData();
        update.writeCategories(categories);
    }

    private int findByName(final String name) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
