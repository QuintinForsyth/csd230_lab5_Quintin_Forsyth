package csd230.lab2.controllers;

import csd230.lab2.entities.Cart;
import csd230.lab2.repositories.CartRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/cart")
public class CartRestController {
    private final CartRepository cartRepository;

    public CartRestController(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @CrossOrigin
    @GetMapping()
    public List<Cart> all() {
        return cartRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable Long id) {
        return cartRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Cart> newCart(@RequestBody Cart newCart) {
        Cart savedCart = cartRepository.save(newCart);
        return ResponseEntity.ok(savedCart);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cart> replaceCart(@RequestBody Cart newCart, @PathVariable Long id) {
        return cartRepository.findById(id)
                .map(existingCart -> {
                    existingCart.setItems(newCart.getItems());
                    return ResponseEntity.ok(cartRepository.save(existingCart));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}