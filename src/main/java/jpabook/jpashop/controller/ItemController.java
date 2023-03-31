package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.dto.item.BookDto;
import jpabook.jpashop.service.dto.item.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createBookForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm bookForm) {
        Book book = Book.createBook(
                null,
                bookForm.getName(),
                bookForm.getIsbn(),
                bookForm.getAuthor(),
                bookForm.getPrice(),
                bookForm.getStockQuantity()
        );
        book.setName(bookForm.getName());
        book.setIsbn(bookForm.getIsbn());
        book.setAuthor(bookForm.getAuthor());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateForm(@PathVariable Long itemId, Model model) {
        Book item = (Book) itemService.findItem(itemId);
        BookForm bookForm = new BookForm();
        bookForm.setId(item.getId());
        bookForm.setName(item.getName());
        bookForm.setIsbn(item.getIsbn());
        bookForm.setPrice(item.getPrice());
        bookForm.setAuthor(item.getAuthor());
        bookForm.setStockQuantity(item.getStockQuantity());

        model.addAttribute("form", bookForm);
        return "items/updateItemForm";
    }

    @PostMapping(value = "/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form) {
        // 말도안되는 사례이지만, dirth checking을 사용해보기 위해 바꿔봄
        ItemDto itemDto = new ItemDto();
        itemDto.setId(form.getId());
        itemDto.setName(form.getName());
        itemDto.setPrice(form.getPrice());
        itemDto.setStockQuantity(form.getStockQuantity());
        itemService.updateItem(itemDto);

        return "redirect:/items";
    }
}
