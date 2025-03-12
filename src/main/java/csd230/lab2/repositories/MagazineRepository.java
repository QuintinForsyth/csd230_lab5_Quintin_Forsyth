package csd230.lab2.repositories;

import csd230.lab2.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {
    // Find by ID
    Magazine findById(long id);

    // Derived Query: Find all magazines with a specific order quantity
    List<Magazine> findByOrderQty(int orderQty);

    // Custom Query: Find magazines where the current issue is before a specific date
    @Query("SELECT m FROM Magazine m WHERE m.currIssue < :date")
    List<Magazine> findByCurrentIssueBefore(@Param("date") Date date);

    // Custom Query: Find magazines where the order quantity is within a specific range
    @Query("SELECT m FROM Magazine m WHERE m.orderQty BETWEEN :minQty AND :maxQty")
    List<Magazine> findByOrderQtyRange(@Param("minQty") int minQty, @Param("maxQty") int maxQty);

    // Custom Query: Find magazines with order quantity greater than a threshold
    @Query("SELECT m FROM Magazine m WHERE m.orderQty > :minOrderQty")
    List<Magazine> findByOrderQtyGreaterThan(@Param("minOrderQty") int minOrderQty);
}
