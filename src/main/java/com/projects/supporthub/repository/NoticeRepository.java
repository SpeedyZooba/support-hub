package com.projects.supporthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.supporthub.model.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer>
{

}