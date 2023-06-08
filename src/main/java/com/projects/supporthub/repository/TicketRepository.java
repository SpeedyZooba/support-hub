package com.projects.supporthub.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projects.supporthub.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, UUID>
{
    /**
     * Retrieves tickets issued by the specific user.
     * @param createdBy id of the User who issued the ticket
     * @return a Collection of {@link Ticket}s if one or more found
     */
    @Query("SELECT ticket FROM Ticket ticket WHERE ticket.createdBy = :createdBy ORDER BY ticket.createdAt")
    public List<Ticket> findByCreatorId(String createdBy);

    /**
     * Deletes tickets issued by the specific user.
     * @param createdBy id of the {@link User} who issued the ticket
     */
    @Query("DELETE FROM Ticket ticket WHERE ticket.createdBy = :createdBy")
    public void deleteByCreatorId(String createdBy);
}
