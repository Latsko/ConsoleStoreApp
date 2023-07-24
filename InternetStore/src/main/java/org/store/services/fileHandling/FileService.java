package org.store.services.fileHandling;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.store.entities.Category;
import org.store.entities.Order;
import org.store.entities.Product;
import org.store.services.helper.CreateData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileService {
    public FileService() throws FileNotFoundException {
        File productFile = CreateData.getProductsPath().toFile();
        File categoryFile = CreateData.getCategoriesPath().toFile();

        if (!productFile.exists()) {
            createProductsInFile();
        }
        if (!categoryFile.exists()) {
            createCategoriesInFile();
        }
    }

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
//            JSONArray basket = read.getJSONObject(i).getJSONArray("basket");
//            // then make a method, which will take JSONArray, and returns Map<Product, Integer>
//            // to be able to make a basket
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

    public void writeProducts(final List<Product> products) throws FileNotFoundException {
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

    public void writeOrders(final List<Order> newOrders) throws FileNotFoundException {
        JSONArray jsonObjects = new JSONArray();
        if (newOrders != null) {
            for (Order order : newOrders) {
                jsonObjects.put(new JSONObject()
                        .put("orderNumber", order.getOrderNumber())
                        .put("basket", mapToJSONArray(order.getBasket()))
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

    private JSONArray mapToJSONArray(final Map<Product, Integer> map) {
        JSONArray result = new JSONArray();
        for (Map.Entry<Product, Integer> entry : map.entrySet()) {
            result.put(new JSONObject()
                    .put("product", entry.getKey().getID())
                    .put("quantity", entry.getValue()));
        }

        return result;
    }

    public void createProductsInFile() throws FileNotFoundException {
        List<JSONObject> jsonObjects = new ArrayList<>();

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
