package org.store.services.fileHandling;

import org.json.JSONArray;
import org.json.JSONObject;
import org.store.entities.Category;
import org.store.entities.Order;
import org.store.entities.Product;
import org.store.services.helper.CreateData;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class WriteData {
    public void writeCategories(final List<Category> updatedCategories) throws FileNotFoundException {
        List<JSONObject> jsonObjects = new ArrayList<>();

        if (updatedCategories != null) {
            for (Category category : updatedCategories) {
                jsonObjects.add(new JSONObject()
                        .put("id", category.getID())
                        .put("name", category.getName()));
            }
        } else {
            throw new IllegalArgumentException("List given as parameter is null!");
        }
        JSONArray jsonArray = new JSONArray(jsonObjects);
        try (PrintWriter printer = new PrintWriter(CreateData.getCategoriesPath().toFile())) {
            printer.print(jsonArray.toString(4));
        }
    }

    public void writeProducts(List<Product> products) throws FileNotFoundException {
        List<JSONObject> jsonObjects = new ArrayList<>();
        if (products != null) {
            for (Product product : products) {
                jsonObjects.add(new JSONObject()
                        .put("id", product.getID())
                        .put("price", product.getPrice())
                        .put("name", product.getName())
                        .put("category", product.getCategory().getName())
                        .put("quantity", product.getQuantity()));
            }
        }

        JSONArray jsonArray = new JSONArray(jsonObjects);

        try (PrintWriter printer = new PrintWriter(CreateData.getProductsPath().toFile())) {
            printer.print(jsonArray.toString(4));
        }
    }

    public void writeOrders(List<Order> newOrders) throws FileNotFoundException {
        List<JSONObject> jsonObjects = new ArrayList<>();
        if (newOrders != null) {
            for (Order order : newOrders) {
                jsonObjects.add(new JSONObject()
                        .put("orderNumber", order.getOrderNumber())
                        .put("basket", order.getBasket())
                        .put("clientName", order.getClientName())
                        .put("clientSurname", order.getClientSurName())
                        .put("address", order.getClientAddress())
                        .put("orderSum", order.getNumberSum())
                        .put("status", order.getStatus()));
            }
        }

        JSONArray jsonArray = new JSONArray(jsonObjects);

        try (PrintWriter printer = new PrintWriter(CreateData.getOrdersPath().toFile())) {
            printer.print(jsonArray.toString(4));
        }
    }

    public void createProductsInFile() throws FileNotFoundException {
        List<JSONObject> jsonObjects = new ArrayList<>();
        final Category[] categories = CreateData.getCategories();

        Product[][] allProducts = CreateData.getProducts();
        for (Product[] category : allProducts) {
            for (int j = 0; j < allProducts[j].length; j++) {
                jsonObjects.add(new JSONObject()
                        .put("id", category[j].getID())
                        .put("price", category[j].getPrice())
                        .put("name", category[j].getName())
                        .put("category", category[j].getCategory().getName())
                        .put("quantity", category[j].getQuantity())
                );
                JSONArray jsonArray = new JSONArray(jsonObjects);

                try (PrintWriter printer = new PrintWriter(CreateData.getProductsPath().toFile())) {
                    printer.print(jsonArray.toString(4));
                }
            }
        }
    }

    public void createCategoriesInFile() throws FileNotFoundException {
        List<JSONObject> jsonObjects = new ArrayList<>();
        final Category[] categories = CreateData.getCategories();
        for (Category category : categories) {
            jsonObjects.add(new JSONObject()
                    .put("id", category.getID())
                    .put("name", category.getName()));
        }

        JSONArray jsonArray = new JSONArray(jsonObjects);
        try (PrintWriter printer = new PrintWriter(CreateData.getCategoriesPath().toFile())) {
            printer.print(jsonArray.toString(4));
        }
    }
}
