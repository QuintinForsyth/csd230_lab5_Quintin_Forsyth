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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class DiscMagRepositoryTest {

    @Autowired
    private DiscMagRepository discMagRepository;

    @BeforeEach
    void cleanUpDatabase() {
        discMagRepository.deleteAll(); // Ensure a clean database before each test
    }

    @Test
    void testFindById() {
        DiscMag discMag = new DiscMag(true);
        discMag.setTitle("Tech Monthly");
        discMag.setPrice(15.99);
        discMagRepository.save(discMag);

        Optional<DiscMag> foundDiscMag = discMagRepository.findById(discMag.getId());

        assertNotNull(foundDiscMag);
        assertEquals(discMag.getTitle(), foundDiscMag.get().getTitle());
        assertTrue(foundDiscMag.get().getHasDisc());
    }

    @Test
    void testFindByHasDisc() {
        DiscMag discMagWithDisc = new DiscMag(true);
        discMagWithDisc.setTitle("Tech World");
        discMagWithDisc.setPrice(20.99);

        DiscMag discMagWithoutDisc = new DiscMag(false);
        discMagWithoutDisc.setTitle("Science Today");
        discMagWithoutDisc.setPrice(10.99);

        discMagRepository.save(discMagWithDisc);
        discMagRepository.save(discMagWithoutDisc);

        List<DiscMag> magsWithDisc = discMagRepository.findByHasDisc(true);
        List<DiscMag> magsWithoutDisc = discMagRepository.findByHasDisc(false);

        assertEquals(1, magsWithDisc.size());
        assertTrue(magsWithDisc.get(0).getHasDisc());

        assertEquals(1, magsWithoutDisc.size());
        assertFalse(magsWithoutDisc.get(0).getHasDisc());
    }

    @Test
    void testFindByTitleContaining() {
        DiscMag discMag1 = new DiscMag(true);
        discMag1.setTitle("The World of Tech");
        discMag1.setPrice(25.99);

        DiscMag discMag2 = new DiscMag(false);
        discMag2.setTitle("Tech Advances");
        discMag2.setPrice(18.50);

        discMagRepository.save(discMag1);
        discMagRepository.save(discMag2);

        List<DiscMag> foundMags = discMagRepository.findByTitleContaining("Tech");

        assertEquals(2, foundMags.size());
        assertTrue(foundMags.stream().allMatch(m -> m.getTitle().contains("Tech")));
    }

    @Test
    void testFindByPriceRange() {
        DiscMag cheapDiscMag = new DiscMag(true);
        cheapDiscMag.setTitle("Budget Tech");
        cheapDiscMag.setPrice(9.99);

        DiscMag expensiveDiscMag = new DiscMag(false);
        expensiveDiscMag.setTitle("Luxury Tech");
        expensiveDiscMag.setPrice(49.99);

        discMagRepository.save(cheapDiscMag);
        discMagRepository.save(expensiveDiscMag);

        List<DiscMag> foundMags = discMagRepository.findByPriceRange(10.00, 50.00);

        assertEquals(1, foundMags.size());
        assertEquals("Luxury Tech", foundMags.get(0).getTitle());
    }
}
