package csd230.lab2.repositories;

import csd230.lab2.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiscMagRepository extends JpaRepository<DiscMag, Long> {

    DiscMag findById(long id);

    List<DiscMag> findByHasDisc(boolean hasDisc);

    @Query("SELECT dm FROM DiscMag dm WHERE dm.title LIKE %:titleText%")
    List<DiscMag> findByTitleContaining(@Param("titleText") String titleText);

    @Query("SELECT dm FROM DiscMag dm WHERE dm.price BETWEEN :minPrice AND :maxPrice")
    List<DiscMag> findByPriceRange(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);
}
