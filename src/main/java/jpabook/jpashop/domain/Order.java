package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자 protected order()과 같은 기능
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // ManyToOne, OneToOne 다 기본이 EAGER인데 JQPL쓰다가 N+1로 쿼리 호출할 수가 있으니 무조건 LAZY써
    @JoinColumn(name = "member_id") // foreign key 이름이 member_id다
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    // order를 persist하면 여기 들어온 컬랙션도 다 강제로 persist해준다.
    // mappedBy -> orderItem 테이블에 있는 order 필드에 의해 맵핑된거다?
    // cascade -> 전파해서 orderItems 컬렉션에 있는 애들도 persist 전파를해줌
    // orderItem order에서만 관리하고 따른데에서 참조하는게 없기 때문에 cascade써도 됐엇다.
    private List<OrderItem> orderItems = new ArrayList<>();

    // order를 persist하면 여기 들어온 컬랙션도 다 강제로 persist해준다.
    // 모든 Entity는 각자 persist 저장하고 싶으면 각각해줘야하는데 cascade 옵션을아래와 같이 걸면
    // order가 persiste될때 각각 다 전파해주니 코드가 줄어든다.
    // devlivery는 order에서만 관리하고 따른데에서 참조하는게 없기 때문에 cascade써도 됐엇다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    private OrderStatus status; // 주문 상태 ENUM 클래스(ORDER, CANCEL)

    //==양방향일 때 연관관계 메서드 쓰면 편하다==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성자를 이런식으로 생성 말고 아래 생성메머스드로 강제화시킬때 사용(위에 lombok 으로 대체함)
//    protected Order() {
//
//    }
    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        for (OrderItem orderItem:
             orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCLE);
        for (OrderItem orderItem:
                orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem:
                orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
