package com.projects.supporthub.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.projects.supporthub.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, UUID>
{
    /**
     * Retrieves {@link Ticket}s issued by the specific {@link User}.
     * @param createdBy id of the {@link User} who issued the {@link Ticket}
     * @return a Collection of {@link Ticket}s if one or more found
     */
    @Transactional(readOnly = true)
    @Query("SELECT ticket FROM Ticket ticket WHERE ticket.createdBy = :createdBy")
    public Page<Ticket> findByCreatorId(@Param("createdBy") String createdBy, Pageable pageable);

    /**
     * Deletes {@link Ticket}s issued by the specific {@link User}.
     * @param createdBy id of the {@link User} who issued the {@link Ticket}
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Ticket ticket WHERE ticket.createdBy = :createdBy")
    public void deleteByCreatorId(@Param("createdBy") String createdBy);
}