package org.store;

import org.store.entities.Order;
import org.store.entities.OrderStatus;
import org.store.services.OrderService;
import org.store.services.fileHandling.CreateData;
import org.store.services.CategoryService;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        CreateData createData = new CreateData();
        createData.createProducts();
        //createData.createCategories(null);

        OrderService orderService = new OrderService();

        orderService.addOrder();
        orderService.showAllOrders();
        orderService.showOrder();
        orderService.changeStatus();
        orderService.showOrder();


    }
}