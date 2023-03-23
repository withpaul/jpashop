package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // ManyToOne, OneToOne 다 기본이 LAZY인데 JAPQL쓰다가 N+1로 쿼리 호출할 수가 있으니 무조건 LAZY써
    @JoinColumn(name = "member_id") // foreign key 이름이 member_id다
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    // mappedBy -> orderItem 테이블에 있는 order 필드에 의해 맵핑된거다?
    // cascade -> 전파해서 orderItems 컬렉션에 있는 애들도 persist 전파를해줌
    private List<OrderItem> orderItems = new ArrayList<>();

    // 모든 Entity는 각자 persist 저장하고 싶으면 각각해줘야하는데 cascade 옵션을아래와 같이 걸면
    // order가 persiste될때 각각 다 전파해주니 코드가 줄어든다.
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
}
