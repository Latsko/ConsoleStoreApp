package org.example;

import org.example.Entities.fileHandling.CreateData;
import org.example.Entities.Product;
import org.example.Entities.fileHandling.ReadData;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        CreateData createData = new CreateData();
        createData.createProducts();
        createData.createCategories();

        ReadData readData = new ReadData();
        List<Product> products = readData.readProductsFromFile();
        products.forEach(System.out::println);

    }
}