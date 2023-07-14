package org.example;

import org.example.Entities.CreateData;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
            CreateData createData = new CreateData();
            createData.createProducts();
    }
}