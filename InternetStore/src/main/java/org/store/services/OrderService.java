package org.store.services;

import org.store.entities.Category;
import org.store.entities.Order;
import org.store.entities.OrderStatus;
import org.store.entities.Product;

import java.util.*;

public class OrderService {
    private static Random random;
    private final List<Order> orderList;

    public OrderService() {
        orderList = new ArrayList<>();
    }

    ////////////////////////////////////////////// tu wczoraj skończyłem //////////////////////////////////////////////
    public void addProductToOrder(final int productID, final int orderID, final int quantity) {
        ProductService productService = new ProductService();
        // tu ma być lista produktów, zapisana do zmiennej przy pomocy metody statycznej ProductService, czyli
        //List<Product> productList = ProductService.getProductList();
        // czy coś w tym stylu

        //sprawdzenie, czy quantity produktu nie jest mniejsze od tego, które podaliśmy
        Product testProduct =  new Product(1, "name", new Category("category"), 10);
        Order testOrder = orderList.get(orderID);
        testOrder.getBasket().put(quantity, testProduct);

        orderList.get(orderID).addToSum(testProduct.getPrice(), quantity);

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
                // got to format basket, so it shows in a more pleasing way
                System.out.println("=== Aktualne produkty w koszyku === \n" + searchedOrder.getBasket());
                System.out.println("Łączna suma zamówienia: " + searchedOrder.getNumberSum());
                System.out.println("Status zamówienia: " + searchedOrder.getStatus());
            } else {
                System.out.println("Order pod takim numerem nie istnieje!");
            }
        } else {
            System.out.println("Na razie nie istnieje żadnego zamówienia");
        }
    }

    public String createUniqueOrderNumber() {
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
        random = new Random();
        for (int i = 0; i < 8; i++) {
            number.append(random.nextInt(0, 10));
        }
        return number.toString();
    }
}
