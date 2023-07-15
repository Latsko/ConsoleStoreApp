package org.example.Entities;

import java.util.Objects;

public class Category {
    private static int lastID = 0;
    private final int ID;
    private final String name;

    public Category(String name) {
        if(!name.matches("(^[A-Za-z]+(\\s[A-Za-z]+){0,3})$")) {
            throw new IllegalArgumentException("Category name must consist of letters only and up to 4 words");
        }
        this.name = name;
        this.ID = lastID++;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return ID == category.ID && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, name);
    }
}
