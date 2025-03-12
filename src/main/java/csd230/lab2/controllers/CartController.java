package csd230.lab2.controllers;

import csd230.lab2.entities.*;
import csd230.lab2.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private TicketRepository ticketRepository;


    @PostMapping("/add")
    public String addToCart(@RequestParam("id") Long id,
                            @RequestParam("quantity") int quantity,
                            @RequestParam("price") double price) {

        // Check if the item is a Publication
        Publication pub = publicationRepository.findById(id).orElse(null);

        // Check if the item is a Ticket
        Ticket tick = ticketRepository.findById(id).orElse(null);

        CartItem cartItem = null; // Initialize cartItem variable

        // If the item is a Publication, create a CartItem for it
        if (pub != null) {
            cartItem = new CartItem();
            cartItem.setPrice(price);
            cartItem.setQuantity(quantity);
            cartItem.setDescription(pub.getTitle());
        }

        // If the item is a Ticket, create a CartItem for it
        else if (tick != null) {
            cartItem = new CartItem();
            cartItem.setPrice(price);
            cartItem.setQuantity(quantity);
            cartItem.setDescription(tick.getText());
        }

        // If neither a Publication nor Ticket was found, don't proceed
        if (cartItem != null) {
            Cart cart = cartRepository.findById(1L).orElse(new Cart()); // Assuming there's only one cart
            cartItem.setCart(cart);
            cartItemRepository.save(cartItem);
        }

        return "redirect:/cart/view";  // Redirect to the cart view
    }





    @GetMapping("/checkout")
    public String checkout(Model model) {
        Cart cart = cartRepository.findById(1L).orElse(new Cart());
        model.addAttribute("cart", cart);


        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("totalPrice", total);

        return "checkout";
    }
    @PostMapping("/checkout/confirm")
    public String confirmPurchase() {
        Cart cart = cartRepository.findById(1L).orElse(new Cart());
        for (CartItem item : cart.getItems()) {
            cart.removeItem(item);
        }
        cartRepository.save(cart);
        return "redirect:/cart/checkout/success";
    }


    @GetMapping("/checkout/success")
    public String orderSuccess() {
        return "checkout-success";
    }



    @GetMapping("/view")
    public String viewCart(Model model) {
        Cart cart = cartRepository.findById(1L).orElse(null);
        if (cart != null) {
            model.addAttribute("cart", cart);
        }

        return "cart";
    }

    @PostMapping("/remove-cart")
    public String removeFromCart(@RequestParam Long cartItemId) {
        Cart cart = cartRepository.findById(1L).orElse(new Cart());
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if (cartItem != null && cartItem.getCart().getId().equals(cart.getId())) {
            cart.removeItem(cartItem);
            cartItemRepository.delete(cartItem);
        }
        cartRepository.save(cart);

        return "redirect:/cart/view";
    }


}