package com.projects.supporthub.service.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.projects.supporthub.exception.NoticeNotFoundException;
import com.projects.supporthub.model.Notice;
import com.projects.supporthub.repository.NoticeRepository;
import com.projects.supporthub.service.NoticeService;

@Service
public class NoticeServiceImpl implements NoticeService
{
    private final NoticeRepository noticeRepo;

    private static final Logger log = LoggerFactory.getLogger(NoticeServiceImpl.class);

    public NoticeServiceImpl(NoticeRepository noticeRepo)
    {
        this.noticeRepo = noticeRepo;
    }

    public void newNotice(Notice notice)
    {
        log.info("Inside service method newNotice.");
        log.info("Service method newNotice calls repo method save.");
        noticeRepo.save(notice);
    }

    public void deleteNoticeById(int id)
    {
        log.info("Inside service method deleteNoticeById.");
        log.info("Service method deleteNoticeById is about to call repo method findById.");
        if (!noticeRepo.findById(id).isPresent())
        {
            log.error("Recevied invalid noticeId.");
            throw new NoticeNotFoundException("Notice not found.");
        }
        else
        {
            log.info("Service method deleteNoticeById calls repo method deleteById.");
            noticeRepo.deleteById(id);
        }
    }

    public Notice getNoticeById(int id)
    {
        log.info("Inside service method getNoticeById.");
        Optional<Notice> notice = noticeRepo.findById(id);
        if (!notice.isPresent())
        {
            log.error("Received invalid noticeId.");
            throw new NoticeNotFoundException("Notice not found.");
        }
        else
        {
            log.info("Returning Notice from the container.");
            return notice.get();
        }
    }

    public Page<Notice> getAllNotices(Pageable pageable)
    {
        log.info("Inside service method getAllNotices.");
        log.info("Service method getAllNotices calls repo method findAll for return.");
        return noticeRepo.findAll(pageable);
    }

    public List<Notice> getLatestNotices()
    {
        Page<Notice> notices = noticeRepo.findAll(PageRequest.of(0, 5, Sort.by("noticeDate").descending()));
        return notices.getContent();
    }
}