package com.projects.supporthub.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.projects.supporthub.model.Notice;
import com.projects.supporthub.repository.NoticeRepository;
import com.projects.supporthub.service.NoticeService;

@Service
public class NoticeServiceImpl implements NoticeService
{
    private NoticeRepository noticeRepo;

    public NoticeServiceImpl(NoticeRepository noticeRepo)
    {
        this.noticeRepo = noticeRepo;
    }

    public void newNotice(Notice notice)
    {
        noticeRepo.save(notice);
    }

    public void deleteNoticeById(int id)
    {
        noticeRepo.deleteById(id);
    }

    public Notice getNoticeById(int id)
    {
        return noticeRepo.findById(id).get();
    }

    public Page<Notice> getAllNotices(Pageable pageable)
    {
        return noticeRepo.findAll(pageable);
    }

    /**
     * Scheduled task to purge notices monthly for resource management.
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void purge()
    {
        noticeRepo.deleteByAMonth();
    }
}