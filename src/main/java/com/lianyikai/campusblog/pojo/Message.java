package com.lianyikai.campusblog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

/*
 * 留言实体类
 * */
@Entity
@Table(name = "leave_message_record")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String pageName;
    private int pId;

    @ManyToOne
    @JoinColumn(name="answererId")
    private User answerer;

    private int respondentId;
    private Date createdAt;
    private int likes;
    private String content;
    private int isRead;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public User getAnswerer() {
        return answerer;
    }

    public void setAnswerer(User answerer) {
        this.answerer = answerer;
    }

    public int getRespondentId() {
        return respondentId;
    }

    public void setRespondentId(int respondentId) {
        this.respondentId = respondentId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
}
