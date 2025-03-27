package csd230.lab2.repositories;

import csd230.lab2.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  List<CartItem> findByPrice(Double price);

  List<CartItem> findByPriceBetween(Double minPrice, Double maxPrice);

  List<CartItem> findByDescriptionContaining(String text);

  List<CartItem> findByCart_Id(Long cartId);

  CartItem findById(long id);

  @Query("SELECT ci FROM CartItem ci WHERE ci.price > :minPrice")
  List<CartItem> findItemsWithPriceGreaterThan(@Param("minPrice") double minPrice);

  @Query("SELECT ci FROM CartItem ci WHERE ci.quantity > :minQuantity")
  List<CartItem> findItemsWithQuantityGreaterThan(@Param("minQuantity") int minQuantity);
}
