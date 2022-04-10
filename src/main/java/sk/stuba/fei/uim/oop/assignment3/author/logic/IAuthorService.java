package sk.stuba.fei.uim.oop.assignment3.author.logic;

import sk.stuba.fei.uim.oop.assignment3.author.data.Author;
import sk.stuba.fei.uim.oop.assignment3.author.web.bodies.AuthorRequest;
import sk.stuba.fei.uim.oop.assignment3.book.data.Book;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;

import java.util.List;

public interface IAuthorService {
    List<Author> getAll();

    Author create(AuthorRequest request);

    Author getById(long id) throws NotFoundException;

    Author update(long id, AuthorRequest request) throws NotFoundException;

    void delete(long id) throws NotFoundException;

    void addBookToAuthor(Author author, Book b);

    void removeBookFromAuthor(Author author, Book b);
}
