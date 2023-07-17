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
        if (price < 0) {
            throw new IllegalArgumentException("Price lesser then zero cannot be assigned");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity lesser then zero cannot be assigned");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category must not be null");
        }
        if (!name.matches("^([ąęŁłśćźńóża-zA-Z+\\-0-9.]+)(\\s[ąęŁłśćźńóża-zA-Z+\\-0-9.]+){0,7}$") || name.length() > 50) {
            throw new IllegalArgumentException("Name should have no more than 50 characters and consist up to 8 words");
        }
        this.price = price;
        this.quantity = quantity;
        this.name = name;
        this.category = category;
        this.ID = lastID++;
    }

    private boolean isNameValid(final String name) {
        return name.matches("^([a-zA-Z+\\-0-9.]+)(\\s[a-zA-Z+\\-0-9.]+){0,7}$") && name.length() <= 50;
    }

    public void setID(int id) {
        this.ID = id;
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
                "id=" + ID +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return getID() == product.getID() &&
                Double.compare(product.getPrice(), getPrice()) == 0 &&
                getQuantity() == product.getQuantity() &&
                Objects.equals(getName(), product.getName()) &&
                Objects.equals(getCategory(), product.getCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID(), getPrice(), getName(), getCategory(), getQuantity());
    }
}
