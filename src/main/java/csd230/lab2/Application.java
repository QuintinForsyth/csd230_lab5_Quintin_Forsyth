package csd230.lab2;

import com.github.javafaker.Code;
import com.github.javafaker.Faker;
import com.github.javafaker.Number;
import csd230.lab2.entities.Book;
import csd230.lab2.entities.Cart;
import csd230.lab2.repositories.BookRepository;
import csd230.lab2.repositories.CartItemRepository;
import csd230.lab2.repositories.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;

@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Allow cross-origin requests (CORS) for React frontend
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:5173");
            }
        };
    }

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartRepository cartRepository;

    @Bean
    public CommandLineRunner demo(BookRepository repository) {
        return (args) -> {
            Cart cart = new Cart();
            cartRepository.save(cart);

            Faker faker = new Faker();
            com.github.javafaker.Book fakeBook = faker.book();
            Number number = faker.number();
            Code code = faker.code();

            // Save a few books
            for (int i = 0; i < 2; i++) {
                String title = fakeBook.title();
                double price = number.randomDouble(2, 10, 100);
                int copies = number.numberBetween(5, 20);
                int quantity = number.numberBetween(1, 50);
                String author = fakeBook.author();
                String isbn = code.isbn10();
                String description = "Book: " + title;

                Book book = new Book(price, quantity, description, title, copies, author, isbn);
                cart.addItem(book);
                repository.save(book);
            }

            cartRepository.save(cart);

            // Fetch all books
            log.info("Books found with findAll():");
            repository.findAll().forEach(book -> log.info(book.toString()));

            // Fetch an individual book by ID (handle missing case)
            Optional<Book> bookOptional = repository.findById(1L);
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                log.info("Book found with findById(1L): " + book);

                // Fetch book by ISBN
                Optional<Book> foundByIsbn = repository.findByIsbn(book.getIsbn());
                foundByIsbn.ifPresentOrElse(
                        b -> log.info("Book found with ISBN: " + b),
                        () -> log.info("No book found with that ISBN.")
                );
            } else {
                log.info("No book found with ID 1.");
            }

            // Fetch all items in cart
            log.info("Cart items found:");
            cartRepository.findAll().forEach(cartItem -> {
                log.info(cartItem.toString());
                cartItemRepository.findAll().forEach(item -> log.info("Cart item: " + item));
            });
        };
    }
}
