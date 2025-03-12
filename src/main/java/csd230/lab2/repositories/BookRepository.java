package csd230.lab2.repositories;

import csd230.lab2.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
  List<Book> findByAuthor(String author);

  List<Book> findByISBN(String isbn);

  List<Book> findByTitle(String title);

  List<Book> findByTitleContaining(String keyword);

  @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword%")
  List<Book> searchByTitleContaining(@Param("keyword") String keyword);
}