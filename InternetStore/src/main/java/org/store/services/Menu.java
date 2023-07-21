package org.store.services;

import org.store.services.fileHandling.CreateData;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu {
    final private Scanner scanner = new Scanner(System.in);
    private CreateData createData;
    private ProductService productService;
    private CategoryService categoryService;
    private OrderService orderService;
    private int choice = 0;

    public Menu() throws FileNotFoundException {
        createData = new CreateData();
        productService = new ProductService();
        categoryService = new CategoryService();
        orderService = new OrderService();
        boolean exit = false;
        while (!exit) {
        System.out.println("===================== Menu =====================");
            System.out.println("[1] Zamówienia");
            System.out.println("[2] Kategorie produktów");
            System.out.println("[3] Produkty");
            System.out.println("[4] Exit");

            System.out.print("Wybierz opcję: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:
                    productServiceOptions();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("\tNiepoprawny numer opcji!");
            }
        }
        System.out.println("========== Koniec działania programu ===========");
    }

    private void productServiceOptions() throws FileNotFoundException {
        boolean localExitFlag = false;
        choice = 0;
        while (!localExitFlag) {
        System.out.println("******* Operacje na produktach *******");
            System.out.println("\t1 - Pokaż wszystkie produkty");
            System.out.println("\t2 - Pokaż informacje o podanym produkcie");
            System.out.println("\t3 - Dodaj produkt");
            System.out.println("\t4 - Usuń produkt");
            System.out.println("\t5 - Cofnij");

            System.out.print("Wybierz opcję: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    productService.showAllProducts();
                    break;
                case 2:
                    productService.showProduct();
                    break;
                case 3:
                    productService.addProduct();
                    break;
                case 4:
                    productService.removeProduct();
                    break;
                case 5:
                    localExitFlag = true;
                    break;
                default:
                    System.out.println("\tNiepoprawny numer opcji!");
            }
        }
        CreateData update = new CreateData();
        update.writeProducts(productService.getProducts());
        System.out.println("**************************************");
    }
}
