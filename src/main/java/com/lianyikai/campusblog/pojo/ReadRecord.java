package com.lianyikai.campusblog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

/*
 * 阅读记录实体类
 * */
@Entity
@Table(name = "read_record")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
public class ReadRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name="aid")
    private Article article;

    @ManyToOne
    @JoinColumn(name="uid")
    private User user;

    private Date createdAt;
    private double duration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
