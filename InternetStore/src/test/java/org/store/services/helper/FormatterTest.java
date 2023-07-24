package org.store.services.helper;

import org.junit.jupiter.api.Test;
import org.store.entities.Category;
import org.store.entities.Product;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class FormatterTest {

    @Test
    void formatCategory() {
        //given
        final Category category = new Category("name", 1);

        //when
        String expected = Formatter.formatCategory(category);

        //then
        assertThat(expected).isEqualTo("ID [1] name");
    }

    @Test
    void formatProduct() {
        //given
        final Category category = new Category("category name", 1);
        final Product product = new Product(12, "product name", category, 10);

        //when
        String expected = Formatter.formatProduct(product);

        //then
        assertThat(expected).isEqualTo("[0] product name\n\tCena: 12.0\n\tKategoria: category name\n\tIlość w magazynie: 10");

    }

    @Test
    void formatOrder() {
        //given

        //when

        //then

    }
}