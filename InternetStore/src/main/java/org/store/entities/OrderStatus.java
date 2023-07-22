package org.store.entities;

public enum OrderStatus {
    PAID,
    CANCELLED,
    SENT,
    PREPARING,
    CREATED;

    public static OrderStatus getOrderFromString(final String orderName) {
        switch (orderName) {
            case "PAID" -> {
                return PAID;
            }
            case "CANCELLED" -> {
                return CANCELLED;
            }
            case "PREPARING" -> {
                return PREPARING;
            }
            case "CREATED" -> {
                return CREATED;
            }
            default -> System.out.println("Wrong order status was given!");
        }
        return null;
    }
}
