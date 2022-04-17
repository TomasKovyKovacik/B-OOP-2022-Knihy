package sk.stuba.fei.uim.oop.assignment3.book.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.oop.assignment3.author.data.Author;
import sk.stuba.fei.uim.oop.assignment3.author.logic.IAuthorService;
import sk.stuba.fei.uim.oop.assignment3.book.data.Book;
import sk.stuba.fei.uim.oop.assignment3.book.data.BookRepository;
import sk.stuba.fei.uim.oop.assignment3.book.web.bodies.BookRequest;
import sk.stuba.fei.uim.oop.assignment3.book.web.bodies.BookUpdateRequest;
import sk.stuba.fei.uim.oop.assignment3.exception.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;

import java.util.List;


@Service
public class BookService implements IBookService {

    @Autowired
    private BookRepository repository;

    @Autowired
    private IAuthorService authorService;

    @Override
    public List<Book> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Book create(BookRequest request) throws NotFoundException {
        Author author = this.authorService.getById(request.getAuthor());
        Book b = this.repository.save(new Book(request, author));
        this.authorService.addBookToAuthor(author, b);
        return b;
    }

    @Override
    public Book getById(long id) throws NotFoundException {
        Book b = this.repository.findBookById(id);
        if (b == null) {
            throw new NotFoundException();
        }
        return b;
    }

    @Override
    public Book update(long id, BookUpdateRequest request) throws NotFoundException {
        Book b = this.getById(id);
        if (request.getName() != null) {
            b.setName(request.getName());
        }
        if (request.getDescription() != null) {
            b.setDescription(request.getDescription());
        }
        if (request.getPages() != 0) {
            b.setPages(request.getPages());
        }
        if (request.getAuthor() != 0) {
            Author author = this.authorService.getById(request.getAuthor());
            this.authorService.removeBookFromAuthor(b.getAuthor(), b);
            this.authorService.addBookToAuthor(author, b);
            b.setAuthor(author);
        }
        return this.repository.save(b);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        Book b = this.getById(id);
        b.getAuthor().getBooks().remove(b);
        this.repository.delete(b);
    }

    @Override
    public int getAmount(long id) throws NotFoundException {
        return this.getById(id).getAmount();
    }

    @Override
    public int addAmount(long id, int increment) throws NotFoundException {
        Book b = this.getById(id);
        b.setAmount(b.getAmount() + increment);
        this.repository.save(b);
        return b.getAmount();
    }

    @Override
    public int getLendCount(long id) throws NotFoundException {
        return this.getById(id).getLendCount();
    }

    @Override
    public void increaseLendCount(Book b) throws IllegalOperationException {
        if (b.getLendCount() + 1 > b.getAmount()) {
            throw new IllegalOperationException();
        }
        b.setLendCount(b.getLendCount() + 1);
        this.repository.save(b);
    }

    @Override
    public void decreaseLendCount(Book b) throws IllegalOperationException {
        if (b.getLendCount() - 1 < 0) {
            throw new IllegalOperationException();
        }
        b.setLendCount(b.getLendCount() - 1);
        this.repository.save(b);
    }
}
