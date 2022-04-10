package sk.stuba.fei.uim.oop.assignment3.author.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.oop.assignment3.author.data.Author;
import sk.stuba.fei.uim.oop.assignment3.author.data.AuthorRepository;
import sk.stuba.fei.uim.oop.assignment3.author.web.bodies.AuthorRequest;
import sk.stuba.fei.uim.oop.assignment3.book.data.Book;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;

import java.util.List;

@Service
public class AuthorService implements IAuthorService {

    @Autowired
    private AuthorRepository repository;

    @Override
    public List<Author> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Author create(AuthorRequest request) {
        return this.repository.save(new Author(request));
    }

    @Override
    public Author getById(long id) throws NotFoundException {
        Author a = this.repository.findAuthorById(id);
        if (a == null) {
            throw new NotFoundException();
        }
        return a;
    }

    @Override
    public Author update(long id, AuthorRequest request) throws NotFoundException {
        Author a = this.getById(id);
        if (request.getName() != null) {
            a.setName(request.getName());
        }
        if (request.getSurname() != null) {
            a.setSurname(request.getSurname());
        }
        return this.repository.save(a);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        this.repository.delete(this.getById(id));
    }

    @Override
    public void addBookToAuthor(Author author, Book b) {
        author.getBooks().add(b);
        this.repository.save(author);
    }

    @Override
    public void removeBookFromAuthor(Author author, Book b) {
        author.getBooks().remove(b);
        this.repository.save(author);
    }
}
