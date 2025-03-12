package csd230.lab2.controllers;

import csd230.lab2.entities.Magazine;
import csd230.lab2.repositories.MagazineRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/magazine")
public class MagazineRestController {
    private final MagazineRepository magazineRepository;

    public MagazineRestController(MagazineRepository magazineRepository) {
        this.magazineRepository = magazineRepository;
    }

    @CrossOrigin
    @GetMapping()
    List<Magazine> all() {
        return magazineRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Magazine> getMagazine(@PathVariable Long id) {
        return magazineRepository.findById(id);
    }

    @PostMapping()
    Magazine newMagazine(@RequestBody Magazine newMagazine) {
        return magazineRepository.save(newMagazine);
    }

    @PutMapping("/{id}")
    Magazine replaceMagazine(@RequestBody Magazine newMagazine, @PathVariable Long id) {
        return magazineRepository.findById(id)
                .map(existingMagazine -> {
                    existingMagazine.setPrice(newMagazine.getPrice());
                    existingMagazine.setQuantity(newMagazine.getQuantity());
                    existingMagazine.setDescription(newMagazine.getDescription());
                    existingMagazine.setTitle(newMagazine.getTitle());
                    existingMagazine.setCopies(newMagazine.getCopies());
                    existingMagazine.setOrderQty(newMagazine.getOrderQty());
                    existingMagazine.setCurrIssue(newMagazine.getCurrIssue());
                    return magazineRepository.save(existingMagazine);
                })
                .orElseGet(() -> magazineRepository.save(newMagazine));
    }

    @DeleteMapping("/{id}")
    void deleteMagazine(@PathVariable Long id) {
        magazineRepository.deleteById(id);
    }
}