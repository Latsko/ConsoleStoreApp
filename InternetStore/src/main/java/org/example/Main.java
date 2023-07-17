package org.example;

import org.example.Entities.fileHandling.CreateData;
import org.example.Services.CategoryService;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        CreateData createData = new CreateData();
        createData.createProducts();
        createData.createCategories();

        CategoryService categoryService = new CategoryService();
        categoryService.showAllCategories();
        categoryService.showCategory("TV");
        categoryService.showCategory("Sport");
    }
}