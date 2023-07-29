package org.store.services;

import org.store.entities.Category;
import org.store.entities.Product;
import org.store.services.fileHandling.FileService;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.function.Predicate;

public class ProductService {
    private final List<Product> products;
    private int lastID;

    public List<Product> getProducts() {
        return products;
    }

    public ProductService(final FileService fileService) throws FileNotFoundException {
        products = fileService.readProductsFromFile();
        lastID = products.size();
    }

    public Product getProductByID(final int id) {
        for (Product product : products) {
            if (product.getID() == id) {
                return product;
            }
        }
        return null;
    }

    public void removeProduct(final Product searched) {
        if (searched == null) {
            throw new IllegalArgumentException("Argument in this function is null!");
        }// products.removeif()
        products.removeIf(Predicate.isEqual(searched));
    }

    public void addProduct(final String inputProductName, final double inputPrice, final String inputCategoryName, final int inputQuantity) {
        products.add(new Product(lastID++, inputPrice, inputProductName, new Category(inputCategoryName, lastID++), inputQuantity));
    }
}
