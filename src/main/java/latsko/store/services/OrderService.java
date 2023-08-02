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

    public OrderService(final FileService fileService) throws FileNotFoundException {
        if (CreateData.getOrdersPath().toFile().exists()) {
            orderList = fileService.readOrdersFromFile();
        } else {
            orderList = new ArrayList<>();
        }
        updateID();
    }

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

    public void addOrder(final String name, final String surName, final String address) {
        orderList.add(new Order(lastID++, createUniqueOrderNumber(), name, surName, address));
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

    public List<Order> getOrderList() {
        return orderList;
    }

    private void updateID() {
        lastID = orderList.stream()
                .mapToInt(Order::getId)
                .max()
                .orElseThrow(NoSuchElementException::new);
        lastID++;
    }
}
