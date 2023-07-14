package org.example.Entities.fileHandling;

import org.example.Entities.Category;
import org.example.Entities.Product;
import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ReadData {
    public List<Product> readProductsFromFile() throws FileNotFoundException {
        File file = CreateData.getPath().toFile();

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
}
