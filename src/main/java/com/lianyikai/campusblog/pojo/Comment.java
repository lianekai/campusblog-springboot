package com.lianyikai.campusblog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

/*
 * 评论实体类
 * */
@Entity
@Table(name = "comment_record")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
public class Comment {
    public static final int IS_READ = 1;
    public static final int IS_NOT_READ = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private int pId;

    @ManyToOne
    @JoinColumn(name="articleId")
    private Article article;

    @ManyToOne
    @JoinColumn(name="answererId")
    private User answerer;

    @ManyToOne
    @JoinColumn(name="respondentId")
    private User respondent;

    private Date createdAt;
    private String content;
    private int isRead;

    @Transient
    private Comment parent;
    @Transient
    private int likeCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getAnswerer() {
        return answerer;
    }

    public void setAnswerer(User answerer) {
        this.answerer = answerer;
    }

    public User getRespondent() {
        return respondent;
    }

    public void setRespondent(User respondent) {
        this.respondent = respondent;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
