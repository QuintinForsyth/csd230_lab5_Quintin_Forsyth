package csd230.lab2.controllers;

import csd230.lab2.entities.Magazine;
import csd230.lab2.repositories.MagazineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/magazines")
public class MagazineController {

    @Autowired
    private MagazineRepository magazineRepository;

    // Display all magazines
    @GetMapping
    public String getAllMagazines(Model model) {
        List<Magazine> magazines = magazineRepository.findAll();
        model.addAttribute("magazines", magazines);
        return "magazineList"; // magazineList.html
    }

    // Display the details of a specific magazine
    @GetMapping("/{id}")
    public String getMagazineById(@PathVariable Long id, Model model) {
        Magazine magazine = magazineRepository.findById(id).orElse(null);
        model.addAttribute("magazine", magazine);
        return "magazineDetails"; // magazineDetails.html
    }

    // Show the form to add a new magazine
    @GetMapping("/addMagazine")
    public String addMagazineForm(Model model) {
        model.addAttribute("magazine", new Magazine());
        return "addMagazine"; // addMagazine.html
    }

    // Handle the submission of a new magazine
    @PostMapping("/addMagazine")
    public String addMagazine(@ModelAttribute Magazine magazine) {
        magazineRepository.save(magazine);
        return "redirect:/magazines"; // Redirect to the magazines list
    }

    // Show the form to edit an existing magazine
    @GetMapping("/editMagazine/{id}")
    public String editMagazineForm(@PathVariable Long id, Model model) {
        Magazine magazine = magazineRepository.findById(id).orElse(null);
        model.addAttribute("magazine", magazine);
        return "editMagazine"; // editMagazine.html
    }

    // Handle the submission of an edited magazine
    @PostMapping("/editMagazine/{id}")
    public String editMagazineSubmit(@PathVariable Long id, @ModelAttribute Magazine updatedMagazine) {
        Magazine existingMagazine = magazineRepository.findById(id).orElse(null);
        if (existingMagazine != null) {
            existingMagazine.setTitle(updatedMagazine.getTitle());
            existingMagazine.setPrice(updatedMagazine.getPrice());
            existingMagazine.setQuantity(updatedMagazine.getQuantity());
            existingMagazine.setOrderQty(updatedMagazine.getOrderQty());
            existingMagazine.setCurrIssue(updatedMagazine.getCurrIssue());
            existingMagazine.setDescription(updatedMagazine.getDescription());
            magazineRepository.save(existingMagazine);
        }
        return "redirect:/magazines"; // Redirect to the magazines list
    }

    // Handle the deletion of a magazine
    @GetMapping("/delete/{id}")
    public String deleteMagazine(@PathVariable Long id) {
        magazineRepository.deleteById(id);
        return "redirect:/magazines"; // Redirect to the magazines list
    }

    // Handle the deletion of selected magazines (bulk delete)
    @PostMapping("/selection")
    public String deleteSelectedMagazines(@RequestParam(value = "selectedMagazines", required = false) List<Long> selectedMagazineIds) {
        if (selectedMagazineIds != null) {
            magazineRepository.deleteAllById(selectedMagazineIds);
        }
        return "redirect:/magazines"; // Redirect to the magazines list
    }
}
