package org.store.services.fileHandling;

import org.json.JSONArray;
import org.json.JSONTokener;
import org.store.entities.Category;
import org.store.entities.Product;
import org.store.services.helper.CreateData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ReadData {
//    public List<Order> readOrdersFromFile() throws FileNotFoundException {
//        File file = CreateData.getOrdersPath().toFile();
//        List<Order> orderList = new ArrayList<>();
//        JSONArray read = new JSONArray(new JSONTokener(new FileInputStream(file)));
//        for (int i = 0; i < orderList.size(); i++) {
//            int id = read.getJSONObject(i).getInt("id");
//            String orderNum = read.getJSONObject(i).getString("orderNumber");
//            String clientName = read.getJSONObject(i).getString("clientName");
//            String clientSurname = read.getJSONObject(i).getString("clientSurname");
//            String clientAddress = read.getJSONObject(i).getString("address");
//            double orderSum = read.getJSONObject(i).getDouble("orderSum");
//            OrderStatus orderStatus = OrderStatus.getOrderFromString(read.getJSONObject(i).getString("status"));
//            // do no know how to read collection which contains collection
//
//        }
//        return null;
//    }

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

            Product currentProduct = new Product(price, name, category, quantity, id);
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
            Category currentCategory = new Category(name, i);
            categoryList.add(currentCategory);
        }

        return categoryList;
    }
}
