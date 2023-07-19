package org.store.services;


import org.store.entities.Category;
import org.store.entities.Product;
import org.store.services.fileHandling.CreateData;
import org.store.services.fileHandling.ReadData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductService {
    private final List<Product> products;

    private final ReadData readData = new ReadData();

    public ProductService() throws FileNotFoundException {
        File productFile = CreateData.getProductsPath().toFile();
        if(productFile.exists()) {
            products = new ArrayList<>(readData.readProductsFromFile());
        } else {
            products = new ArrayList<>();
        }

    }

    public void showProduct(String name) throws FileNotFoundException {
        List<Product> productList = new ArrayList<>(readData.readProductsFromFile());
        Product foundProduct = products.stream().filter(product -> product.getName()
                        .equals(name))
                .findAny()
                .orElse(null);

        if(foundProduct != null) {
            System.out.println("{" + foundProduct.getID() + "}" + foundProduct.getName());
            productList.stream()
                    .filter(product -> product.getName().equals(name))
                    .forEach(System.out::println);
        } else {
            System.out.println("Niepoprawny produkt");
        }
    }

    public void showAllProducts() {
        products.stream()
                .map(Product::getName)
                .forEach(name -> System.out.println("\t" + name));
    }

    public void addProduct() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String newProductName;
        List<String> productsNames = products.stream()
                .map(Product::getName)
                .toList();
        do {
            System.out.print("Podaj nazwę dodawanego produktu: ");
            newProductName = scanner.nextLine();
            if (!Product.nameIsCorrect(newProductName)) {
                System.out.println("\tNiepoprawna nazwa. Proszę podać nazwę do 8 wyrazów.");
            } else if (productsNames.contains(newProductName)) {
                System.out.println("\tProdukt o takiej nazwie już istnieje. Proszę podać inną nazwę.");
            } else {
                break;
            }

        } while (true);

        double price;
        do {
            System.out.println("Podaj cenę: ");
            price = scanner.nextInt();
        } while (!(price > 0));

        Category category;
        String name;
        List<Category> categoryList = new ArrayList<>(readData.readCategoriesFromFile());
        List<String> categoryNameList = categoryList.stream()
                .map(current -> current.getName())
                .toList();
        do{
            System.out.print("Podaj nazwę kategorii: ");
            name = scanner.nextLine();
            if (!Category.nameIsCorrect(name)) {
                System.out.println("\tNiepoprawna nazwa. Proszę podać nazwę do 8 wyrazów.");
            } else if (productsNames.contains(name)) {
                System.out.println("\tProdukt o takiej nazwie już istnieje. Proszę podać inną nazwę.");
            } else {
                break;
            }

        } while (true);


//        CreateData update = new CreateData();
////        update.createProducts(products);
//         gdzie na szaro tam mam bledy

    }
}
