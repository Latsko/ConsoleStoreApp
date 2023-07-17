package org.example.Services;

import org.example.Entities.Category;
import org.example.Entities.Product;
import org.example.Entities.fileHandling.CreateData;
import org.example.Entities.fileHandling.ReadData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private final List<Category> categories;
    private final ReadData readData = new ReadData();
    public CategoryService() throws FileNotFoundException {
        File categoryFile = CreateData.getCategoriesPath().toFile();
        if(categoryFile.exists()) {
            categories = new ArrayList<>(readData.readCategoriesFromFile());
        } else {
            categories = new ArrayList<>();
        }
    }

    public void showCategory(String name) throws FileNotFoundException {
        List<Product> productList = new ArrayList<>(readData.readProductsFromFile());
        Category foundCategory = categories.stream()
                .filter(category -> category.getName().equals(name))
                .findAny()
                .orElse(null);

        if(foundCategory != null) {
            System.out.println("[" + foundCategory.getID() + "] " + foundCategory.getName());
            productList.stream()
                    .filter(product -> product.getCategory().getName().equals(name))
                    .forEach(System.out::println);
        } else {
            System.out.println("Niepoprawna kategoria");
        }
    }

    public void showAllCategories() {
        categories.stream()
                .map(Category::getName)
                .forEach(name -> System.out.println("\t" + name));
    }
}
