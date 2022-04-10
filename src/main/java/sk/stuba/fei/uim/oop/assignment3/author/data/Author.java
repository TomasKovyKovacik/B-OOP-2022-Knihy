package sk.stuba.fei.uim.oop.assignment3.author.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.stuba.fei.uim.oop.assignment3.author.web.bodies.AuthorRequest;
import sk.stuba.fei.uim.oop.assignment3.book.data.Book;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String surname;

    @OneToMany(cascade = {CascadeType.REMOVE})
    private List<Book> books;

    public Author() {
        this.books = new ArrayList<>();
    }

    public Author(AuthorRequest r) {
        this.books = new ArrayList<>();
        this.name = r.getName();
        this.surname = r.getSurname();
    }
}
