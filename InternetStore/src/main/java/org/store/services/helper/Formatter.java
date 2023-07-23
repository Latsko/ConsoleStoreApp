package org.store.services.helper;

import org.store.entities.Category;
import org.store.entities.Product;

import java.util.List;
import java.util.stream.Collectors;

public class Formatter {
    public static String formatCategory(String name, List<Product> productList) {
        return productList.stream()
                .filter(product -> product.getCategory().getName().equals(name))
                .map(product -> "[" + product.getID() + "] " + product.getName() + "\n")
                .collect(Collectors.joining())
                .trim();
    }

    public static String formatAllCategories(List<Category> categories) {
        return categories.stream()
                .map(category -> "\t[" + category.getID() + "] " + category.getName() + "\n")
                .collect(Collectors.joining())
                .trim();
    }
}
