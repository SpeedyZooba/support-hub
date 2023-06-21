package com.projects.supporthub.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.projects.supporthub.exception.NoticeNotFoundException;
import com.projects.supporthub.model.Notice;
import com.projects.supporthub.repository.NoticeRepository;
import com.projects.supporthub.service.NoticeService;

@Service
public class NoticeServiceImpl implements NoticeService
{
    private NoticeRepository noticeRepo;

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
        log.info("Service method getNoticeById is about to call repo method findById.");
        if (!noticeRepo.findById(id).isPresent())
        {
            log.error("Received invalid noticeId.");
            throw new NoticeNotFoundException("Notice not found.");
        }
        else
        {
            log.info("Service method getNoticeById calls repo method findById for return.");
            return noticeRepo.findById(id).get();
        }
    }

    public Page<Notice> getAllNotices(Pageable pageable)
    {
        log.info("Inside service method getAllNotices.");
        log.info("Service method getAllNotices calls repo method findAll for return.");
        return noticeRepo.findAll(pageable);
    }

    /**
     * Scheduled task to purge notices monthly for resource management.
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void purge()
    {
        log.info("Service method purge calls repo method deleteByAMonth.");
        noticeRepo.deleteByAMonth();
    }
}