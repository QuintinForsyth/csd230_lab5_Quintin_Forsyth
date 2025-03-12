package csd230.lab2.controllers;

import csd230.lab2.entities.Ticket;
import csd230.lab2.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    // Display all tickets
    @GetMapping
    public String getAllTickets(Model model) {
        List<Ticket> tickets = ticketRepository.findAll();
        model.addAttribute("tickets", tickets);
        return "ticketList"; // This will return the 'ticketList.html' template
    }

    // Display the details of a specific ticket
    @GetMapping("/{id}")
    public String getTicketById(@PathVariable Long id, Model model) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        model.addAttribute("ticket", ticket);
        return "ticketDetails"; // This will return the 'ticketDetails.html' template
    }

    // Show the form to add a new ticket
    @GetMapping("/addTicket")
    public String addTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "addTicket"; // This will return the 'addTicket.html' template
    }

    // Handle the submission of a new ticket
    @PostMapping("/addTicket")
    public String addTicket(@ModelAttribute Ticket ticket) {
        ticketRepository.save(ticket);
        return "redirect:/tickets"; // Redirect to the ticket list page after adding
    }

    // Show the form to edit an existing ticket
    @GetMapping("/editTicket/{id}")
    public String editTicketForm(@PathVariable Long id, Model model) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        model.addAttribute("ticket", ticket);
        return "editTicket"; // This will return the 'editTicket.html' template
    }

    // Handle the submission of an edited ticket
    @PostMapping("/editTicket/{id}")
    public String editTicketSubmit(@PathVariable Long id, @ModelAttribute Ticket updatedTicket) {
        Ticket existingTicket = ticketRepository.findById(id).orElse(null);
        if (existingTicket != null) {
            existingTicket.setPrice(updatedTicket.getPrice());
            existingTicket.setQuantity(updatedTicket.getQuantity());
            existingTicket.setDescription(updatedTicket.getDescription());
            existingTicket.setText(updatedTicket.getText());
            ticketRepository.save(existingTicket);
        }
        return "redirect:/tickets"; // Redirect to the ticket list page after editing
    }

    // Handle the deletion of a ticket
    @GetMapping("/delete/{id}")
    public String deleteTicket(@PathVariable Long id) {
        ticketRepository.deleteById(id);
        return "redirect:/tickets"; // Redirect to the ticket list page after deletion
    }

    // Handle the deletion of selected tickets (bulk delete)
    @PostMapping("/selection")
    public String deleteSelectedTickets(@RequestParam(value = "selectedTickets", required = false) List<Long> selectedTicketIds) {
        if (selectedTicketIds != null) {
            ticketRepository.deleteAllById(selectedTicketIds);
        }
        return "redirect:/tickets"; // Redirect to the ticket list page after bulk deletion
    }
}
