package latsko.store.services;

import latsko.store.entities.Category;
import latsko.store.entities.Product;
import latsko.store.services.file_handling.FileService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Test
    void getEmptyProductsList() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        when(fileService.readCategoriesFromFile()).thenReturn(null);
        ProductService productService = new ProductService(fileService);

        //when
        List<Product> products = productService.getProducts();

        //then
        assertThat(products).isEmpty();
    }

    @Test
    void getNonEmptyProductsList() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        final Category testCategory = new Category("test", 1);
        final List<Product> products = new ArrayList<>();
        products.add(new Product(1, 10, "test product 1", testCategory, 10));
        products.add(new Product(2, 20, "test product 2", testCategory, 20));
        products.add(new Product(3, 30, "test product 3", testCategory, 30));
        when(fileService.readProductsFromFile()).thenReturn(products);
        ProductService productService = new ProductService(fileService);

        //when
        List<Product> expected = productService.getProducts();

        //then
        assertThat(expected).isEqualTo(products);
    }

    @Test
    void getProductByID() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        final Category testCategory = new Category("test", 1);
        final List<Product> products = new ArrayList<>();
        products.add(new Product(0, 100, "test product 1", testCategory, 10));
        products.add(new Product(1, 200, "test product 2", testCategory, 20));
        products.add(new Product(2, 300, "test product 3", testCategory, 30));
        when(fileService.readProductsFromFile()).thenReturn(products);
        ProductService productService = new ProductService(fileService);

        //when
        Product productByID0 = productService.getProductByID(0);
        Product productByID1 = productService.getProductByID(1);
        Product productByID2 = productService.getProductByID(2);

        //then
        assertThat(productByID0.id()).isZero();
        assertThat(productByID1.id()).isEqualTo(1);
        assertThat(productByID2.id()).isEqualTo(2);

    }

    @Test
    void removeProduct() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        final Category testCategory = new Category("test", 1);
        final List<Product> products = new ArrayList<>();
        final Product product1 = new Product(1, 10, "test product 1", testCategory, 1);
        final Product product2 = new Product(2, 20, "test product 2", testCategory, 2);
        final Product product3 = new Product(3, 30, "test product 3", testCategory, 3);
        products.add(product1);
        products.add(product2);
        products.add(product3);
        when(fileService.readProductsFromFile()).thenReturn(products);
        ProductService productService = new ProductService(fileService);

        //when
        productService.getProducts().remove(1);

        //then
        assertThat(products).containsOnly(product1, product3);
    }

    @Test
    void addProduct() throws FileNotFoundException {
        //given
        FileService fileService = mock(FileService.class);
        when(fileService.readProductsFromFile()).thenReturn(new ArrayList<>());
        ProductService productService = new ProductService(fileService);

        //when
        productService.addProduct(
                "Product Name",
                100,
                "Category name",
                10);

        //then
        Assertions.assertThat(productService.getProducts()).hasSize(1);

    }
}