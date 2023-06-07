package com.projects.supporthub.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.projects.supporthub.model.UserTicketLink;

public interface UserTicketLinkRepository extends JpaRepository<UserTicketLink, Integer>
{
    /**
     * Retrive the links for the tickets issued by the specified user.
     * @param userId id of the User
     * @return a Collection of {@link UserTicketLink}s if one or more found
     */
    @Query("SELECT link FROM UserTicketLink link WHERE link.userId = :userId")
    @Transactional(readOnly = true)
    public List<UserTicketLink> findByUserId(String userId);

    /**
     * Retrive the link for the ticket with the specified ticket number.
     * @param ticketId id of the desired Ticket
     * @return the {@link UserTicketLink} if found
     */
    @Query("SELECT link FROM UserTicketLink link WHERE link.ticketId = :ticketId")
    @Transactional(readOnly = true)
    public UserTicketLink findByTicketId(UUID ticketId);

    /**
     * Delete the links for the tickets issued by the specified user.
     * @param userId id of the User
     */
    @Query("DELETE FROM UserTicketLink link WHERE link.userId = :userId")
    public void deleteByUserId(String userId);

    /**
     * Delete the link for the ticket with the specified ticket number.
     * @param ticketId id of the Ticket
     */
    @Query("DELETE FROM UserTicketLink link WHERE link.ticketId = :ticketId")
    public void deleteByTicketId(UUID ticketId);
}
