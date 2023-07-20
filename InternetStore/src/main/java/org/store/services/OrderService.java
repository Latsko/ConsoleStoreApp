package org.store.services;

import org.store.entities.Order;
import org.store.entities.OrderStatus;
import org.store.entities.Product;

import java.io.FileNotFoundException;
import java.util.*;

public class OrderService {
    private final List<Order> orderList;

    public OrderService() {
        orderList = new ArrayList<>();
    }

    public Order getOrderByID(int id) {
        for (Order order : orderList) {
            if (id == order.getID()) {
                return order;
            }
        }
        return null;
    }

    public void removeOrder() {
        Scanner scanner = new Scanner(System.in);
        showAllOrders();
        do {
            String inputOrderNum;
            System.out.print("Wprowadź numer zamówienia, które chcesz usunąć: ");
            inputOrderNum = scanner.nextLine();
            Order searched = orderList.stream()
                    .filter(order -> inputOrderNum.equals(order.getOrderNumber()))
                    .findFirst()
                    .orElse(null);

            if (searched != null) {
                orderList.remove(searched);
                System.out.println("Zamówienie zostało usunięte.");
                break;
            } else {
                System.out.println("Nie udało się znaleźć zamówienie pod takim numerem. Spróbuj ponownie.");
            }
        } while(true);
    }

    public void addProductToOrder() throws FileNotFoundException {
        int productID, orderID, quantity;
        Scanner scanner = new Scanner(System.in);
        ProductService productService = new ProductService();
        List<Product> productList = productService.getProducts();
        List<Integer> productIDs = productList.stream()
                .map(Product::getID)
                .toList();
        List<Integer> orderIDs = orderList.stream()
                .map(Order::getID)
                .toList();
        Product searched;

        productService.showAllProducts();
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

        Order order = getOrderByID(orderID);
        if (order != null) {
            order.addToSum(searched.getPrice(), quantity);
            order.getBasket().put(quantity, searched);
        } else {
            throw new IllegalArgumentException("No order under that ID!");
        }
    }

    public void showOrderStatus() {
        System.out.print("Proszę podać ID zamówienia: ");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();

        Order searched = orderList.stream()
                .filter(order -> order.getID() == id)
                .findFirst()
                .orElse(null);

        if (searched != null) {
            System.out.println(searched.getStatus());
        } else {
            System.out.println("Niepoprawny ID");
        }
    }

    public void changeStatus() {
        System.out.print("Proszę podać ID zamówienia: ");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();

        Order searched = orderList.stream()
                .filter(order -> order.getID() == id)
                .findFirst()
                .orElse(null);

        if (searched != null) {
            while (searched.getStatus().equals(OrderStatus.CREATED)) {
                System.out.print("1 - PAID\n2 - PREPARING\n3 - SENT\n4 - CANCELLED\nWybierz nowy status: ");
                int status = scanner.nextInt();
                switch (status) {
                    case 1 -> searched.setStatus(OrderStatus.PAID);
                    case 2 -> searched.setStatus(OrderStatus.PREPARING);
                    case 3 -> searched.setStatus(OrderStatus.SENT);
                    case 4 -> searched.setStatus(OrderStatus.CANCELLED);
                    default -> System.out.println("Podałeś niepoprawną liczbę!");
                }
            }
        }
    }

    public void addOrder() {
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
            if (!Order.isClientAddressCorrect(address)) {
                System.out.println("\tAdres nie powinien się składać z więcej, niż sześciu słów!");
            } else {
                break;
            }
        } while (true);

        orderList.add(new Order(createUniqueOrderNumber(), name, surName, address));

    }

    public void showAllOrders() {
        if(orderList.isEmpty()) {
            System.out.println("Na razie nie ma żadnego zamówienia.");
        }
        orderList.stream()
                .map(order -> "[" + order.getID() + "] " + order.getOrderNumber())
                .forEach(System.out::println);
    }

    public void showOrder() {
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
                System.out.println(searchedOrder.getOrderNumber());
                System.out.println("[" + searchedOrder.getID() + "]" + "\tClient info \n\tName: "
                        + searchedOrder.getClientName() + searchedOrder.getClientSurName() +
                        "\n\tAddress: " + searchedOrder.getClientAddress());

                System.out.println("=== Aktualne produkty w koszyku === \n");
                if(searchedOrder.getBasket().isEmpty()) {
                    System.out.println("Kosz jest pusty");
                } else {
                    searchedOrder.getBasket().entrySet().stream()
                            .map(entry -> entry.getValue().getName() + " - " + entry.getKey())
                            .forEach(System.out::println);
                }
                System.out.println("\nŁączna suma zamówienia: " + searchedOrder.getNumberSum());
                System.out.println("Status zamówienia: " + searchedOrder.getStatus());
            } else {
                System.out.println("Order pod takim numerem nie istnieje!");
            }
        } else {
            System.out.println("Na razie nie istnieje żadnego zamówienia");
        }
    }

    private String createUniqueOrderNumber() {
        if (!orderList.isEmpty()) {
            String generated;
            List<String> orderNumbers = orderList.stream()
                    .map(Order::getOrderNumber)
                    .toList();
            do {
                generated = generateOrderNumber();
            } while (orderNumbers.contains(generated));
        }
        return generateOrderNumber();
    }

    private String generateOrderNumber() {
        StringBuilder number = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            number.append(random.nextInt(0, 10));
        }
        return number.toString();
    }
}
