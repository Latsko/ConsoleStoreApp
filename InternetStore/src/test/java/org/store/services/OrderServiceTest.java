package org.store.services;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.store.entities.Category;
import org.store.entities.Order;
import org.store.entities.OrderStatus;
import org.store.entities.Product;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest {

    @Test
    void removeOrder() {
        //given
        OrderService orderService = new OrderService();

        //when
        final ThrowableAssert.ThrowingCallable callable1 = () ->
                orderService.removeOrder(null);
        final ThrowableAssert.ThrowingCallable callable2 = () ->
                orderService.removeOrder(new Order("00000000", "name", "surname", "address"));

        //then
        assertThatThrownBy(callable1)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Method argument is null!");

        assertThatThrownBy(callable2)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("There is no elements to remove!");
    }

    @Test
    void addProductToOrder() {
        //given
        final OrderService orderService = new OrderService();
        final Order order = new Order("00000000", "name", "surname", "address");
        final Product product = new Product(1, "product", new Category("Category"), 1, 1);
        //when
        orderService.addProductToOrder(order, product, 1);
        //then
        assertThat(order.getBasket()).hasSize(1);
        assertThat(order.getBasket()).containsKey(product);
    }

    @Test
    void changeStatus() {
        //given
        Order order1 = new Order("00000001", "name", "surname", "address");
        Order order2 = new Order("00000002", "name", "surname", "address");
        Order order3 = new Order("00000003", "name", "surname", "address");
        Order order4 = new Order("00000004", "name", "surname", "address");
        final OrderService orderService = new OrderService();
        //when
        orderService.changeStatus(1, order1);
        orderService.changeStatus(2, order2);
        orderService.changeStatus(3, order3);
        orderService.changeStatus(4, order4);

        //then
        assertThat(order1.getStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(order2.getStatus()).isEqualTo(OrderStatus.PREPARING);
        assertThat(order3.getStatus()).isEqualTo(OrderStatus.SENT);
        assertThat(order4.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void addOrder() {
        //given
        final OrderService orderService = new OrderService();
        
        //when
        orderService.addOrder("Name", "SurName", "Address");
        orderService.addOrder("Name", "SurName", "Address");
        orderService.addOrder("Name", "SurName", "Address");

        //then
        assertThat(orderService.getOrderList()).hasSize(3);
    }

    @Test
    void getEmptyOrderList() {
        //given
        final OrderService orderService = new OrderService();

        //when
        final List<Order> orderList = orderService.getOrderList();

        //then
        assertThat(orderList).isEmpty();
    }
}