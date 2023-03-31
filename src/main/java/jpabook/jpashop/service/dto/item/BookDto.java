package jpabook.jpashop.service.dto.item;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookDto extends ItemDto {
    private String author;
    private String isbn;
}
