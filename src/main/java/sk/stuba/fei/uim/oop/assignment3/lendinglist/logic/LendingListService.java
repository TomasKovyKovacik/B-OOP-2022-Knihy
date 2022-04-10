package sk.stuba.fei.uim.oop.assignment3.lendinglist.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.oop.assignment3.book.data.Book;
import sk.stuba.fei.uim.oop.assignment3.book.logic.IBookService;
import sk.stuba.fei.uim.oop.assignment3.exception.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;
import sk.stuba.fei.uim.oop.assignment3.lendinglist.data.LendingList;
import sk.stuba.fei.uim.oop.assignment3.lendinglist.data.LendingListRepository;
import sk.stuba.fei.uim.oop.assignment3.lendinglist.web.bodies.BookIdRequest;

import java.util.List;

@Service
public class LendingListService implements ILendingListService {

    @Autowired
    private LendingListRepository repository;

    @Autowired
    private IBookService bookService;

    @Override
    public LendingList create() {
        return this.repository.save(new LendingList());
    }

    @Override
    public List<LendingList> getAll() {
        return this.repository.findAll();
    }

    @Override
    public LendingList getById(long id) throws NotFoundException {
        LendingList list = this.repository.findLendingListById(id);
        if (list == null) {
            throw new NotFoundException();
        }
        return list;
    }

    @Override
    public void delete(long id) throws NotFoundException, IllegalOperationException {
        LendingList list = this.getById(id);
        if (list.isLended()) {
            for (Book b : list.getList()) {
                this.bookService.decreaseLendCount(b);
            }
        }
        this.repository.delete(list);
    }

    @Override
    public LendingList addToList(long id, BookIdRequest body) throws NotFoundException, IllegalOperationException {
        LendingList list = this.getUnlended(id);
        Book book = this.bookService.getById(body.getId());
        if (!list.getList().contains(book)) {
            list.getList().add(book);
        } else {
            throw new IllegalOperationException();
        }
        return this.repository.save(list);
    }

    @Override
    public LendingList removeFromList(long id, BookIdRequest body) throws NotFoundException, IllegalOperationException {
        LendingList list = this.getUnlended(id);
        Book book = this.bookService.getById(body.getId());
        if (list.getList().contains(book)) {
            list.getList().remove(book);
        } else {
            throw new IllegalOperationException();
        }
        return this.repository.save(list);
    }

    @Override
    public void lendList(Long listId) throws NotFoundException, IllegalOperationException {
        LendingList list = this.getUnlended(listId);
        for (Book b : list.getList()) {
            this.bookService.increaseLendCount(b);
        }
        list.setLended(true);
        this.repository.save(list);
    }

    private LendingList getUnlended(long id) throws NotFoundException, IllegalOperationException {
        LendingList list = this.getById(id);
        if (list.isLended()) {
            throw new IllegalOperationException();
        }
        return list;
    }
}
