package sk.stuba.fei.uim.oop.assignment3.book.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.stuba.fei.uim.oop.assignment3.author.data.Author;
import sk.stuba.fei.uim.oop.assignment3.book.web.bodies.BookRequest;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    private Author author;

    private int pages;

    private int amount;

    private int lendCount;

    public Book(BookRequest r, Author author) {
        this.name = r.getName();
        this.description = r.getDescription();
        this.pages = r.getPages();
        this.author = author;
        this.amount = r.getAmount();
        this.lendCount = r.getLendCount();
    }
}
