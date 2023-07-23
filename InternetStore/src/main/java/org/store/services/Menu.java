package org.store.services;

import org.store.entities.Category;
import org.store.services.fileHandling.FileService;
import org.store.services.helper.Formatter;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    final private Scanner scanner = new Scanner(System.in);
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private int choice = 0;
    private final FileService fileService;

    public Menu() throws FileNotFoundException {
        fileService = new FileService();
        categoryService = new CategoryService(fileService);
        productService = new ProductService();
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
                case 1 -> {
                    System.out.println("\t" + Formatter.formatAllCategories(categoryService.getCategories()));
                }
                case 2 -> {
                    List<Category> categories = categoryService.getCategories();
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Podaj nazwę kategorii: ");
                    String name = scanner.nextLine();

                    // here I must update lastID from RAM, because every time I call
                    // readProductsFromFile(), I invoke Category constructor, which
                    // increments lastID pole
                    Category.setLastID(categories.get(categories.size() - 1).getID() + 1);
                    Category foundCategory = categories.stream()
                            .filter(category -> category.getName().equals(name))
                            .findAny()
                            .orElse(null);

                    if (foundCategory != null) {
                        System.out.println("-------------- ID [" + foundCategory.getID() + "] " +
                                foundCategory.getName() + " --------------");
                        System.out.println(Formatter.formatCategory(name, productService.getProducts()));
                    } else {
                        System.out.println("Niepoprawna kategoria");
                    }
                }
                case 3 -> {
                    System.out.println(" ++++++++++++ Dodanie kategorii ++++++++++++");
                    List<Category> categories = categoryService.getCategories();
                    Scanner scanner = new Scanner(System.in);
                    String newCategoryName;
                    Category.setLastID(categories.get(categories.size() - 1).getID() + 1);
                    List<String> categoriesNames = categories.stream()
                            .map(Category::getName)
                            .toList();
                    do {
                        System.out.print("Podaj nazwę dodawanej kategorii: ");
                        newCategoryName = scanner.nextLine();
                        if (!Category.isNameCorrect(newCategoryName)) {
                            System.out.println("\tNiepoprawna nazwa! Proszę podać nazwę do czterech słów bez cyfr i znaków specjalnych");
                        } else if (categoriesNames.contains(newCategoryName)) {
                            System.out.println("\tKategoria o takiej nazwie już istnieje! Proszę podać inną nazwę");
                        } else {
                            break;
                        }
                    } while (true);

                    categoryService.addCategory(newCategoryName);
                    //zapis
                    fileService.writeCategories(categories);

                    System.out.println("\tNowa kategoria została dodana");
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
                }
                case 4 -> {
                    List<Category> categories = categoryService.getCategories();
                    System.out.println("----------- Usunięcie kategorii -----------");
                    Scanner scanner = new Scanner(System.in);
                    String categoryToRemove;
                    boolean removeFlag = false;
                    List<String> categoriesNames = categories.stream()
                            .map(Category::getName)
                            .toList();
                    do {
                        System.out.print("Podaj nazwę kategorii do usunięcia( -1 żeby cofnąć): ");
                        categoryToRemove = scanner.nextLine();
                        if (categoryToRemove.equals("-1")) {
                            break;
                        }
                        if (!Category.isNameCorrect(categoryToRemove)) {
                            System.out.println("\tNiepoprawna nazwa! Proszę podać nazwę do czterech słów bez cyfr i znaków specjalnych");
                        } else if (!categoriesNames.contains(categoryToRemove)) {
                            System.out.println("\tKategoria o takiej nazwie nie istnieje! Proszę podać inną nazwę");
                        } else {
                            removeFlag = true;
                            break;
                        }
                    } while (true);

                    if (removeFlag) {
                        categoryService.removeCategory(categoryToRemove);
                        //aktualizacja
                        fileService.writeCategories(categories);
                        System.out.println("\tKategoria została usunięta");
                    }

                    System.out.println("--------------------------------------------");
                }
                case 5 -> localExitFlag = true;
                default -> System.out.println("\tNiepoprawny numer opcji!");
            }
        }
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

        FileService update = new FileService();
        update.writeProducts(productService.getProducts());
        System.out.println("**************************************");
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
            FileService update = new FileService();
            update.writeOrders(orderService.getOrderList());
        }
        System.out.println("****************************************");
    }
}
