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
    private final ReadData readData = new ReadData();
    private final List<Product> products;

    public ProductService() throws FileNotFoundException {
        File productFile = CreateData.getProductsPath().toFile();
        if (productFile.exists()) {
            products = new ArrayList<>(readData.readProductsFromFile());
        } else {
            products = new ArrayList<>();
        }

    }

    public void showProduct(String name) throws FileNotFoundException {
        Product foundProduct = products.stream().filter(product -> product.getName()
                        .equals(name))
                .findAny()
                .orElse(null);

        if (foundProduct != null) {
            System.out.println("[" + foundProduct.getID() + "] " + foundProduct.getName() +
                    "\n\tPrice: " + foundProduct.getPrice() + "\n\tCategory: " + foundProduct.getCategory().getName()
                    + "\n\tQuantity: " + foundProduct.getQuantity());
        } else {
            System.out.println("Niepoprawny produkt");
        }
    }

    public void showAllProducts() {
        products.stream()
                .map(product -> "[" + product.getID() + "] " + product.getName())
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
            System.out.print("Podaj cenę: ");
            price = scanner.nextDouble();
            if(price <= 0) {
                System.out.println("Cena nie może być mniejsza lub równa zero!");
            } else {
                scanner.nextLine();
                break;
            }
        } while (true);

        Category category;
        String inputName;
        // need this lists for further category names validation
        List<Category> categoryList = new ArrayList<>(readData.readCategoriesFromFile());
        List<String> categoryNameList = categoryList.stream()
                .map(Category::getName)
                .toList();
        do {
            System.out.print("Podaj nazwę kategorii: ");
            inputName = scanner.nextLine();
            if (!Category.nameIsCorrect(inputName)) {
                System.out.println("\tNiepoprawna nazwa. Proszę podać nazwę do 8 wyrazów.");
            } else if (productsNames.contains(inputName)) {
                System.out.println("\tProdukt o takiej nazwie już istnieje. Proszę podać inną nazwę.");
            } else if (!categoryNameList.contains(inputName)) {
                System.out.println("\tNie ma takiej kategorii!\nLista aktualnych kategorii:");
                categoryNameList.forEach(System.out::println);
            } else {
                category = new Category(inputName);
                break;
            }
        } while (true);

        int quantity;
        do {
            System.out.print("Podaj ilość w magazynie: ");
            quantity = scanner.nextInt();
            if(quantity <= 0) {
                System.out.println("Ilość nie może być mniejsza lub równa zero!");
            } else {
                scanner.nextLine();
                break;
            }
        } while (true);

        products.add(new Product(price, newProductName, category, quantity));
        CreateData update = new CreateData();
        update.createProducts(products);

    }
}
