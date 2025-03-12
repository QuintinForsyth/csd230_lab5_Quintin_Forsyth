package csd230.lab2.controllers;

import csd230.lab2.entities.DiscMag;
import csd230.lab2.repositories.DiscMagRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/discmag")
public class DiscMagRestController {
    private final DiscMagRepository discMagRepository;

    public DiscMagRestController(DiscMagRepository discMagRepository) {
        this.discMagRepository = discMagRepository;
    }

    @CrossOrigin
    @GetMapping()
    List<DiscMag> all() {
        return discMagRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<DiscMag> getDiscMag(@PathVariable Long id) {
        return discMagRepository.findById(id);
    }

    @PostMapping()
    DiscMag newDiscMag(@RequestBody DiscMag newDiscMag) {
        return discMagRepository.save(newDiscMag);
    }

    @PutMapping("/{id}")
    DiscMag replaceDiscMag(@RequestBody DiscMag newDiscMag, @PathVariable Long id) {
        return discMagRepository.findById(id)
                .map(existingDiscMag -> {
                    existingDiscMag.setPrice(newDiscMag.getPrice());
                    existingDiscMag.setQuantity(newDiscMag.getQuantity());
                    existingDiscMag.setDescription(newDiscMag.getDescription());
                    existingDiscMag.setTitle(newDiscMag.getTitle());
                    existingDiscMag.setCopies(newDiscMag.getCopies());
                    existingDiscMag.setOrderQty(newDiscMag.getOrderQty());
                    existingDiscMag.setCurrIssue(newDiscMag.getCurrIssue());
                    existingDiscMag.setHasDisc(newDiscMag.getHasDisc());
                    return discMagRepository.save(existingDiscMag);
                })
                .orElseGet(() -> discMagRepository.save(newDiscMag));
    }

    @DeleteMapping("/{id}")
    void deleteDiscMag(@PathVariable Long id) {
        discMagRepository.deleteById(id);
    }
}