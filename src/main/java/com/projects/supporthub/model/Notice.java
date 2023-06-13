package com.projects.supporthub.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notices")
public class Notice implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id", nullable = false, updatable = false)
    private int noticeId;

    @Column(name = "notice_date", nullable = false, updatable = false, insertable = false)
    private LocalDate noticeDate;

    @Column(name = "announcement", nullable = false)
    private String description;

    public int getId() 
    {
        return noticeId;
    }

    public LocalDate getNoticeDate() 
    {
        return noticeDate;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setId(int noticeId)
    {
        this.noticeId = noticeId;
    }

    public void setNoticeDate(LocalDate noticeDate) 
    {
        this.noticeDate = noticeDate;
    }
    
    public void setDescription(String description) 
    {
        this.description = description;
    }
}