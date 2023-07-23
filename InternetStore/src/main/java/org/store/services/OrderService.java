package org.store.services;

import org.store.entities.Order;
import org.store.entities.OrderStatus;
import org.store.entities.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderService {
    private final List<Order> orderList;

    public OrderService() {
        orderList = new ArrayList<>();
    }

    public void removeOrder(Order searched) {
        if (searched == null) {
            throw new IllegalArgumentException("Method argument is null!");
        }
        orderList.remove(searched);
    }

    public void addProductToOrder(Order order, Product searched, int quantity) {
        if (order == null) {
            throw new IllegalArgumentException("No order under that ID!");
        }
        order.addToSum(searched.getPrice(), quantity);
        order.getBasket().put(quantity, searched);
    }

    public void changeStatus(final int status, final Order searched) {
        switch (status) {
            case 1 -> searched.setStatus(OrderStatus.PAID);
            case 2 -> searched.setStatus(OrderStatus.PREPARING);
            case 3 -> searched.setStatus(OrderStatus.SENT);
            case 4 -> searched.setStatus(OrderStatus.CANCELLED);
        }
    }

    public void addOrder(String name, String surName, String address) {
        orderList.add(new Order(createUniqueOrderNumber(), name, surName, address));
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
