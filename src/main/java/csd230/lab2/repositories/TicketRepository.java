package csd230.lab2.repositories;

import csd230.lab2.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByText(String text);

    @Query("SELECT t FROM Ticket t WHERE t.text LIKE %:keyword%")
    List<Ticket> findByTextContaining(@Param("keyword") String keyword);

    @Query("SELECT t FROM Ticket t WHERE t.text LIKE :pattern")
    List<Ticket> findByTextLike(@Param("pattern") String pattern);
}
