package latsko.store.services;

import latsko.store.entities.Category;
import latsko.store.entities.Product;
import latsko.store.services.file_handling.FileService;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void getProductByIDEmptyList() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        final List<Product> products = new ArrayList<>();
        when(fileService.readProductsFromFile()).thenReturn(products);
        ProductService productService = new ProductService(fileService);

        //when
        final Product product = productService.getProductByID(1);

        //then
        assertThat(product).isNull();
    }

    @Test
    void getProductByIDNonEmptyListIDExists() throws FileNotFoundException {
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
    void getProductByIDNonEmptyListIDDoesNotExist() throws FileNotFoundException {
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
        Product productByID0 = productService.getProductByID(5);
        Product productByID1 = productService.getProductByID(6);
        Product productByID2 = productService.getProductByID(7);

        //then
        assertThat(productByID0).isNull();
        assertThat(productByID1).isNull();
        assertThat(productByID2).isNull();
    }

    @Test
    void removeProductNonEmptyList() throws FileNotFoundException {
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
    void removeProductEmptyList() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        final Category testCategory = new Category("test", 1);
        final List<Product> products = new ArrayList<>();

        when(fileService.readProductsFromFile()).thenReturn(products);
        ProductService productService = new ProductService(fileService);

        //when
        final ThrowableAssert.ThrowingCallable callable = () -> productService.getProducts().remove(1);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void addProductToEmptyList() throws FileNotFoundException {
        //given
        FileService fileService = mock(FileService.class);
        when(fileService.readProductsFromFile()).thenReturn(new ArrayList<>());
        ProductService productService = new ProductService(fileService);
        Category testCategory = new Category("Category name", 2);

        //when
        productService.addProduct(
                "Product Name",
                100,
                "Category name",
                10);

        //then
        Assertions.assertThat(productService.getProducts()).hasSize(1);
        Assertions.assertThat(productService.getProducts().get(0)).
                isEqualTo(new Product(1, 100, "Product Name", testCategory, 10));
    }

    @Test
    void addProductToNonEmptyList() throws FileNotFoundException {
        //given
        FileService fileService = mock(FileService.class);
        when(fileService.readProductsFromFile()).thenReturn(new ArrayList<>());
        ProductService productService = new ProductService(fileService);
        Category testCategory = new Category("Fourth category name", 8);
        productService.addProduct(
                "Product Name1",
                100,
                "First category name",
                10);
        productService.addProduct(
                "Product Name2",
                100,
                "Second category name",
                10);
        productService.addProduct(
                "Product Name3",
                100,
                "Third category name",
                10);


        //when
        productService.addProduct(
                "Product Name4",
                100,
                "Fourth category name",
                10);

        //then
        Assertions.assertThat(productService.getProducts()).hasSize(4);
        Assertions.assertThat(productService.getProducts().get(3)).
                isEqualTo(new Product(7, 100, "Product Name4", testCategory, 10));
    }
}