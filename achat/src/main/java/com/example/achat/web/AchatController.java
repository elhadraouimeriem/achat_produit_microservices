package com.example.achat.web;

import com.example.achat.DTOs.AchatDTO;
import com.example.achat.DTOs.AchatReq;
import com.example.achat.services.AchatService;
import com.example.achat.services.AchatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achats")
public class AchatController {

    @Autowired
    private AchatServiceImpl achatService;

    // Create a new Achat
    @PostMapping
    public ResponseEntity<AchatDTO> createAchat(@RequestBody AchatReq achatReq) {
        AchatDTO createdAchat = achatService.createAchat(achatReq);
        return ResponseEntity.ok(createdAchat);
    }

    // Get Achat by ID
    @GetMapping("/{id}")
    public ResponseEntity<AchatDTO> getAchatById(@PathVariable Long id) {
        AchatDTO achatDTO = achatService.getAchatById(id);
        return ResponseEntity.ok(achatDTO);
    }

    // Update an existing Achat
    @PutMapping("/{id}")
    public ResponseEntity<AchatDTO> updateAchat(@PathVariable Long id, @RequestBody AchatDTO achatDTO) {
        AchatDTO updatedAchat = achatService.updateAchat(id, achatDTO);
        return ResponseEntity.ok(updatedAchat);
    }

    // Get all Achats
    @GetMapping
    public ResponseEntity<List<AchatDTO>> getAllAchats() {
        List<AchatDTO> achats = achatService.getAllAchats();
        return ResponseEntity.ok(achats);
    }

    // Delete an Achat by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchat(@PathVariable Long id) {
        achatService.deleteAchat(id);
        return ResponseEntity.noContent().build();
    }
}
