package com.projects.supporthub.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.projects.supporthub.model.Notice;

public interface NoticeService 
{
    public void newNotice(Notice notice);
    public void deleteNoticeById(int id);
    public Notice getNoticeById(int id);
    public Page<Notice> getAllNotices(Pageable pageable);
}