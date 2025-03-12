package csd230.lab2;

import csd230.lab2.entities.*;
import csd230.lab2.repositories.*;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private final Faker faker = new Faker();

    @Test
    void testSaveAndFindById() {
        Book book = new Book(
                faker.number().randomDouble(2, 10, 100),
                faker.number().numberBetween(1, 100),
                faker.lorem().sentence(),
                faker.book().title(),
                faker.number().numberBetween(1, 10),
                faker.book().author(),
                faker.number().digits(13)
        );

        bookRepository.save(book);
        Optional<Book> foundBook = bookRepository.findById(book.getId());

        assertNotNull(foundBook);
        assertEquals(book.getTitle(), foundBook.get().getTitle());
        assertEquals(book.getAuthor(), foundBook.get().getAuthor());
    }

    @Test
    void testFindByAuthor() {
        String author = faker.book().author();

        Book book1 = new Book(19.99, 10, "Description 1", "Title 1", 5, author, "1234567890123");
        Book book2 = new Book(29.99, 20, "Description 2", "Title 2", 3, author, "9876543210987");

        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> booksByAuthor = bookRepository.findByAuthor(author);

        assertEquals(2, booksByAuthor.size());
        assertTrue(booksByAuthor.stream().allMatch(b -> b.getAuthor().equals(author)));
    }

    @Test
    void testFindByISBN() {
        bookRepository.deleteAll(); // Clear existing records

        String isbn = "1234567890123";
        Book book = new Book(19.99, 10, "Description", "Title", 5, faker.book().author(), isbn);
        bookRepository.save(book);

        List<Book> booksByISBN = bookRepository.findByISBN(isbn);

        assertEquals(1, booksByISBN.size());
        assertEquals(isbn, booksByISBN.get(0).getIsbn());
    }

    @Test
    void testFindByTitle() {
        String title = "Unique Title";

        Book book = new Book(19.99, 10, "Description", title, 5, faker.book().author(), "1234567890123");
        bookRepository.save(book);

        List<Book> booksByTitle = bookRepository.findByTitle(title);

        assertEquals(1, booksByTitle.size());
        assertEquals(title, booksByTitle.get(0).getTitle());
    }

    @Test
    void testFindByTitleContaining() {
        String keyword = "Adventure";

        Book book1 = new Book(19.99, 10, "Description 1", "Adventure in Space", 5, faker.book().author(), "1234567890123");
        Book book2 = new Book(29.99, 20, "Description 2", "The Great Adventure", 3, faker.book().author(), "9876543210987");

        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> booksContainingKeyword = bookRepository.findByTitleContaining(keyword);

        assertEquals(2, booksContainingKeyword.size());
        assertTrue(booksContainingKeyword.stream().allMatch(b -> b.getTitle().contains(keyword)));
    }

    @Test
    void testSearchByTitleContaining() {
        String keyword = "Mystery";

        Book book = new Book(19.99, 10, "Description", "Mystery of the Old House", 5, faker.book().author(), "1234567890123");
        bookRepository.save(book);

        List<Book> booksFound = bookRepository.searchByTitleContaining(keyword);

        assertEquals(1, booksFound.size());
        assertTrue(booksFound.get(0).getTitle().contains(keyword));
    }
}