package csd230.lab2;

import csd230.lab2.entities.*;
import csd230.lab2.repositories.*;
import com.github.javafaker.Code;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private DiscMagRepository discMagRepository;

    @Autowired
    private MagazineRepository magazineRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {
            Faker faker = new Faker();
            Code code = faker.code();

            // Create a cart
            Cart cart = new Cart();
            cartRepository.save(cart);

            // Create and save books
            for (int i = 0; i < 5; i++) {
                Book book = new Book(
                        faker.number().randomDouble(2, 10, 100),
                        faker.number().numberBetween(1, 50),
                        "Book: " + faker.book().title(),
                        faker.book().title(),
                        faker.number().numberBetween(5, 20),
                        faker.book().author(),
                        code.isbn10()
                );
                cart.addItem(book);
                bookRepository.save(book);
            }
            cartRepository.save(cart);

            // Log books
            log.info("Books found with findAll():");
            bookRepository.findAll().forEach(book -> log.info(book.toString()));

            // Create and save magazines
            for (int i = 0; i < 3; i++) {
                Magazine magazine = new Magazine(
                        faker.number().randomDouble(2, 5, 50),
                        faker.number().numberBetween(10, 30),
                        "Magazine: " + faker.book().title(),
                        faker.book().title(),
                        faker.number().numberBetween(1, 100),
                        faker.number().numberBetween(10, 50),
                        faker.date().birthday()
                );
                magazineRepository.save(magazine);
            }

            // Log magazines
            log.info("Magazines found with findAll():");
            magazineRepository.findAll().forEach(magazine -> log.info(magazine.toString()));

            // Create and save disc magazines
            for (int i = 0; i < 3; i++) {
                DiscMag discMag = new DiscMag();
                discMag.setPrice(faker.number().randomDouble(2, 5, 50));
                discMag.setQuantity(faker.number().numberBetween(10, 30));
                discMag.setDescription("DiscMag: " + faker.book().title());
                discMag.setTitle(faker.book().title());
                discMag.setCopies(faker.number().numberBetween(1, 100));
                discMag.setOrderQty(faker.number().numberBetween(10, 50));
                discMag.setCurrIssue(faker.date().birthday());
                discMag.setHasDisc(faker.bool().bool());
                discMagRepository.save(discMag);
            }

            // Log disc magazines
            log.info("DiscMags found with findAll():");
            discMagRepository.findAll().forEach(discMag -> log.info(discMag.toString()));

            // Create and save tickets
            for (int i = 0; i < 4; i++) {
                Ticket ticket = new Ticket(
                        faker.number().randomDouble(2, 1, 20),
                        faker.number().numberBetween(1, 5),
                        "Ticket: " + faker.book().title(),
                        "Event: " + faker.company().name()
                );
                ticketRepository.save(ticket);
            }

            // Log tickets
            log.info("Tickets found with findAll():");
            ticketRepository.findAll().forEach(ticket -> log.info(ticket.toString()));
        };
    }
}
