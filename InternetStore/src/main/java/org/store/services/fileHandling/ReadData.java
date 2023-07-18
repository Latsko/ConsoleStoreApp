package org.store.services.fileHandling;

import org.store.entities.Category;
import org.store.entities.Product;
import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ReadData {
    public List<Product> readProductsFromFile() throws FileNotFoundException {
        File file = CreateData.getProductsPath().toFile();

        List<Product> productList = new ArrayList<>();
        JSONArray read = new JSONArray(new JSONTokener(new FileInputStream(file)));
        for (int i = 0; i < read.length(); i++) {
            int id = read.getJSONObject(i).getInt("id");
            double price = read.getJSONObject(i).getDouble("price");
            String name = read.getJSONObject(i).getString("name");
            Category category = new Category(read.getJSONObject(i).getString("category"));
            int quantity = read.getJSONObject(i).getInt("quantity");

            Product currentProduct = new Product(price, name, category, quantity);
            currentProduct.setID(id);
            productList.add(currentProduct);
        }

        return productList;
    }

    public List<Category> readCategoriesFromFile() throws FileNotFoundException {
        File file = CreateData.getCategoriesPath().toFile();

        List<Category> categoryList = new ArrayList<>();
        JSONArray read = new JSONArray(new JSONTokener(new FileInputStream(file)));
        for (int i = 0; i < read.length(); i++) {
            String name = read.getJSONObject(i).getString("name");

            Category currentCategory = new Category(name);
            categoryList.add(currentCategory);
        }

        return categoryList;
    }
}
