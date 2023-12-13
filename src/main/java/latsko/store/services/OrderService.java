package latsko.store.services;

import latsko.store.entities.Order;
import latsko.store.entities.OrderStatus;
import latsko.store.entities.Product;
import latsko.store.services.file_handling.FileService;
import latsko.store.services.helper.CreateData;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class OrderService {
    private final List<Order> orderList;
    private int lastID;

    /**
     * Constructor uses dependency injected FileService to create orders.
     * List of orders is either initialized from a file or if no such file
     * is provided file empty list being created.
     * ID is updated to maintain order of IDs assigned to every new created object
     *
     * @throws FileNotFoundException if file with Categories does not exist
     **/
    public OrderService(final FileService fileService) throws FileNotFoundException {
        if (CreateData.getOrdersPath().toFile().exists()) {
            orderList = fileService.readOrdersFromFile();
        } else {
            orderList = new ArrayList<>();
        }
        updateID();
    }

    /**
     * This method removes order given by parameter
     *
     * @param searched name of the order
     * @throws NullPointerException     Method argument is null!
     * @throws IllegalArgumentException There is no element to remove!
     **/
    public void removeOrder(final Order searched) {
        if (searched == null) {
            throw new NullPointerException("Method argument is null!");
        } else if (orderList.isEmpty()) {
            throw new IllegalArgumentException("There is no elements to remove!");
        }
        orderList.remove(searched);
        updateID();
    }


    public void addProductToOrder(final Order order, final Product searched, final int quantity) {
        if (order == null) {
            throw new IllegalArgumentException("No order under that ID!");
        }
        order.addToSum(searched.price(), quantity);
        order.getBasket().put(searched, quantity);
    }

    public void changeStatus(final int status, final Order searched) {
        switch (status) {
            case 1 -> searched.setStatus(OrderStatus.PAID);
            case 2 -> searched.setStatus(OrderStatus.PREPARING);
            case 3 -> searched.setStatus(OrderStatus.SENT);
            case 4 -> searched.setStatus(OrderStatus.CANCELLED);
            default -> System.out.println("Searched not found");
        }
    }

    /**
     * This method adds new order to list and gives it incremented last ID and unique number
     *
     * @param name    customer name
     * @param surName customer surname
     * @param address customer address
     **/
    public void addOrder(final String name, final String surName, final String address) {
        orderList.add(new Order(lastID++, createUniqueOrderNumber(), name, surName, address));
    }

    /**
     * This method ensures that an 8-digit number for each order would not be repeated.
     *
     * @return string containing 8 digit unique number for order
     **/
    private String createUniqueOrderNumber() {
        String generated;
        if (!orderList.isEmpty()) {
            List<String> orderNumbers = orderList.stream()
                    .map(Order::getOrderNumber)
                    .toList();
            do {
                generated = generateOrderNumber();
            } while (orderNumbers.contains(generated));
        } else {
            generated = generateOrderNumber();
        }
        return generated;
    }

    /**
     * This method draws 8-digit number to a string
     *
     * @return string containing 8-digit number
     **/
    private String generateOrderNumber() {
        StringBuilder number = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            number.append(random.nextInt(0, 10));
        }
        return number.toString();
    }

    /**
     * This method simply returns list of orders
     *
     * @return orders
     **/
    public List<Order> getOrderList() {
        return orderList;
    }

    /**
     * This method helps to keep track for what last ID for orders is
     **/
    private void updateID() {
        if (orderList.isEmpty()) {
            lastID = 0;
        } else {
            lastID = orderList.stream()
                    .mapToInt(Order::getId)
                    .max()
                    .orElseThrow(NoSuchElementException::new);
        }
        lastID++;
    }
}
