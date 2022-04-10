package sk.stuba.fei.uim.oop.assignment3.book.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAll();

    Book findBookById(Long id);
}
