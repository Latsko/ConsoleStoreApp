package org.store.services;

import org.junit.jupiter.api.Test;
import org.store.entities.Category;
import org.store.entities.Product;
import org.store.services.fileHandling.FileService;

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
        final Category testCategory = new Category("test");
        final List<Product> products = new ArrayList<>();
        products.add(new Product(1, "test product 1", testCategory, 10));
        products.add(new Product(2, "test product 2", testCategory, 20));
        products.add(new Product(3, "test product 3", testCategory, 30));
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
        final Category testCategory = new Category("test");
        final List<Product> products = new ArrayList<>();
        products.add(new Product(1, "test product 1", testCategory, 10));
        products.add(new Product(2, "test product 2", testCategory, 20));
        products.add(new Product(3, "test product 3", testCategory, 30));
        when(fileService.readProductsFromFile()).thenReturn(products);
        ProductService productService = new ProductService(fileService);

        //when
        Product productByID0 = productService.getProductByID(0);
        Product productByID1 = productService.getProductByID(1);
        Product productByID2 = productService.getProductByID(2);

        //then
        assertThat(productByID0.getID()).isEqualTo(0);
        assertThat(productByID1.getID()).isEqualTo(1);
        assertThat(productByID2.getID()).isEqualTo(2);

    }

    @Test
    void removeProduct() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        final Category testCategory = new Category("test");
        final List<Product> products = new ArrayList<>();
        final Product product1 = new Product(1, "test product 1", testCategory, 10);
        final Product product2 = new Product(2, "test product 2", testCategory, 20);
        final Product product3 = new Product(3, "test product 3", testCategory, 30);
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
    void addProduct() {
    }
}