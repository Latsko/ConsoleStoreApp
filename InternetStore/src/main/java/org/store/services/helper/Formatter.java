package org.store.services.helper;

import org.store.entities.Category;
import org.store.entities.Order;
import org.store.entities.Product;

import java.util.stream.Collectors;

public class Formatter {
    public static String formatCategory(Category category) {
        return "ID [" + category.getID() + "] " + category.getName();
    }

    public static String formatProduct(Product product) {
        return "[" + product.getID() + "] " + product.getName() +
                "\n\tCena: " + product.getPrice() + "\n\tKategoria: " + product.getCategory().getName()
                + "\n\tIlość w magazynie: " + product.getQuantity();
    }

    public static String formatOrder(Order order) {
        String formatted = order.getOrderNumber() + "\n" + "[" + order.getID() + "]"
                + "\tClient info \n\tName: " + order.getClientName() + " " + order.getClientSurName() +
                "\n\tAddress: " + order.getClientAddress() + "\n" + "=== Aktualne produkty w koszyku === \n\n";
        if (order.getBasket().isEmpty()) {
            formatted += "Kosz jest pusty";
        } else {
            formatted += order.getBasket().entrySet().stream()
                    .map(entry -> entry.getValue() + " - " + entry.getKey().getName() + "\n")
                    .collect(Collectors.joining())
                    .trim();
        }
        formatted += "\n\nŁączna suma zamówienia: " + order.getNumberSum() +
                "\nStatus zamówienia: " + order.getStatus();

        return formatted;
    }
}
