package org.store.services;

import org.store.entities.Category;
import org.store.entities.Product;
import org.store.services.fileHandling.FileService;

import java.io.FileNotFoundException;
import java.util.List;

public class ProductService {
    private final List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public ProductService(FileService fileService) throws FileNotFoundException {
        products = fileService.readProductsFromFile();
    }

    public Product getProductByID(int id) {
        for (Product product : products) {
            if (product.getID() == id) {
                return product;
            }
        }
        return null;
    }

    public void removeProduct(Product searched) {
        if (searched == null) {
            throw new IllegalArgumentException("Argument in this function is null!");
        }
        products.remove(searched);
    }

    public void addProduct(String inputProductName, double inputPrice, String inputCategoryName, int inputQuantity) {
        products.add(new Product(inputPrice, inputProductName, new Category(inputCategoryName), inputQuantity));
    }
}
