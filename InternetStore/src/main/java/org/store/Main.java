package org.store;

import org.store.services.fileHandling.CreateData;
import org.store.services.CategoryService;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        CreateData createData = new CreateData();
        createData.createProducts();
        //createData.createCategories(null);

        CategoryService categoryService = new CategoryService();
        categoryService.addCategory();
        categoryService.showAllCategories();
        categoryService.removeCategory();
        categoryService.showAllCategories();
    }
}