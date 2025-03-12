package csd230.lab2.repositories;

import csd230.lab2.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

    List<Publication> findByTitle(String title);

    List<Publication> findByCopies(int copies);

    Publication findById(long id);

    @Query("SELECT p FROM Publication p WHERE p.copies > :minCopies")
    List<Publication> findByCopiesGreaterThan(@Param("minCopies") int minCopies);

    @Query("SELECT p FROM Publication p WHERE p.title LIKE %:keyword%")
    List<Publication> findByTitleContaining(@Param("keyword") String keyword);

    @Query("SELECT p FROM Publication p WHERE p.title = :title AND p.copies = :copies")
    List<Publication> findByTitleAndCopies(@Param("title") String title, @Param("copies") int copies);
}
