package csd230.lab2.repositories;

import csd230.lab2.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
  List<Book> findByAuthor(String author);
  Optional<Book> findByIsbn(String isbn);
  List<Book> findByTitle(String title);
  List<Book> findByTitleContaining(String title); // Searches titles partially
}