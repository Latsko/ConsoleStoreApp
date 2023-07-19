package org.store.services;

import org.store.entities.Product;
import org.store.services.fileHandling.CreateData;
import org.store.services.fileHandling.ReadData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private final List<Product> products;

    private final ReadData readData = new ReadData();

    public ProductService() throws FileNotFoundException {
        File productFile = CreateData.getProductsPath().toFile();
        if(productFile.exists()) {
            products = new ArrayList<>(readData.readProductsFromFile());
        } else {
            products = new ArrayList<>();
        }

    }

    public void showProduct(String name) throws FileNotFoundException {
        List<Product> productList = new ArrayList<>(readData.readProductsFromFile());
        Product foundProduct = products.stream().filter(product -> product.getName()
                        .equals(name))
                .findAny()
                .orElse(null);

        if(foundProduct != null) {
            System.out.println("{" + foundProduct.getID() + "}" + foundProduct.getName());
            productList.stream()
                    .filter(product -> product.getName().equals(name))
                    .forEach(System.out::println);
        } else {
            System.out.println("Niepoprawny produkt");
        }
    }

    public void showAllProducts() {
        products.stream()
                .map(Product::getName)
                .forEach(name -> System.out.println("\t" + name));
    }
}
