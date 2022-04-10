package sk.stuba.fei.uim.oop.assignment3.book.web.bodies;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest {
    private String name;
    private String description;
    private long author;
    private int pages;
    private int amount;
    private int lendCount;
}
