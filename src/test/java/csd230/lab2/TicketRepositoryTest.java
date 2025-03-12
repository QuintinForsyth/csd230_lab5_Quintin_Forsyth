package csd230.lab2;

import csd230.lab2.entities.*;
import csd230.lab2.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void cleanUpDatabase() {
        ticketRepository.deleteAll(); // Ensure a clean database before each test
    }

    @Test
    void testFindByText() {
        Ticket ticket1 = new Ticket(15.99, 2, "Description 1", "Event A");
        Ticket ticket2 = new Ticket(25.99, 3, "Description 2", "Event B");
        ticketRepository.save(ticket1);
        ticketRepository.save(ticket2);

        List<Ticket> ticketsByText = ticketRepository.findByText("Event A");

        assertEquals(1, ticketsByText.size());
        assertEquals("Event A", ticketsByText.get(0).getText());
    }

    @Test
    void testFindByTextContaining() {
        Ticket ticket1 = new Ticket(19.99, 2, "Description 1", "Big Event A");
        Ticket ticket2 = new Ticket(29.99, 1, "Description 2", "Small Event B");
        ticketRepository.save(ticket1);
        ticketRepository.save(ticket2);

        List<Ticket> ticketsByTextContaining = ticketRepository.findByTextContaining("Event");

        assertEquals(2, ticketsByTextContaining.size());
        assertTrue(ticketsByTextContaining.stream().allMatch(ticket -> ticket.getText().contains("Event")));
    }

    @Test
    void testFindByTextLike() {
        Ticket ticket1 = new Ticket(10.99, 1, "Description 1", "Concert Night");
        Ticket ticket2 = new Ticket(12.99, 2, "Description 2", "Movie Night");
        Ticket ticket3 = new Ticket(14.99, 1, "Description 3", "Trivia Night");
        ticketRepository.save(ticket1);
        ticketRepository.save(ticket2);
        ticketRepository.save(ticket3);

        List<Ticket> ticketsByTextLike = ticketRepository.findByTextLike("%Night");

        assertEquals(3, ticketsByTextLike.size());
        assertTrue(ticketsByTextLike.stream().allMatch(ticket -> ticket.getText().endsWith("Night")));
    }
}
