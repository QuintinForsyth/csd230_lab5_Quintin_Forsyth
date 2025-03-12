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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MagazineRepositoryTest {

    @Autowired
    private MagazineRepository magazineRepository;

    @BeforeEach
    void cleanUpDatabase() {
        magazineRepository.deleteAll();
    }

    @Test
    void testFindById() {
        Magazine magazine = new Magazine(5.99, 50, "Tech Trends", "Tech Monthly", 200, 100, new Date());
        magazineRepository.save(magazine);

        Optional<Magazine> foundMagazine = magazineRepository.findById(magazine.getId());

        assertNotNull(foundMagazine);
        assertEquals(magazine.getTitle(), foundMagazine.get().getTitle());
        assertEquals(magazine.getOrderQty(), foundMagazine.get().getOrderQty());
    }

    @Test
    void testFindByOrderQty() {
        Magazine magazine1 = new Magazine(5.99, 50, "Tech Trends", "Tech Monthly", 200, 50, new Date());
        Magazine magazine2 = new Magazine(4.99, 40, "Science Insights", "Science Weekly", 150, 50, new Date());
        magazineRepository.save(magazine1);
        magazineRepository.save(magazine2);

        List<Magazine> foundMagazines = magazineRepository.findByOrderQty(50);

        assertEquals(2, foundMagazines.size());
        assertTrue(foundMagazines.stream().allMatch(m -> m.getOrderQty() == 50));
    }

    @Test
    void testFindByCurrentIssueBefore() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date pastDate = sdf.parse("2023-01-01");
        Date futureDate = sdf.parse("2025-01-01");

        Magazine oldMagazine = new Magazine(6.99, 60, "History Highlights", "History Monthly", 300, 80, pastDate);
        Magazine newMagazine = new Magazine(7.99, 70, "Future Tech", "Future Monthly", 400, 90, futureDate);

        magazineRepository.save(oldMagazine);
        magazineRepository.save(newMagazine);

        List<Magazine> foundMagazines = magazineRepository.findByCurrentIssueBefore(sdf.parse("2024-01-01"));

        assertEquals(1, foundMagazines.size());
        assertEquals("History Monthly", foundMagazines.get(0).getTitle());
    }


    @Test
    void testFindByOrderQtyGreaterThan() {
        Magazine magazine1 = new Magazine(5.99, 50, "Tech Trends", "Tech Monthly", 200, 20, new Date());
        Magazine magazine2 = new Magazine(4.99, 40, "Science Insights", "Science Weekly", 150, 40, new Date());
        Magazine magazine3 = new Magazine(6.99, 60, "History Highlights", "History Monthly", 300, 80, new Date());

        magazineRepository.save(magazine1);
        magazineRepository.save(magazine2);
        magazineRepository.save(magazine3);

        List<Magazine> foundMagazines = magazineRepository.findByOrderQtyGreaterThan(30);

        assertEquals(2, foundMagazines.size());
        assertTrue(foundMagazines.stream().allMatch(m -> m.getOrderQty() > 30));
    }
}
