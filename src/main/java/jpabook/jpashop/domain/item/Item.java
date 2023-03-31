package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.service.dto.item.ItemDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
// 한 테이블에 상속받은 애들 다 때려밖는거고, TABLE_PERCLASS는 BOOK, AUTHOR 다 각 테이블 만드는거고 그외는 찾아봐
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 구분이 될 떄 BOOK이면 어떻게 할꺼야 이런거??;;  -> 싱글테이블이니까 저장할때 구분할 수 있어야하는 건데 그걸 설정하는거래..
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items") // Category 테이블에 있는 items 필드에 의해 맵핑
    private List<Category> categories = new ArrayList<>();

    /**
     * entity내에서 값 변경을 서비스 단계에서 하는게 아니라 entity 단계에서 하는게 응집도가 좋으며 객체지향적이다.
     */
    public void addStock(int stockQuantity) {
        this.stockQuantity += stockQuantity;
    }

    // setter가지고 변경하지말고.. 이런식으로 변경해야해
    public void removeStock(int stockQuantity) {
        int resultStockQuantity = this.stockQuantity - stockQuantity;

        if (resultStockQuantity < 1) {
            throw new NotEnoughStockException("need more stock");
        }

        this.stockQuantity = resultStockQuantity;
    }

    public void changeInfo(ItemDto itemDto) {
        this.name = itemDto.getName();
        this.price = itemDto.getPrice();
        this.stockQuantity = itemDto.getStockQuantity();
    }
}
