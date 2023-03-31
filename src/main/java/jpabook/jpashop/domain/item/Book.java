package jpabook.jpashop.domain.item;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // protected Book()과 같은 기능(무조건 createbook강제)
public class Book extends Item {
    private String author;
    private String isbn;

    //==생성 메서드==//
    public static Book createBook(Long id, String name, String isbn, String author, int price, int stockQuantity) {
        Book book = new Book();
        book.setId(id);
        book.setName(name);
        book.setIsbn(isbn);
        book.setAuthor(author);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        return book;
    }

    public void changeAuthor(String author) {
        this.author = author;
    }
}
