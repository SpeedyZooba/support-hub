package com.projects.supporthub.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.projects.supporthub.model.Notice;

public interface NoticeService 
{
    public void newNotice(Notice notice);
    public Notice getNoticeById(int id);
    public Page<Notice> getAllNotices(Pageable pageable);
    public List<Notice> getLatestNotices();
}