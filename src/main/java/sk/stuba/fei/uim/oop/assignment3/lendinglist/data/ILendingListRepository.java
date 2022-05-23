package sk.stuba.fei.uim.oop.assignment3.lendinglist.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILendingListRepository extends JpaRepository<LendingList, Long> {
    List<LendingList> findAll();

    LendingList findLendingListById(Long id);
}
