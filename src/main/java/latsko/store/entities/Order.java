package latsko.store.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Order {
    private static final String NAME_VALIDATION_REGEX = "^[ąęŁłśćźńóżA-Za-z]+$";
    private static final String ADDRESS_VALIDATION_REGEX = "^([ąęŁłśćźńóża-zA-Z+\\-0-9.]+)(\\s[\\\\/ąęŁłśćźńóża-zA-Z+\\-0-9.]+){0,5}$";
    private static final String ORDER_NUMBER_VALIDATION_REGEX = "^\\d{8}$";
    private final int id;
    private final String orderNumber;
    private final String clientName;
    private final String clientSurName;
    private final String clientAddress;
    private final Map<Product, Integer> basket;
    private double numberSum;
    private OrderStatus status;

    public Order(final int id, final String orderNumber, final String clientName, final String clientSurName, final String clientAddress) {
        checkClientFullName(clientName, clientSurName);
        checkClientAddress(clientAddress);
        checkOrderNumber(orderNumber);

        this.orderNumber = orderNumber;
        this.clientName = clientName;
        this.clientSurName = clientSurName;
        this.clientAddress = clientAddress;
        this.id = id;
        this.status = OrderStatus.CREATED;
        this.basket = new HashMap<>();
        numberSum = 0;
    }

    public Order(final int id,
                 final String orderNumber,
                 final String clientName,
                 final String clientSurName,
                 final String clientAddress,
                 final double numberSum,
                 final OrderStatus status,
                 final Map<Product, Integer> basket) {
        checkClientFullName(clientName, clientSurName);
        checkClientAddress(clientAddress);
        checkOrderNumber(orderNumber);

        this.orderNumber = orderNumber;
        this.clientName = clientName;
        this.clientSurName = clientSurName;
        this.clientAddress = clientAddress;
        this.id = id;
        this.status = status;
        this.basket = basket;
        this.numberSum = numberSum;
    }

    public static boolean isClientAddressCorrect(final String address) {
        return !address.matches(ADDRESS_VALIDATION_REGEX);
    }

    public static boolean isClientNameCorrect(final String name) {
        return name.matches(NAME_VALIDATION_REGEX);
    }

    public static boolean isClientSurNameCorrect(final String surName) {
        return isClientNameCorrect(surName);
    }

    private void checkClientFullName(final String name, final String surName) {
        if (!name.matches(NAME_VALIDATION_REGEX) || !surName.matches(NAME_VALIDATION_REGEX)) {
            throw new IllegalArgumentException("Client name and surname must consist only of letters");
        }
    }

    private void checkClientAddress(final String address) {
        if (!address.matches(ADDRESS_VALIDATION_REGEX)) {
            throw new IllegalArgumentException("Client address must consist with no more than 6 words");
        }
    }

    private void checkOrderNumber(final String number) {
        if (!number.matches(ORDER_NUMBER_VALIDATION_REGEX)) {
            throw new IllegalArgumentException("Order number must be 8-digit number");
        }
    }

    public void setStatus(final OrderStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientSurName() {
        return clientSurName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void addToSum(final double price, final int quantity) {
        this.numberSum = this.numberSum + price * quantity;
    }

    public Map<Product, Integer> getBasket() {
        return basket;
    }

    public double getNumberSum() {
        return numberSum;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    @Override
    public String toString() {
        return "Order{" +
                "ID=" + id +
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
        return id == order.id && Double.compare(order.numberSum, numberSum) == 0 &&
                Objects.equals(orderNumber, order.orderNumber) &&
                Objects.equals(clientName, order.clientName) &&
                Objects.equals(clientSurName, order.clientSurName) &&
                Objects.equals(clientAddress, order.clientAddress) &&
                Objects.equals(basket, order.basket) && status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderNumber, clientName, clientSurName, clientAddress, basket, numberSum, status);
    }
}
