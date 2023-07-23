package org.store.services;

import org.store.services.fileHandling.CreateData;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu {
    final private Scanner scanner = new Scanner(System.in);
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private int choice = 0;

    public Menu() throws FileNotFoundException {
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
                case 1 -> orderServiceOption();
                case 2 -> categoryServiceOptions();
                case 3 -> productServiceOptions();
                case 4 -> exit = true;
                default -> System.out.println("\tNiepoprawny numer opcji!");
            }
        }
        System.out.println("========== Koniec działania programu ===========");
    }

    private void orderServiceOption() throws FileNotFoundException {
        boolean localExitFlag = false;
        choice = 0;
        while (!localExitFlag) {
            System.out.println("******* Operacje na zamówieniach *******");
            System.out.println("\t1 - Pokaż wszystkie zamówienia");
            System.out.println("\t2 - Pokaż informacje o podanym zamówieniu");
            System.out.println("\t3 - Dodaj zamówienie");
            System.out.println("\t4 - Usuń zamówienie");
            System.out.println("\t5 - Zmień status zamówienia");
            System.out.println("\t6 - Pokaż status zamówienia");
            System.out.println("\t7 - Dodaj produkt do zamówienia");
            System.out.println("\t8 - Cofnij");

            System.out.print("Wybierz opcję: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1 -> orderService.showAllOrders();
                case 2 -> orderService.showOrder();
                case 3 -> orderService.addOrder();
                case 4 -> orderService.removeOrder();
                case 5 -> orderService.changeStatus();
                case 6 -> orderService.showOrderStatus();
                case 7 -> orderService.addProductToOrder();
                case 8 -> localExitFlag = true;
                default -> System.out.println("\tNiepoprawny numer opcji!");
            }
            CreateData update = new CreateData();
            update.writeOrders(orderService.getOrderList());
        }
        System.out.println("****************************************");
    }

    private void categoryServiceOptions() throws FileNotFoundException {
        boolean localExitFlag = false;
        choice = 0;
        while (!localExitFlag) {
            System.out.println("******* Operacje na kategoriach *******");
            System.out.println("\t1 - Pokaż wszystkie kategorie");
            System.out.println("\t2 - Pokaż informacje o podanej kategorii");
            System.out.println("\t3 - Dodaj kategorię");
            System.out.println("\t4 - Usuń kategorię");
            System.out.println("\t5 - Cofnij");

            System.out.print("Wybierz opcję: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1 -> categoryService.showAllCategories();
                case 2 -> categoryService.showCategory();
                case 3 -> categoryService.addCategory();
                case 4 -> categoryService.removeCategory();
                case 5 -> localExitFlag = true;
                default -> System.out.println("\tNiepoprawny numer opcji!");
            }
        }
        CreateData update = new CreateData();
        update.writeProducts(productService.getProducts());
        System.out.println("**************************************");
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
                case 1 -> productService.showAllProducts();
                case 2 -> productService.showProduct();
                case 3 -> productService.addProduct();
                case 4 -> productService.removeProduct();
                case 5 -> localExitFlag = true;
                default -> System.out.println("\tNiepoprawny numer opcji!");
            }
        }
        CreateData update = new CreateData();
        update.writeProducts(productService.getProducts());
        System.out.println("**************************************");
    }
}
