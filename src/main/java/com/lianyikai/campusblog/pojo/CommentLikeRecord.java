package com.lianyikai.campusblog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

/*
 * 点赞评论实体类
 * */
@Entity
@Table(name = "comment_likes_record")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
public class CommentLikeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name="cid")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name="uid")
    private User user;

    private Date createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
