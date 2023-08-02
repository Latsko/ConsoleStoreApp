package latsko.store.services;

import latsko.store.entities.Category;
import latsko.store.entities.Product;
import latsko.store.services.file_handling.FileService;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class ProductService {
    private final List<Product> products;
    private int lastID;

    public List<Product> getProducts() {
        return products;
    }

    public ProductService(final FileService fileService) throws FileNotFoundException {
        products = fileService.readProductsFromFile();
        updateID();
    }

    public Product getProductByID(final int id) {
        for (Product product : products) {
            if (product.id() == id) {
                return product;
            }
        }
        return null;
    }

    public void removeProduct(final Product searched) {
        if (searched == null) {
            throw new IllegalArgumentException("Argument in this function is null!");
        }
        products.removeIf(Predicate.isEqual(searched));
        updateID();
    }

    public void addProduct(final String inputProductName, final double inputPrice, final String inputCategoryName, final int inputQuantity) {
        products.add(new Product(lastID++, inputPrice, inputProductName, new Category(inputCategoryName, lastID++), inputQuantity));
    }

    private void updateID() {
        lastID = products.stream()
                .mapToInt(Product::id)
                .max()
                .orElseThrow(NoSuchElementException::new);
        lastID++;
    }
}
