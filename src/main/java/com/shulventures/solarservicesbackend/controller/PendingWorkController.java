package com.shulventures.solarservicesbackend.controller;

import com.shulventures.solarservicesbackend.entity.PendingWork;
import com.shulventures.solarservicesbackend.service.PendingWorkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//DTO file import
import com.shulventures.solarservicesbackend.dto.PendingWorkResponse;
import java.util.List;

@RestController
@RequestMapping("/api/pending-work")
@CrossOrigin(origins = "http://localhost:5173")
public class PendingWorkController {

    private final PendingWorkService pendingWorkService;


    public PendingWorkController(
            PendingWorkService pendingWorkService
    ) {
        this.pendingWorkService = pendingWorkService;
    }


    // ==================== CREATE ====================

    @PostMapping("/client/{clientId}")
    public ResponseEntity<PendingWork> createPendingWork(
            @PathVariable Long clientId,
            @RequestBody PendingWork pendingWork
    ) {
        PendingWork savedWork =
                pendingWorkService.createPendingWork(
                        clientId,
                        pendingWork
                );

        return new ResponseEntity<>(
                savedWork,
                HttpStatus.CREATED
        );
    }


    // ==================== GET ALL ====================

    @GetMapping
    public ResponseEntity<List<PendingWork>> getAllPendingWork() {

        return ResponseEntity.ok(
                pendingWorkService.getAllPendingWork()
        );
    }


    // ==================== GET PENDING ONLY ====================

    @GetMapping("/pending")
    public ResponseEntity<List<PendingWork>> getPendingWork() {

        return ResponseEntity.ok(
                pendingWorkService.getPendingWork()
        );
    }


    // ==================== GET PENDING WORK WITH CLIENT =============

    @GetMapping("/pending-with-client")
    public ResponseEntity<List<PendingWorkResponse>> getPendingWorkWithClient() {

        return ResponseEntity.ok(
                pendingWorkService.getPendingWorkWithClient()
        );
    }


    // ==================== GET BY CLIENT ====================

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PendingWork>> getByClient(
            @PathVariable Long clientId
    ) {

        return ResponseEntity.ok(
                pendingWorkService.getPendingWorkByClient(
                        clientId
                )
        );
    }


    // ==================== UPDATE ====================
    @PutMapping("/{id}")
    public ResponseEntity<PendingWork> updatePendingWork(
            @PathVariable Long id,
            @RequestBody PendingWork pendingWork
    ) {

        return ResponseEntity.ok(
                pendingWorkService.updatePendingWork(
                        id,
                        pendingWork
                )
        );
    }


    // ==================== MARK COMPLETE ====================
    @PutMapping("/{id}/complete")
    public ResponseEntity<PendingWork> markComplete(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                pendingWorkService.markComplete(id)
        );
    }

    // ==================== DELETE ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePendingWork(
            @PathVariable Long id
    ) {
        pendingWorkService.deletePendingWork(id);
        return ResponseEntity.noContent().build();
    }
}
