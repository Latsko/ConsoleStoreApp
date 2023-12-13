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

    /**
     * Constructor uses dependency injected FileService to create products.
     * List of categories is either initialized from a file or if no such file
     * is provided file with data being created after which products are initialized.
     * ID is updated to maintain order of IDs assigned to every new created object
     *
     * @throws FileNotFoundException if file with Products does not exist
     **/
    public ProductService(final FileService fileService) throws FileNotFoundException {
        products = fileService.readProductsFromFile();
        updateID();
    }

    /**
     * This method simply returns list of products
     *
     * @return products
     **/
    public List<Product> getProducts() {
        return products;
    }

    /**
     * This method find product by id
     *
     * @param id product id
     * @return found product or null
     **/
    public Product getProductByID(final int id) {
        for (Product product : products) {
            if (product.id() == id) {
                return product;
            }
        }
        return null;
    }

    /**
     * This method removes product by name and updates IDs
     *
     * @param searched product to remove
     * @throws IllegalArgumentException Argument in this function is null!
     **/
    public void removeProduct(final Product searched) {
        if (searched == null) {
            throw new IllegalArgumentException("Argument in this function is null!");
        }
        products.removeIf(Predicate.isEqual(searched));
        updateID();
    }

    /**
     * This method adds new product to the list and gives incremented last ID to
     * that product
     *
     * @param inputProductName  product name
     * @param inputPrice        product price
     * @param inputCategoryName product category
     * @param inputQuantity     quantity of added product
     **/
    public void addProduct(final String inputProductName, final double inputPrice, final String inputCategoryName, final int inputQuantity) {
        products.add(new Product(lastID++, inputPrice, inputProductName, new Category(inputCategoryName, lastID++), inputQuantity));
    }

    /**
     * This method helps to keep track for what last ID for categories is
     **/
    private void updateID() {
        if (products.isEmpty()) {
            lastID = 0;
        } else {
            lastID = products.stream()
                    .mapToInt(Product::id)
                    .max()
                    .orElseThrow(NoSuchElementException::new);
        }
        lastID++;
    }
}
