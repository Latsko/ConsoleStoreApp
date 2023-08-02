package latsko.store.services.helper;

import latsko.store.entities.Category;
import latsko.store.entities.Order;
import latsko.store.entities.Product;

import java.util.stream.Collectors;

public class Formatter {
    public static String formatCategory(Category category) {
        return "ID [" + category.id() + "] " + category.name();
    }

    public static String formatProduct(Product product) {
        return "[" + product.id() + "] " + product.name() +
                "\n\tCena: " + product.price() + "\n\tKategoria: " + product.category().name()
                + "\n\tIlość w magazynie: " + product.quantity();
    }

    public static String formatOrder(Order order) {
        String formatted = order.getOrderNumber() + "\n" + "[" + order.getId() + "]"
                + "\tClient info \n\tName: " + order.getClientName() + " " + order.getClientSurName() +
                "\n\tAddress: " + order.getClientAddress() + "\n" + "=== Aktualne produkty w koszyku === \n\n";
        if (order.getBasket().isEmpty()) {
            formatted += "Kosz jest pusty";
        } else {
            formatted += order.getBasket().entrySet().stream()
                    .map(entry -> entry.getValue() + " - " + entry.getKey().name() + "\n")
                    .collect(Collectors.joining())
                    .trim();
        }
        formatted += "\n\nŁączna suma zamówienia: " + order.getNumberSum() +
                "\nStatus zamówienia: " + order.getStatus();

        return formatted;
    }
}
