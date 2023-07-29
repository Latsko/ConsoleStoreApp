package org.store.services;

import org.store.entities.Category;
import org.store.entities.Order;
import org.store.entities.OrderStatus;
import org.store.entities.Product;
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
        productService = new ProductService(fileService);
        orderService = new OrderService(fileService);
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
                case 1 -> categoryService.getCategories().stream()
                        .map(Formatter::formatCategory)
                        .forEach(System.out::println);
                case 2 -> {
                    List<Category> categories = categoryService.getCategories();
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Podaj nazwę kategorii: ");
                    String name = scanner.nextLine();

                    // here I must update lastID from RAM, because every time I call
                    // readProductsFromFile(), I invoke Category constructor, which
                    // increments lastID pole
                    //Category.setLastID(categories.get(categories.size() - 1).getID() + 1);
                    Category foundCategory = categories.stream()
                            .filter(category -> category.getName().equals(name))
                            .findAny()
                            .orElse(null);

                    if (foundCategory != null) {
                        System.out.println("-------------- " + Formatter.formatCategory(foundCategory) + " --------------");
                        productService.getProducts().stream()
                                .filter(product -> product.getCategory().getName().equals(name))
                                .map(product -> "[" + product.getID() + "] " + product.getName() + "\n")
                                .forEach(System.out::print);
                    } else {
                        System.out.println("Niepoprawna kategoria");
                    }
                }
                case 3 -> {
                    System.out.println(" ++++++++++++ Dodanie kategorii ++++++++++++");
                    List<Category> categories = categoryService.getCategories();
                    Scanner scanner = new Scanner(System.in);
                    String newCategoryName;
                    //Category.setLastID(categories.get(categories.size() - 1).getID() + 1);
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
                case 1 -> showAllProducts();
                case 2 -> {
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Podaj nazwę produktu: ");
                    String name = scanner.nextLine();

                    Product foundProduct = productService.getProducts().stream()
                            .filter(product -> product.getName().equals(name))
                            .findAny()
                            .orElse(null);

                    if (foundProduct != null) {
                        System.out.println(Formatter.formatProduct(foundProduct));
                    } else {
                        System.out.println("Niepoprawny produkt");
                    }
                }
                case 3 -> {
                    Scanner scanner = new Scanner(System.in);
                    String inputProductName;
                    double inputPrice;
                    String inputCategoryName;
                    int inputQuantity;

                    // need this lists for further category names validation
                    List<Category> categoryList = fileService.readCategoriesFromFile();
                    List<String> categoryNameList = categoryList.stream()
                            .map(Category::getName)
                            .toList();

                    List<String> productsNames = productService.getProducts().stream()
                            .map(Product::getName)
                            .toList();


                    System.out.println(" +++++++++++++ Dodanie produktu +++++++++++++");
                    // get product name from input
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

                    // get product price from input
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

                    // get category name from input
                    do {
                        System.out.print("Podaj nazwę kategorii: ");
                        inputCategoryName = scanner.nextLine();
                        if (!Category.isNameCorrect((inputCategoryName))) {
                            System.out.println("\tNiepoprawna nazwa. Proszę podać nazwę do 8 wyrazów bez znaków specjalnych.");
                        } else if (!categoryNameList.contains(inputCategoryName)) {
                            System.out.println("\tNie ma takiej kategorii!\nLista aktualnych kategorii:");
                            categoryNameList.forEach(System.out::println);
                        } else {
                            break;
                        }
                    } while (true);

                    // get product quantity from input
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

                    productService.addProduct(inputProductName, inputPrice, inputCategoryName, inputQuantity);
                    fileService.writeProducts(productService.getProducts());

                    System.out.println("\tNowy produkt był dodany do listy");
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
                }
                case 4 -> {
                    System.out.println("-------- Usunięcie produktu z listy --------");
                    int inputID;
                    Product searched;
                    Scanner scanner = new Scanner(System.in);
                    showAllProducts();
                    do {
                        System.out.print("Podaj ID usuwanego produktu( -1 - żeby cofnąć): ");
                        inputID = scanner.nextInt();
                        searched = getProductByID(inputID);

                        if (inputID == -1) {
                            return;
                        }
                        if (searched != null) {
                            break;
                        } else {
                            System.out.println("Nie ma produktu pod takim id, spróbuj podać inny.");
                        }
                    } while (true);

                    productService.removeProduct(searched);
                    fileService.writeProducts(productService.getProducts());

                    System.out.println("--------- Produkt został usunięty ----------");
                    System.out.println("--------------------------------------------");
                }
                case 5 -> localExitFlag = true;
                default -> System.out.println("\tNiepoprawny numer opcji!");
            }
        }
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
                case 1 -> showAllOrders();
                case 2 -> {
                    List<Order> orderList = orderService.getOrderList();
                    if (orderList.isEmpty()) {
                        System.out.println("\tJeszcze nie dodano żadnego zamówienia!");
                        break;
                    }
                    showAllOrders();
                    final String orderNum;
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Podaj numer zamówienia: ");
                    orderNum = scanner.nextLine();

                    if (!orderList.isEmpty()) {
                        Order searchedOrder = orderList.stream().
                                filter(order -> orderNum.equals(order.getOrderNumber()))
                                .findFirst()
                                .orElse(null);
                        if (searchedOrder != null) {
                            System.out.println(Formatter.formatOrder(searchedOrder));
                        } else {
                            System.out.println("Order pod takim numerem nie istnieje!");
                        }
                    } else {
                        System.out.println("Na razie nie istnieje żadnego zamówienia");
                    }
                }
                case 3 -> {
                    System.out.println("++++++++++++++++++ Dodawanie nowego zamówienia ++++++++++++++++++");
                    Scanner scanner = new Scanner(System.in);
                    String name, surName, address;

                    do {
                        System.out.print("Imię klienta: ");
                        name = scanner.nextLine();
                        if (!Order.isClientNameCorrect(name)) {
                            System.out.println("\tImię nie powinno zawierać znaków specjalnych lub cyfr!");
                        } else {
                            break;
                        }
                    } while (true);

                    do {
                        System.out.print("Nazwisko klienta: ");
                        surName = scanner.nextLine();
                        if (!Order.isClientSurNameCorrect(surName)) {
                            System.out.println("\tNazwisko nie powinno zawierać znaków specjalnych lub cyfr!");
                        } else {
                            break;
                        }
                    } while (true);

                    do {
                        System.out.print("Address klienta: ");
                        address = scanner.nextLine();
                        if (Order.isClientAddressCorrect(address)) {
                            System.out.println("\tAdres nie powinien się składać z więcej, niż sześć słów!");
                        } else {
                            break;
                        }
                    } while (true);

                    orderService.addOrder(name, surName, address);
                    fileService.writeOrders(orderService.getOrderList());

                    System.out.println("\tZamówienie zostało dodane");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                }
                case 4 -> {
                    if (orderService.getOrderList().isEmpty()) {
                        System.out.println("\tNie ma zamówień do usunięcia!");
                        break;
                    }
                    System.out.println("--------------------- Usunięcie zamówienia ---------------------");
                    Scanner scanner = new Scanner(System.in);
                    Order searched;
                    showAllOrders();
                    do {
                        String inputOrderNum;
                        System.out.print("Wprowadź numer zamówienia, które chcesz usunąć: ");
                        inputOrderNum = scanner.nextLine();
                        searched = orderService.getOrderList().stream()
                                .filter(order -> inputOrderNum.equals(order.getOrderNumber()))
                                .findFirst()
                                .orElse(null);

                        if (searched == null) {
                            System.out.println("Nie udało się znaleźć zamówienie pod takim numerem. Spróbuj ponownie.");
                        } else {
                            break;
                        }
                    } while (true);

                    orderService.removeOrder(searched);
                    fileService.writeOrders(orderService.getOrderList());

                    System.out.println("Zamówienie zostało usunięte.");
                    System.out.println("----------------------------------------------------------------");
                }
                case 5 -> {
                    if (orderService.getOrderList().isEmpty()) {
                        System.out.println("\tNie ma aktualnych zamówień!");
                        break;
                    }
                    System.out.println("¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤ Zmień status zamówienia ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤");
                    System.out.print("Proszę podać ID zamówienia: ");
                    Scanner scanner = new Scanner(System.in);
                    int id = scanner.nextInt();
                    int status;
                    Order searched = orderService.getOrderList().stream()
                            .filter(order -> order.getID() == id)
                            .findFirst()
                            .orElse(null);

                    if (searched != null) {
                        while (searched.getStatus().equals(OrderStatus.CREATED)) {
                            System.out.print("1 - PAID\n2 - PREPARING\n3 - SENT\n4 - CANCELLED\nWybierz nowy status: ");
                            status = scanner.nextInt();
                            if (status >= 1 && status < 5) {
                                orderService.changeStatus(status, searched);
                            } else {
                                System.out.println("Podałeś niepoprawną liczbę!");
                            }
                        }
                    } else {
                        System.out.println("\tNiepoprawny ID");
                    }

                    System.out.println("¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤");
                }
                case 6 -> {
                    if (orderService.getOrderList().isEmpty()) {
                        System.out.println("\tNie ma aktualnych zamówień!");
                        break;
                    }
                    System.out.println("¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤ Pokaż status zamówienia ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤");
                    System.out.print("Proszę podać ID zamówienia: ");
                    Scanner scanner = new Scanner(System.in);
                    int id = scanner.nextInt();

                    Order searched = orderService.getOrderList().stream()
                            .filter(order -> order.getID() == id)
                            .findFirst()
                            .orElse(null);

                    if (searched != null) {
                        System.out.println(searched.getStatus());
                    } else {
                        System.out.println("Niepoprawny ID");
                    }
                    System.out.println("¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤");
                }
                case 7 -> {
                    if (orderService.getOrderList().isEmpty()) {
                        System.out.println("\tNie ma aktualnych zamówień!");
                        break;
                    }
                    System.out.println("+++++++++++++++ Dodawanie produktu do zamówienia ++++++++++++++++");
                    int productID, orderID, quantity;
                    Scanner scanner = new Scanner(System.in);

                    List<Product> productList = productService.getProducts();
                    List<Integer> productIDs = productList.stream()
                            .map(Product::getID)
                            .toList();
                    List<Integer> orderIDs = orderService.getOrderList().stream()
                            .map(Order::getID)
                            .toList();
                    Product searched;
                    showAllProducts();
                    do {
                        System.out.print("Wybierz ID produktu: ");
                        productID = scanner.nextInt();
                        if (!productIDs.contains(productID)) {
                            System.out.println("\tWybrałeś niepoprawny ID!");
                        } else {
                            searched = productService.getProductByID(productID);
                            if (searched == null) {
                                throw new IllegalArgumentException("No product under that ID!");
                            }
                            break;
                        }
                    } while (true);

                    showAllOrders();
                    do {
                        System.out.print("Wybierz ID ordera do którego włożymy produkt: ");
                        orderID = scanner.nextInt();
                        if (!orderIDs.contains(orderID)) {
                            System.out.println("\tWybrałeś niepoprawny ID!");
                        } else {
                            break;
                        }
                    } while (true);

                    do {
                        System.out.print("Podaj ile produktów chcesz zamówić: ");
                        quantity = scanner.nextInt();
                        if (quantity <= 0) {
                            System.out.println("\tIlość powinna być większa od zera!");
                        } else if (quantity > searched.getQuantity()) {
                            System.out.println("\tNie ma tyle produktów w magazynie!");
                        } else {
                            break;
                        }
                    } while (true);
                    Order order = getOrderByID(orderID, orderService.getOrderList());

                    orderService.addProductToOrder(order, searched, quantity);
                    fileService.writeOrders(orderService.getOrderList());

                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                }
                case 8 -> localExitFlag = true;
                default -> System.out.println("\tNiepoprawny numer opcji!");
            }
        }
        System.out.println("****************************************");
    }

    public Product getProductByID(int id) {
        for (Product product : productService.getProducts()) {
            if (product.getID() == id) {
                return product;
            }
        }
        return null;
    }

    public Order getOrderByID(int id, List<Order> orderList) {
        for (Order order : orderList) {
            if (id == order.getID()) {
                return order;
            }
        }
        return null;
    }

    public void showAllOrders() {
        if (orderService.getOrderList().isEmpty()) {
            System.out.println("Na razie nie ma żadnego zamówienia.");
        }
        orderService.getOrderList().stream()
                .map(order -> "[" + order.getID() + "] " + order.getOrderNumber())
                .forEach(System.out::println);
    }

    public void showAllProducts() {
        productService.getProducts().stream()
                .map(product -> "[" + product.getID() + "] " + product.getName())
                .forEach(name -> System.out.println("\t" + name));
    }
}
