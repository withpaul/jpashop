package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired OrderService orderService;
    @Autowired EntityManager em;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();
        Book book = createBook("반지의제왕", 10000, 10);

        //when
        int orderCount = 3;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order order = orderRepository.findOne(orderId);
        Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER);
        Assertions.assertThat(order.getOrderItems().size()).isEqualTo(1);
        Assertions.assertThat(order.getTotalPrice()).isEqualTo(orderCount*10000);
        Assertions.assertThat(book.getStockQuantity()).isEqualTo(10-orderCount);

    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();
        Book item = createBook("시골 jpa", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        Assertions.assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.CANCLE);
        Assertions.assertThat(item.getStockQuantity()).isEqualTo(10);
    }
    
    @Test
    public void 상품주문_재고수량초가() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("반지의제왕", 10000, 10);

        //when
        int orderCount = 11;

        //then
        assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), item.getId(), orderCount));
    }


    private Book createBook(String name, int price, int stockQuantity) {
        Book book = Book.createBook(name, "isbn", "김종진", price, stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("김종진");
        member.setAddress(new Address("판교역로", "109", "111-111"));
        em.persist(member);
        return member;
    }
}