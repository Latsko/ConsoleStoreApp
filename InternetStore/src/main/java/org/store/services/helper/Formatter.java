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

    public static String formatOrder(Order searchedOrder) {
        String formatted = searchedOrder.getOrderNumber() + "\n" + "[" + searchedOrder.getID() + "]"
                + "\tClient info \n\tName: " + searchedOrder.getClientName() + searchedOrder.getClientSurName() +
                "\n\tAddress: " + searchedOrder.getClientAddress() + "\n" + "=== Aktualne produkty w koszyku === \n\n";
        if (searchedOrder.getBasket().isEmpty()) {
            formatted += "Kosz jest pusty";
        } else {
            formatted += searchedOrder.getBasket().entrySet().stream()
                    .map(entry -> entry.getValue().getName() + " - " + entry.getKey() + "\n")
                    .collect(Collectors.joining())
                    .trim();
        }
        formatted += "\n\nŁączna suma zamówienia: " + searchedOrder.getNumberSum() +
                "\nStatus zamówienia: " + searchedOrder.getStatus();

        return formatted;
    }
}
