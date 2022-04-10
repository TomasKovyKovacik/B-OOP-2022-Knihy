package sk.stuba.fei.uim.oop.assignment3.book.web.bodies;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookUpdateRequest {
    private String name;
    private String description;
    private int pages;
    private long author;
}
