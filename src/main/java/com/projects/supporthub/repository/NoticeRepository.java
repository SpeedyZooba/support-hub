package com.projects.supporthub.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.projects.supporthub.model.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer>
{
    /**
     * Retrieves {@link Notice}s based on their visibility status.
     */
    @Transactional(readOnly = true)
    @Query("SELECT notice FROM Notice notice WHERE notice.isDeleted = false")
    public Page<Notice> findByVisibility(Pageable pageable);
}