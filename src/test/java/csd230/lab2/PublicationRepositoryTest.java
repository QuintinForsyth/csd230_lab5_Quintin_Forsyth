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
public class PublicationRepositoryTest {

    @Autowired
    private PublicationRepository publicationRepository;

    @BeforeEach
    void cleanUpDatabase() {
        publicationRepository.deleteAll();
    }

    @Test
    void testFindByTitle() {
        Publication pub1 = new Publication(10.99, 10, "Description 1", "Tech World", 100);
        Publication pub2 = new Publication(15.99, 15, "Description 2", "Tech World", 200);
        publicationRepository.save(pub1);
        publicationRepository.save(pub2);

        List<Publication> foundPublications = publicationRepository.findByTitle("Tech World");

        assertEquals(2, foundPublications.size());
        assertTrue(foundPublications.stream().allMatch(p -> p.getTitle().equals("Tech World")));
    }

    @Test
    void testFindByCopies() {
        Publication pub1 = new Publication(12.99, 5, "Description A", "Science Journal", 150);
        Publication pub2 = new Publication(8.99, 8, "Description B", "History Digest", 150);
        publicationRepository.save(pub1);
        publicationRepository.save(pub2);

        List<Publication> foundPublications = publicationRepository.findByCopies(150);

        assertEquals(2, foundPublications.size());
        assertTrue(foundPublications.stream().allMatch(p -> p.getCopies() == 150));
    }

    @Test
    void testFindByCopiesGreaterThan() {
        Publication pub1 = new Publication(20.99, 10, "Description X", "Health Guide", 300);
        Publication pub2 = new Publication(15.99, 20, "Description Y", "Travel Guide", 500);
        publicationRepository.save(pub1);
        publicationRepository.save(pub2);

        List<Publication> foundPublications = publicationRepository.findByCopiesGreaterThan(250);

        assertEquals(2, foundPublications.size());
        assertTrue(foundPublications.stream().allMatch(p -> p.getCopies() > 250));
    }

    @Test
    void testFindByTitleContaining() {
        Publication pub1 = new Publication(25.99, 30, "Description C", "Adventure Weekly", 400);
        Publication pub2 = new Publication(18.99, 15, "Description D", "Adventure Monthly", 250);
        publicationRepository.save(pub1);
        publicationRepository.save(pub2);

        List<Publication> foundPublications = publicationRepository.findByTitleContaining("Adventure");

        assertEquals(2, foundPublications.size());
        assertTrue(foundPublications.stream().allMatch(p -> p.getTitle().contains("Adventure")));
    }

    @Test
    void testFindByTitleAndCopies() {
        Publication pub1 = new Publication(30.99, 40, "Description M", "Tech Today", 350);
        Publication pub2 = new Publication(35.99, 50, "Description N", "Tech Today", 450);
        publicationRepository.save(pub1);
        publicationRepository.save(pub2);

        List<Publication> foundPublications = publicationRepository.findByTitleAndCopies("Tech Today", 350);

        assertEquals(1, foundPublications.size());
        assertEquals(350, foundPublications.get(0).getCopies());
        assertEquals("Tech Today", foundPublications.get(0).getTitle());
    }
}
