package com.projects.supporthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projects.supporthub.model.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer>
{
    /**
     * Deletes a month-old notices from the database using a native SQL query.
     */
    @Query(value = "DELETE FROM notices WHERE notice_date < NOW() - INTERVAL '1 month'", nativeQuery = true)
    public void deleteByAMonth();
}