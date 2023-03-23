package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // 내장 타입 클래스에 Embeddable을 쓰면 사실 안써도되나 양쪽에 써야 아 내장되는구나를 인지하기 쉬움
    private Address address;

    @OneToMany(mappedBy = "member") // order 테이블에 있는 member 필드에 의해 맵핑된거다?
    private List<Order> orders = new ArrayList<>();

}
