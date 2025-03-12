package csd230.lab2.repositories;

import csd230.lab2.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  // Derived Query: Find items by price
  List<CartItem> findByPrice(Double price);

  // Derived Query: Find items within a price range
  List<CartItem> findByPriceBetween(Double minPrice, Double maxPrice);

  // Derived Query: Find items by description containing text
  List<CartItem> findByDescriptionContaining(String text);

  // Derived Query: Find items by cart ID
  List<CartItem> findByCart_Id(Long cartId);

  CartItem findById(long id);

  // Custom Query: Find items with a price greater than a certain value
  @Query("SELECT ci FROM CartItem ci WHERE ci.price > :minPrice")
  List<CartItem> findItemsWithPriceGreaterThan(@Param("minPrice") double minPrice);

  // Custom Query: Find items with quantity greater than a given value
  @Query("SELECT ci FROM CartItem ci WHERE ci.quantity > :minQuantity")
  List<CartItem> findItemsWithQuantityGreaterThan(@Param("minQuantity") int minQuantity);
}
