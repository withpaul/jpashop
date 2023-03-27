package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // final에들만 주입
@Transactional(readOnly = true) // 설정하면 insert, update, delete와 같은거 반영안뎀
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional // 설정해야 readonly = true를 오버라이드해서 false로 먹음
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public Item findItem(Long id) {
        return itemRepository.findOne(id);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public List<Item> findByName(String name) {
        return itemRepository.findByName(name);
    }
}
