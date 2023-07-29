package org.store.services.helper;

import org.junit.jupiter.api.Test;
import org.store.entities.Category;
import org.store.entities.Order;
import org.store.entities.Product;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class FormatterTest {

    @Test
    void formatCategory() {
        //given
        final Category category = new Category("name", 1);

        //when
        final String expected = Formatter.formatCategory(category);

        //then
        assertThat(expected).isEqualTo("ID [1] name");
    }

    @Test
    void formatProduct() {
        //given
        final Category category = new Category("category name", 1);
        final Product product = new Product(0, 12, "product name", category, 10);

        //when
        String expected = Formatter.formatProduct(product);

        //then
        assertThat(expected).isEqualTo("[0] product name\n\tCena: 12.0\n\tKategoria: category name\n\tIlość w magazynie: 10");

    }

    @Test
    void formatOrder() {
        //given
        final Order order = new Order(0, "00000000", "Name", "Surname", "address");

        //when
        String expected = Formatter.formatOrder(order);

        //then
        assertThat(expected).isEqualTo("""
                00000000
                [0]\tClient info\s
                \tName: Name Surname
                \tAddress: address
                === Aktualne produkty w koszyku ===\s

                Kosz jest pusty

                Łączna suma zamówienia: 0.0
                Status zamówienia: CREATED""");
    }
}