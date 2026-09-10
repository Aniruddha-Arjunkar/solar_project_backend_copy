package com.shulventures.solarservicesbackend.service;


import com.shulventures.solarservicesbackend.entity.Client;
import com.shulventures.solarservicesbackend.entity.PendingWork;
import com.shulventures.solarservicesbackend.repository.ClientRepository;
import com.shulventures.solarservicesbackend.repository.PendingWorkRepository;
import org.springframework.stereotype.Service;
//dto file import
import com.shulventures.solarservicesbackend.dto.PendingWorkResponse;
import java.util.List;

@Service
public class PendingWorkService {

    private final PendingWorkRepository pendingWorkRepository;
    private final ClientRepository clientRepository;


    public PendingWorkService(
            PendingWorkRepository pendingWorkRepository,
            ClientRepository clientRepository
    ) {
        this.pendingWorkRepository = pendingWorkRepository;
        this.clientRepository = clientRepository;
    }


    // ==================== CREATE PENDING WORK ====================

    public PendingWork createPendingWork(
            Long clientId,
            PendingWork pendingWork
    ) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Client not found with id: " + clientId
                        )
                );

        pendingWork.setClient(client);

        if (pendingWork.getStatus() == null ||
                pendingWork.getStatus().isBlank()) {

            pendingWork.setStatus("Pending");
        }

        return pendingWorkRepository.save(pendingWork);
    }


    // ==================== GET ALL ====================

    public List<PendingWork> getAllPendingWork() {
        return pendingWorkRepository.findAll();
    }


    // ==================== GET BY CLIENT ====================

    public List<PendingWork> getPendingWorkByClient(
            Long clientId
    ) {

        return pendingWorkRepository.findByClientId(clientId);
    }


    // ==================== GET PENDING ONLY ====================

    public List<PendingWork> getPendingWork() {

        return pendingWorkRepository.findByStatus("Pending");
    }

    // ==================== GET PENDING WORK WITH CLIENT ====================

    public List<PendingWorkResponse> getPendingWorkWithClient() {

        return pendingWorkRepository
                .findAll()
                .stream()
                .map(work -> new PendingWorkResponse(
                        work.getId(),
                        work.getClient().getId(),
                        work.getClient().getCustName(),
                        work.getWorkDescription(),
                        work.getAssignedTo(),
                        work.getDueDate(),
                        work.getStatus()
                ))
                .toList();
    }


    // ==================== MARK COMPLETE ====================
    public PendingWork markComplete(Long id) {
        PendingWork existingWork =
                pendingWorkRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Pending work not found with id: "
                                                + id
                                )
                        );
        existingWork.setStatus("Completed");
        return pendingWorkRepository.save(existingWork);
    }


    // ==================== UPDATE ====================
    public PendingWork updatePendingWork(
            Long id,
            PendingWork updatedWork
    ) {

        PendingWork existingWork =
                pendingWorkRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Pending work not found with id: "
                                                + id
                                )
                        );

        existingWork.setWorkDescription(
                updatedWork.getWorkDescription()
        );

        existingWork.setAssignedTo(
                updatedWork.getAssignedTo()
        );

        existingWork.setDueDate(
                updatedWork.getDueDate()
        );

        existingWork.setStatus(
                updatedWork.getStatus()
        );

        return pendingWorkRepository.save(existingWork);
    }


    // ==================== DELETE ====================

    public void deletePendingWork(Long id) {

        if (!pendingWorkRepository.existsById(id)) {
            throw new RuntimeException(
                    "Pending work not found with id: " + id
            );
        }

        pendingWorkRepository.deleteById(id);
    }
}
