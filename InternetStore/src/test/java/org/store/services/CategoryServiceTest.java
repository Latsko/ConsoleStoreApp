package org.store.services;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.store.entities.Category;
import org.store.services.fileHandling.FileService;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CategoryServiceTest {
    @Test
    void addCategoryToEmptyListTest() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        when(fileService.readCategoriesFromFile()).thenReturn(new ArrayList<>());
        CategoryService categoryService = new CategoryService(fileService);
        List<Category> categories = categoryService.getCategories();

        //when
        categoryService.addCategory("Category");

        //then
        assertThat(categories)
                .hasSize(1);
    }

    @Test
    void addCategoryToNonEmptyList() throws FileNotFoundException {
        //given
        final List<Category> list = new ArrayList<>();
        final FileService fileService = mock(FileService.class);
        final Category category1 = new Category("first category");
        final Category category2 = new Category("second category");
        final Category category3 = new Category("third category");
        list.add(category1);
        list.add(category2);
        list.add(category3);

        when(fileService.readCategoriesFromFile()).thenReturn(list);
        final CategoryService categoryService = new CategoryService(fileService);
        final List<Category> categories = categoryService.getCategories();

        //when
        categoryService.addCategory("fourth category");
        final List<String> listOfCategoryNames = categories.stream().map(Category::getName).toList();

        //then
        assertThat(categories)
                .hasSize(4);
        for (int i = 0; i < categories.size(); i++) {
            assertThat(categories.get(i).getName()).isEqualTo(listOfCategoryNames.get(i));
        }
    }

    @Test
    void removeCategoryEmptyList() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        when(fileService.readCategoriesFromFile()).thenReturn(null);
        final CategoryService categoryService = new CategoryService(fileService);

        //when
        final ThrowableAssert.ThrowingCallable callable = () ->
                categoryService.removeCategory("some category");

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("There is no elements to remove!");
    }

    @Test
    void removeCategoryNonEmptyList() throws FileNotFoundException {
        //given
        final List<Category> list = new ArrayList<>();
        final Category category1 = new Category("first category");
        final Category category2 = new Category("second category");
        final Category category3 = new Category("third category");
        list.add(category1);
        list.add(category2);
        list.add(category3);
        final FileService fileService = mock(FileService.class);
        when(fileService.readCategoriesFromFile()).thenReturn(list);
        final CategoryService categoryService = new CategoryService(fileService);
        final List<Category> categories = categoryService.getCategories();

        //when
        categoryService.removeCategory("third category");

        //then
        assertThat(categories)
                .hasSize(2)
                .contains(category1, category2);
    }

    @Test
    void getEmptyCategoryList() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        when(fileService.readCategoriesFromFile()).thenReturn(null);
        CategoryService categoryService = new CategoryService(fileService);

        //when
        List<Category> categories = categoryService.getCategories();

        //then
        assertThat(categories).isNull();
    }

    @Test
    void getNonEmptyCategoryList() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        final List<Category> categories = new ArrayList<>();
        categories.add(new Category("test one"));
        categories.add(new Category("test two"));
        categories.add(new Category("test three"));
        when(fileService.readCategoriesFromFile()).thenReturn(categories);
        final CategoryService categoryService = new CategoryService(fileService);

        //when
        List<Category> expected = categoryService.getCategories();

        //then
        assertThat(categories).isEqualTo(expected);
    }
}