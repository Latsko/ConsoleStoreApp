package latsko.store.services;

import latsko.store.entities.Category;
import latsko.store.entities.Order;
import latsko.store.entities.OrderStatus;
import latsko.store.entities.Product;
import latsko.store.services.file_handling.FileService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class OrderServiceTest {

    @Test
    void removeOrderWithEmptyList() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        final OrderService orderService = new OrderService(fileService);

        //when
        final ThrowableAssert.ThrowingCallable callable1 = () ->
                orderService.removeOrder(null);
        final ThrowableAssert.ThrowingCallable callable2 = () ->
                orderService.removeOrder(new Order(0, "00000000", "name", "surname", "address"));

        //then
        assertThatThrownBy(callable1)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Method argument is null!");

        assertThatThrownBy(callable2)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("There is no elements to remove!");
    }

    @Test
    void removeOrderWithNonEmptyList() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        final OrderService orderService = new OrderService(fileService);
        orderService.addOrder("NameOne", "SurnameOne", "Address1");
        orderService.addOrder("NameTwo", "SurnameTwo", "Address2");
        orderService.addOrder("NameThree", "SurnameThree", "Address3");
        final Order orderToRemove = orderService.getOrderList().get(1);
        final List<Order> expected = new ArrayList<>();
        expected.add(orderService.getOrderList().get(0));
        expected.add(orderService.getOrderList().get(2));

        //when
        orderService.removeOrder(orderToRemove);

        //then
        assertThat(orderService.getOrderList()).isEqualTo(expected);
    }

    @Test
    void addProductToOrderWithEmptyBasket() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        final OrderService orderService = new OrderService(fileService);
        final Order order = new Order(0, "00000000", "name", "surname", "address");
        final Product product = new Product(1, 10, "product", new Category("Category", 1), 1);
        //when
        orderService.addProductToOrder(order, product, 1);
        //then
        assertThat(order.getBasket()).hasSize(1);
        assertThat(order.getBasket()).containsKey(product);
    }

    @Test
    void addProductToOrderWithNonEmptyBasket() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        final OrderService orderService = new OrderService(fileService);
        final Order order = new Order(0, "00000000", "name", "surname", "address");
        final Product product1 = new Product(1, 10, "product1", new Category("Category", 1), 1);
        final Product product2 = new Product(2, 10, "product2", new Category("Category", 2), 1);
        final Product product3 = new Product(3, 10, "product3", new Category("Category", 3), 1);
        orderService.addProductToOrder(order, product1, 1);
        orderService.addProductToOrder(order, product2, 1);

        //when
        orderService.addProductToOrder(order, product3, 1);

        //then
        assertThat(order.getBasket()).hasSize(3);
        assertThat(order.getBasket()).containsKey(product1);
        assertThat(order.getBasket()).containsKey(product2);
        assertThat(order.getBasket()).containsKey(product3);
    }

    @Test
    void changeStatus() throws FileNotFoundException {
        //given
        Order order1 = new Order(0, "00000001", "name", "surname", "address");
        Order order2 = new Order(1, "00000002", "name", "surname", "address");
        Order order3 = new Order(2, "00000003", "name", "surname", "address");
        Order order4 = new Order(3, "00000004", "name", "surname", "address");
        final FileService fileService = mock(FileService.class);
        final OrderService orderService = new OrderService(fileService);
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
    void addOrder() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        final OrderService orderService = new OrderService(fileService);

        //when
        orderService.addOrder("Name", "SurName", "Address");
        orderService.addOrder("Name", "SurName", "Address");
        orderService.addOrder("Name", "SurName", "Address");

        //then
        assertThat(orderService.getOrderList()).hasSize(3);
    }

    @Test
    void getEmptyOrderList() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        final OrderService orderService = new OrderService(fileService);

        //when
        final List<Order> orderList = orderService.getOrderList();

        //then
        assertThat(orderList).isEmpty();
    }

    @Test
    void getNonEmptyOrderList() throws FileNotFoundException {
        //given
        final FileService fileService = mock(FileService.class);
        final OrderService orderService = new OrderService(fileService);
        orderService.addOrder("Name", "SurName", "Address");
        orderService.addOrder("Name", "SurName", "Address");
        orderService.addOrder("Name", "SurName", "Address");

        //when
        final List<Order> orderList = orderService.getOrderList();

        //then
        assertThat(orderList).hasSize(3);
    }
}