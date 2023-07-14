package org.example.Entities;

import java.util.Objects;

public class Product {
    private static int lastID = 0;
    private int ID;
    private final double price;
    private final String name;
    private final Category category;
    private final int quantity;

    public Product(double price, String name, Category category, int quantity) {
        this.price = price;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.ID = lastID++;
    }

    public int getID() {
        return ID;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "price=" + price +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Double.compare(product.price, price) == 0 &&
                quantity == product.quantity &&
                Objects.equals(name, product.name) &&
                Objects.equals(category, product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, name, category, quantity);
    }
}
