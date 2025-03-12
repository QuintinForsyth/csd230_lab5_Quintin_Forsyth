package csd230.lab2.controllers;

import csd230.lab2.entities.Cart;
import csd230.lab2.entities.Ticket;
import csd230.lab2.repositories.CartRepository;
import csd230.lab2.repositories.TicketRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/ticket")
public class TicketRestController {
    private final TicketRepository ticketRepository;

    public TicketRestController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping()
    public List<Ticket> all() {
        return ticketRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) {
        return ticketRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Ticket> newTicket(@RequestBody Ticket newTicket) {
        Ticket savedTicket = ticketRepository.save(newTicket);
        return ResponseEntity.ok(savedTicket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> replaceTicket(@RequestBody Ticket newTicket, @PathVariable Long id) {
        return ticketRepository.findById(id)
                .map(existingTicket -> {
                    existingTicket.setText(newTicket.getText());
                    return ResponseEntity.ok(ticketRepository.save(existingTicket));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}