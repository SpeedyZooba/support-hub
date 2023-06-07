package com.projects.supporthub.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projects.supporthub.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, UUID>
{
    /**
     * Deletes tickets issued by the specific user.
     * @param issuerId id of the ticket issuer
     */
    @Query("DELETE FROM Ticket ticket WHERE ticket.issuerId = :issuerId")
    public void deleteByIssuerId(String issuerId);
}
