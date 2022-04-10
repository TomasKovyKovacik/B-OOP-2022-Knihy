package sk.stuba.fei.uim.oop.assignment3.author.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAll();

    Author findAuthorById(Long id);
}
