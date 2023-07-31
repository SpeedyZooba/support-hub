package com.projects.supporthub.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "notices")
public class Notice implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id", nullable = false, updatable = false)
    private int noticeId;

    @Column(name = "notice_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate noticeDate;

    @Column(name = "header", nullable = false)
    private String header;

    @Column(name = "announcement", nullable = false)
    private String description;

    public int getNoticeId() 
    {
        return noticeId;
    }

    public LocalDate getNoticeDate() 
    {
        return noticeDate;
    }

    public String getHeader() 
    {
        return header;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setNoticeId(int noticeId)
    {
        this.noticeId = noticeId;
    }

    public void setNoticeDate(LocalDate noticeDate) 
    {
        this.noticeDate = noticeDate;
    }

    public void setHeader(String header) 
    {
        this.header = header;
    }
    
    public void setDescription(String description) 
    {
        this.description = description;
    }

    @PrePersist
    public void initCreationDate()
    {
        noticeDate = LocalDate.now();
    }
}