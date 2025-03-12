package csd230.lab2.repositories;

import csd230.lab2.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
