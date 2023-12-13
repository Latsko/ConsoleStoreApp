package latsko.store.services.file_handling;

import latsko.store.entities.Category;
import latsko.store.entities.Order;
import latsko.store.entities.OrderStatus;
import latsko.store.entities.Product;
import latsko.store.services.ProductService;
import latsko.store.services.helper.CreateData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class objective is to assure that data is read and written to
 * .json files in data folder properly. It also ensures that in case
 * there is no files categories.json and/or products.json, they will
 * be created.
 **/

public class FileService {
    /**
     * Constructor creates data for application if files containing it does not exist
     **/
    public FileService() throws IOException {
        File productFile = CreateData.getProductsPath().toFile();
        File categoryFile = CreateData.getCategoriesPath().toFile();

        if (productFile.createNewFile()) {
            createProductsInFile();
        }
        if (categoryFile.createNewFile()) {
            createCategoriesInFile();
        }
    }

    /**
     * This method reads Orders from file. JSONTokener is used to break a
     * string from FileInputStream into tokens in order to assign it to
     * JSONArray. Then object fields are assigned one by one to local
     * variables. In order to extract data from JSONArray basket variable,
     * a static JSONArrayToMap mapper is used.
     *
     * @return List<Order> This returns list of orders read from file
     * @throws FileNotFoundException In case if file does not exist
     **/
    public List<Order> readOrdersFromFile() throws FileNotFoundException {
        File file = CreateData.getOrdersPath().toFile();
        List<Order> orderList = new ArrayList<>();
        JSONArray read = new JSONArray(new JSONTokener(new FileInputStream(file)));
        for (int i = 0; i < read.length(); i++) {
            int id = read.getJSONObject(i).getInt("id");
            String orderNum = read.getJSONObject(i).getString("orderNumber");
            String clientName = read.getJSONObject(i).getString("clientName");
            String clientSurname = read.getJSONObject(i).getString("clientSurname");
            String clientAddress = read.getJSONObject(i).getString("address");
            double orderSum = read.getJSONObject(i).getDouble("orderSum");
            String statusString = read.getJSONObject(i).getString("status");
            OrderStatus status = OrderStatus.getOrderFromString(statusString);
            JSONArray basket = read.getJSONObject(i).getJSONArray("basket");

            Map<Product, Integer> basketMap = JSONArrayToMap(basket);
            orderList.add(new Order(id, orderNum, clientName, clientSurname, clientAddress, orderSum, status, basketMap));
        }
        return orderList;
    }

    /**
     * This method reads Products from file. JSONTokener is used to break a
     * string from FileInputStream into tokens in order to assign it to
     * JSONArray. Then object fields are assigned one by one to local
     * variables. Creates data if corresponding file does not exist.
     *
     * @return List<Product> This returns list of orders read from file
     * @throws FileNotFoundException In case if file does not exist
     **/
    public List<Product> readProductsFromFile() throws FileNotFoundException {
        File file = CreateData.getProductsPath().toFile();

        List<Product> productList = new ArrayList<>();
        JSONArray read = new JSONArray(new JSONTokener(new FileInputStream(file)));
        for (int i = 0; i < read.length(); i++) {
            int id = read.getJSONObject(i).getInt("id");
            double price = read.getJSONObject(i).getDouble("price");
            String name = read.getJSONObject(i).getString("name");
            Category category = new Category(read.getJSONObject(i).getString("category"), i);
            int quantity = read.getJSONObject(i).getInt("quantity");

            Product currentProduct = new Product(id, price, name, category, quantity);
            productList.add(currentProduct);
        }

        return productList;
    }

    /**
     * This method reads Categories from file. JSONTokener is used to break a
     * string from FileInputStream into tokens in order to assign it to
     * JSONArray. Then object fields are assigned one by one to local
     * variables. Creates data if corresponding file does not exist.
     *
     * @return List<Category> This returns list of orders read from file
     * @throws FileNotFoundException In case if file does not exist
     **/
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

    /**
     * This method writes Categories to file. After creating empty list of JSONObjects,
     * if there are changes to be made in categories, list is filled with updated data.
     * In case of success, proceeds to write it to a file with indent factor of 4.
     *
     * @param updatedCategories needed when data from any file is being modified
     * @throws FileNotFoundException if destination file does not exist
     **/
    public void writeCategories(final List<Category> updatedCategories) throws FileNotFoundException {
        List<JSONObject> jsonObjects = new ArrayList<>();

        if (updatedCategories != null) {
            for (Category category : updatedCategories) {
                jsonObjects.add(new JSONObject()
                        .put("id", category.id())
                        .put("name", category.name()));
            }
        }

        JSONArray jsonArray = new JSONArray(jsonObjects);
        try (PrintWriter printer = new PrintWriter(CreateData.getCategoriesPath().toFile())) {
            printer.print(jsonArray.toString(4));
        }
    }

    /**
     * This method writes Categories to file. After creating empty list of JSONObjects,
     * if there are changes to be made in categories, list is filled with updated data.
     * In case of success, proceeds to write it to a file with indent factor of 4.
     *
     * @param products needed when data from any file is being modified
     * @throws FileNotFoundException if destination file does not exist
     **/
    public void writeProducts(final List<Product> products) throws FileNotFoundException {
        List<JSONObject> jsonObjects = new ArrayList<>();
        if (products != null) {
            for (Product product : products) {
                jsonObjects.add(new JSONObject()
                        .put("id", product.id())
                        .put("price", product.price())
                        .put("name", product.name())
                        .put("category", product.category().name())
                        .put("quantity", product.quantity()));
            }
        }

        JSONArray jsonArray = new JSONArray(jsonObjects);

        try (PrintWriter printer = new PrintWriter(CreateData.getProductsPath().toFile())) {
            printer.print(jsonArray.toString(4));
        }
    }

    /**
     * This method writes Orders to file. After creating empty list of JSONObjects,
     * if there are changes to be made in categories, list is filled with updated data.
     * In case of success, proceeds to write it to a file with indent factor of 4.
     *
     * @param newOrders needed when data from any file is being modified
     * @throws FileNotFoundException if destination file does not exist
     **/
    public void writeOrders(final List<Order> newOrders) throws FileNotFoundException {
        JSONArray jsonObjects = new JSONArray();
        if (newOrders != null) {
            for (Order order : newOrders) {
                jsonObjects.put(new JSONObject()
                        .put("id", order.getId())
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

    /**
     * This method maps Map which holds Product class as a key and Integer as a value
     * to a JSONArray on requirement to put it in a JSONArray.
     *
     * @param map represents basket, that contains product(key) and quantity(value)
     * @return JSONArray ready to be nested into another JSONArray
     **/
    private JSONArray mapToJSONArray(final Map<Product, Integer> map) {
        JSONArray result = new JSONArray();
        for (Map.Entry<Product, Integer> entry : map.entrySet()) {
            result.put(new JSONObject()
                    .put("product", entry.getKey().id())
                    .put("quantity", entry.getValue()));
        }

        return result;
    }

    /**
     * This method maps JSONArray to a Map which holds Product class as a key and Integer
     * as a value on requirement assign it to a variable during extracting data from file.
     *
     * @param jsonArray represents basket, that contains product(key) and quantity(value)
     * @return Map<Product, Integer> containing product and its quantity as a value
     * @throws FileNotFoundException if file could not be read from ProductService
     **/
    private Map<Product, Integer> JSONArrayToMap(final JSONArray jsonArray) throws FileNotFoundException {
        ProductService productService = new ProductService(this);
        Map<Product, Integer> result = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            int productID = jsonArray.getJSONObject(i).getInt("product");
            int quantity = jsonArray.getJSONObject(i).getInt("quantity");
            result.put(productService.getProductByID(productID), quantity);
        }

        return result;
    }

    /**
     * This method is responsible for the file creation with products in it.
     *
     * @throws FileNotFoundException if PrintWriter fails to write to file
     **/
    public void createProductsInFile() throws FileNotFoundException {
        List<JSONObject> jsonObjects = new ArrayList<>();

        Product[][] allProducts = CreateData.getProducts();
        for (Product[] category : allProducts) {
            for (int j = 0; j < allProducts[j].length; j++) {
                jsonObjects.add(new JSONObject()
                        .put("id", category[j].id())
                        .put("price", category[j].price())
                        .put("name", category[j].name())
                        .put("category", category[j].category().name())
                        .put("quantity", category[j].quantity())
                );
                JSONArray jsonArray = new JSONArray(jsonObjects);

                try (PrintWriter printer = new PrintWriter(CreateData.getProductsPath().toFile())) {
                    printer.print(jsonArray.toString(4));
                }
            }
        }
    }

    /**
     * This method is responsible for the file creation with categories in it.
     *
     * @throws FileNotFoundException if PrintWriter fails to write to file
     **/
    public void createCategoriesInFile() throws FileNotFoundException {
        List<JSONObject> jsonObjects = new ArrayList<>();
        final Category[] categories = CreateData.getCategories();
        for (Category category : categories) {
            jsonObjects.add(new JSONObject()
                    .put("id", category.id())
                    .put("name", category.name()));
        }

        JSONArray jsonArray = new JSONArray(jsonObjects);
        try (PrintWriter printer = new PrintWriter(CreateData.getCategoriesPath().toFile())) {
            printer.print(jsonArray.toString(4));
        }
    }
}
