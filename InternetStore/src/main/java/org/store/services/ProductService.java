package org.store.services;

import org.store.entities.Category;
import org.store.entities.Product;
import org.store.services.fileHandling.FileService;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductService {
    private final FileService fileService = new FileService();
    private final List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public ProductService() throws FileNotFoundException {
        products = fileService.readProductsFromFile();
    }


    public Product getProductByID(int id) {
        for (Product product : products) {
            if (product.getID() == id) {
                return product;
            }
        }
        return null;
    }

    public void removeProduct() {
        System.out.println("-------- Usunięcie produktu z listy --------");
        Scanner scanner = new Scanner(System.in);
        int inputID;
        showAllProducts();

        do {
            System.out.print("Podaj ID usuwanego produktu( -1 - żeby cofnąć): ");
            inputID = scanner.nextInt();

            Product searched = getProductByID(inputID);
            if (inputID == -1) {
                return;
            }
            if (searched != null) {
                products.remove(searched);
                break;
            } else {
                System.out.println("Nie ma produktu pod takim id, spróbuj podać inny.");
            }
        } while (true);

        System.out.println("--------- Produkt został usunięty ----------");
        System.out.println("--------------------------------------------");
    }

    public void showProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj nazwę produktu: ");
        String name = scanner.nextLine();

        Product foundProduct = products.stream()
                .filter(product -> product.getName().equals(name))
                .findAny()
                .orElse(null);

        if (foundProduct != null) {
            System.out.println("[" + foundProduct.getID() + "] " + foundProduct.getName() +
                    "\n\tCena: " + foundProduct.getPrice() + "\n\tKategoria: " + foundProduct.getCategory().getName()
                    + "\n\tIlość w magazynie: " + foundProduct.getQuantity());
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
        String inputProductName;
        List<String> productsNames = products.stream()
                .map(Product::getName)
                .toList();
        System.out.println(" +++++++++++++ Dodanie produktu +++++++++++++");
        do {
            System.out.print("Podaj nazwę dodawanego produktu: ");
            inputProductName = scanner.nextLine();
            if (!Product.nameIsCorrect(inputProductName)) {
                System.out.println("\tNiepoprawna nazwa. Proszę podać nazwę do 8 wyrazów.");
            } else if (productsNames.contains(inputProductName)) {
                System.out.println("\tProdukt o takiej nazwie już istnieje. Proszę podać inną nazwę.");
            } else {
                break;
            }

        } while (true);

        double inputPrice;
        do {
            System.out.print("Podaj cenę: ");
            inputPrice = scanner.nextDouble();
            if (inputPrice <= 0) {
                System.out.println("Cena nie może być mniejsza lub równa zero!");
            } else {
                scanner.nextLine();
                break;
            }
        } while (true);

        Category inputCategory;
        String inputCategoryName;
        // need this lists for further category names validation
        List<Category> categoryList = new ArrayList<>(fileService.readCategoriesFromFile());
        List<String> categoryNameList = categoryList.stream()
                .map(Category::getName)
                .toList();
        do {
            System.out.print("Podaj nazwę kategorii: ");
            inputCategoryName = scanner.nextLine();
            if (!Category.isNameCorrect((inputCategoryName))) {
                System.out.println("\tNiepoprawna nazwa. Proszę podać nazwę do 8 wyrazów bez znaków specjalnych.");
            } else if (!categoryNameList.contains(inputCategoryName)) {
                System.out.println("\tNie ma takiej kategorii!\nLista aktualnych kategorii:");
                categoryNameList.forEach(System.out::println);
            } else {
                inputCategory = new Category(inputCategoryName);
                break;
            }
        } while (true);

        int inputQuantity;
        do {
            System.out.print("Podaj ilość w magazynie: ");
            inputQuantity = scanner.nextInt();
            if (inputQuantity <= 0) {
                System.out.println("Ilość nie może być mniejsza lub równa zero!");
            } else {
                scanner.nextLine();
                break;
            }
        } while (true);

        products.add(new Product(inputPrice, inputProductName, inputCategory, inputQuantity));

        System.out.println("\tNowy produkt był dodany do listy");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
    }
}
