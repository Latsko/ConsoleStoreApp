package org.store.services;

import org.store.entities.Order;
import org.store.entities.OrderStatus;
import org.store.entities.Product;
import org.store.services.fileHandling.FileService;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

public class OrderService {
    private final List<Order> orderList;
    private int lastID;

    public OrderService(final FileService fileService) throws FileNotFoundException {
        orderList = fileService.readOrdersFromFile();
        lastID = orderList.size();
    }

    public void removeOrder(final Order searched) {
        if (searched == null) {
            throw new NullPointerException("Method argument is null!");
        } else if (orderList.isEmpty()) {
            throw new IllegalArgumentException("There is no elements to remove!");
        }
        orderList.remove(searched);
    }

    public void addProductToOrder(final Order order, final Product searched, final int quantity) {
        if (order == null) {
            throw new IllegalArgumentException("No order under that ID!");
        }
        order.addToSum(searched.getPrice(), quantity);
        order.getBasket().put(searched, quantity);
    }

    public void changeStatus(final int status, final Order searched) {
        switch (status) {
            case 1 -> searched.setStatus(OrderStatus.PAID);
            case 2 -> searched.setStatus(OrderStatus.PREPARING);
            case 3 -> searched.setStatus(OrderStatus.SENT);
            case 4 -> searched.setStatus(OrderStatus.CANCELLED);
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
}
