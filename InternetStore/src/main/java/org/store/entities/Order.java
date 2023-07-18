package org.store.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Order {
    private static int lastID = 0;
    private final int ID;
    private final String orderNumber;
    private final String clientName;
    private final String clientSurName;
    private final String clientAddress;
    private final Map<Product, Integer> basket = new HashMap<>();
    private double numberSum;
    private OrderStatus status;

    public Order(final String orderNumber, final String clientName, final String clientSurName, final String clientAddress) {
        checkClientFullName(clientName, clientSurName);
        checkClientAddress(clientAddress);
        checkOrderNumber(orderNumber);

        this.orderNumber = orderNumber;
        this.clientName = clientName;
        this.clientSurName = clientSurName;
        this.clientAddress = clientAddress;
        this.ID = lastID++;
        this.status = OrderStatus.CREATED;
    }

    private void checkClientFullName(final String name, final String surName) {
        if(!name.matches("^[A-Za-z]+$") || !surName.matches("^[A-Za-z]+$")) {
            throw new IllegalArgumentException("Client name and surname must consist only of letters");
        }
    }

    private void checkClientAddress(final String address) {
        if (!address.matches("^([a-zA-Z+\\-0-9.]+)(\\s[\\\\/a-zA-Z+\\-0-9.]+){0,5}$")) {
            throw new IllegalArgumentException("Client address must consist with no more than 6 words");
        }
    }

    private void checkOrderNumber(final String number) {
        if (!number.matches("^[0-9]{8}$")) {
            throw new IllegalArgumentException("Order number must be 8-digit number");
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "ID=" + ID +
                ", orderNumber='" + orderNumber + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientSurName='" + clientSurName + '\'' +
                ", clientAddress='" + clientAddress + '\'' +
                ", basket=" + basket +
                ", numberSum=" + numberSum +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return ID == order.ID && Double.compare(order.numberSum, numberSum) == 0 &&
                Objects.equals(orderNumber, order.orderNumber) &&
                Objects.equals(clientName, order.clientName) &&
                Objects.equals(clientSurName, order.clientSurName) &&
                Objects.equals(clientAddress, order.clientAddress) &&
                Objects.equals(basket, order.basket) && status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, orderNumber, clientName, clientSurName, clientAddress, basket, numberSum, status);
    }
}
