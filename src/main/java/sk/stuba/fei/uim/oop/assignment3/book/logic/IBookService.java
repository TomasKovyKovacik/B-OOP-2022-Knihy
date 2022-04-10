package sk.stuba.fei.uim.oop.assignment3.book.logic;

import sk.stuba.fei.uim.oop.assignment3.book.data.Book;
import sk.stuba.fei.uim.oop.assignment3.book.web.bodies.BookRequest;
import sk.stuba.fei.uim.oop.assignment3.book.web.bodies.BookUpdateRequest;
import sk.stuba.fei.uim.oop.assignment3.exception.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;

import java.util.List;

public interface IBookService {

    List<Book> getAll();

    Book create(BookRequest request) throws NotFoundException;

    Book getById(long id) throws NotFoundException;

    Book update(long id, BookUpdateRequest request) throws NotFoundException;

    void delete(long id) throws NotFoundException;

    int getAmount(long id) throws NotFoundException;

    int addAmount(long id, int increment) throws NotFoundException;

    int getLendCount(long id) throws NotFoundException;

    void increaseLendCount(Book b) throws IllegalOperationException;

    void decreaseLendCount(Book b) throws IllegalOperationException;

}
