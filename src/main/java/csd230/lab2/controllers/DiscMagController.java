package csd230.lab2.controllers;

import csd230.lab2.entities.DiscMag;
import csd230.lab2.repositories.DiscMagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/discMags")  // Mapping for DiscMags
public class DiscMagController {

    @Autowired
    private DiscMagRepository discMagRepository;

    // Display all DiscMag items
    @GetMapping
    public String getAllDiscMags(Model model) {
        List<DiscMag> discMags = discMagRepository.findAll();
        model.addAttribute("discMags", discMags);
        return "discMagList";  // Returning 'discMagList.html' for the list view
    }

    // Display the details of a specific DiscMag item
    @GetMapping("/{id}")
    public String getDiscMagById(@PathVariable Long id, Model model) {
        DiscMag discMag = discMagRepository.findById(id).orElse(null);
        model.addAttribute("discMag", discMag);
        return "discMagDetails";  // Returning 'discMagDetails.html' for the detail view
    }

    // Show the form to add a new DiscMag
    @GetMapping("/addDiscMag")
    public String addDiscMagForm(Model model) {
        model.addAttribute("discMag", new DiscMag());
        return "addDiscMag";  // Returning 'addDiscMag.html' for adding a new DiscMag
    }

    // Handle the submission of a new DiscMag
    @PostMapping("/addDiscMag")
    public String addDiscMag(@ModelAttribute DiscMag discMag) {
        discMagRepository.save(discMag);
        return "redirect:/discMags";  // Redirect to the list page after adding
    }

    // Show the form to edit an existing DiscMag
    @GetMapping("/editDiscMag/{id}")
    public String editDiscMagForm(@PathVariable Long id, Model model) {
        DiscMag discMag = discMagRepository.findById(id).orElse(null);
        model.addAttribute("discMag", discMag);
        return "editDiscMag";  // Returning 'editDiscMag.html' for editing an existing DiscMag
    }

    // Handle the submission of an edited DiscMag
    @PostMapping("/editDiscMag/{id}")
    public String editDiscMagSubmit(@PathVariable Long id, @ModelAttribute DiscMag updatedDiscMag) {
        DiscMag existingDiscMag = discMagRepository.findById(id).orElse(null);
        if (existingDiscMag != null) {
            existingDiscMag.setTitle(updatedDiscMag.getTitle());
            existingDiscMag.setPrice(updatedDiscMag.getPrice());
            existingDiscMag.setQuantity(updatedDiscMag.getQuantity());
            existingDiscMag.setOrderQty(updatedDiscMag.getOrderQty());
            existingDiscMag.setCurrIssue(updatedDiscMag.getCurrIssue());
            existingDiscMag.setHasDisc(updatedDiscMag.getHasDisc());
            discMagRepository.save(existingDiscMag);
        }
        return "redirect:/discMags";  // Redirect to the list page after editing
    }

    // Handle the deletion of a DiscMag
    @GetMapping("/delete/{id}")
    public String deleteDiscMag(@PathVariable Long id) {
        discMagRepository.deleteById(id);
        return "redirect:/discMags";  // Redirect to the list page after deletion
    }

    // Handle the deletion of selected DiscMags (bulk delete)
    @PostMapping("/selection")
    public String deleteSelectedDiscMags(@RequestParam(value = "selectedDiscMags", required = false) List<Long> selectedDiscMagIds) {
        if (selectedDiscMagIds != null) {
            discMagRepository.deleteAllById(selectedDiscMagIds);
        }
        return "redirect:/discMags";  // Redirect to the list page after bulk deletion
    }
}
