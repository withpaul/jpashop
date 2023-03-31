package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class ItemServiceTest {
    @Autowired private ItemService itemService;

    @Test
    public void 아이템저장() throws Exception {
        //given
        Book book = new Book();
        book.setName("김종진");
        //when
        itemService.saveItem(book);
        List<Item> findItems = itemService.findByName("반지의제왕");
        //then
        Assertions.assertThat(findItems).isNotEmpty();
    }


}