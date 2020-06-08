package com.lianyikai.campusblog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

/*
 * 友链实体类
 * */
@Entity
@Table(name = "friend_link")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
public class FriendLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String blogger;
    private String url;
    private Date createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBlogger() {
        return blogger;
    }

    public void setBlogger(String blogger) {
        this.blogger = blogger;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
