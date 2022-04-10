package sk.stuba.fei.uim.oop.assignment3.lendinglist.data;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.stuba.fei.uim.oop.assignment3.book.data.Book;

import java.util.List;

public interface LendingListRepository extends JpaRepository<LendingList, Long> {
    List<LendingList> findAll();

    LendingList findLendingListById(Long id);
}
