package sk.stuba.fei.uim.oop.assignment3.lendinglist.logic;

import sk.stuba.fei.uim.oop.assignment3.exception.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exception.NotFoundException;
import sk.stuba.fei.uim.oop.assignment3.lendinglist.data.LendingList;
import sk.stuba.fei.uim.oop.assignment3.lendinglist.web.bodies.BookIdRequest;

import java.util.List;

public interface ILendingListService {
    LendingList create();

    List<LendingList> getAll();

    LendingList getById(long id) throws NotFoundException;

    void delete(long id) throws NotFoundException, IllegalOperationException;

    LendingList addToList(long id, BookIdRequest body) throws NotFoundException, IllegalOperationException;

    LendingList removeFromList(long id, BookIdRequest body) throws NotFoundException, IllegalOperationException;

    void lendList(Long listId) throws NotFoundException, IllegalOperationException;
}
